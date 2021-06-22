package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.APK;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ApexKingsImport implements TTImport {


	private static final String[] kingsPath = {".*\\\\Kings\\\\KingsPathMap.txt"};
	private TTDocument forwardMapDocument;
	private TTDocument backMapDocument;
	private TTDocument valueSetDocument;
	private final Map<String, List<Snomed>> r2ToSnomed = new HashMap<>();
	private final Map<TTEntity, List<Snomed>> apexToSnomed = new HashMap();
	private final Map<String, String> apexToRead = new HashMap<>();
	private final Map<String, List<TTEntity>> snomedToApex = new HashMap<>();
	private static final TTIriRef utl= TTIriRef.iri(IM.NAMESPACE+"VSET_UnifiedTestList");
	private static final Set<String> utlSet= new HashSet<>();
	private static final Set<String> utlMembers= new HashSet<>();
	private Connection conn;



	private static class Snomed {
		private String entityId;
		private String descId;
		private String name;
	}



	@Override
	public TTImport importData(String inFolder) throws Exception {
		conn = ImportUtils.getConnection();
		TTManager manager = new TTManager();
		TTManager backManager = new TTManager();
		TTManager vsetManager = new TTManager();
		forwardMapDocument = manager.createDocument(IM.GRAPH_APEX_KINGS.getIri());
		backMapDocument = backManager.createDocument(IM.GRAPH_APEX_KINGS.getIri());
		backMapDocument.setCrud(IM.UPDATE);
		valueSetDocument= vsetManager.createDocument(IM.GRAPH_DISCOVERY.getIri());
		valueSetDocument.setCrud(IM.ADD);
		importR2Matches();
		importApexKings(inFolder);
		createManyToManyMaps();
		createBackMaps();
		createForwardMaps();
		addToUtlSet();
		TTDocumentFiler filer = new TTDocumentFiler(forwardMapDocument.getGraph());
		filer.fileDocument(forwardMapDocument);
		filer = new TTDocumentFiler(backMapDocument.getGraph());
		filer.fileDocument(backMapDocument);
		if (valueSetDocument.getEntities()!=null) {
			filer = new TTDocumentFiler(valueSetDocument.getGraph());
			filer.fileDocument(valueSetDocument);
		}
		return this;
	}

	private void addToUtlSet() throws SQLException {
		PreparedStatement getUtlSet= conn.prepareStatement("Select o.iri\n" +
			"from tpl\n" +
			"join entity e on tpl.subject= e.dbid\n" +
			"join entity p on tpl.predicate=p.dbid\n" +
			"join entity o on tpl.object= o.dbid\n" +
			"where e.iri='http://endhealth.info/im#VSET_UnifiedTestList' and p.iri='http://endhealth.info/im#hasMembers'");
		ResultSet rs= getUtlSet.executeQuery();
		while (rs.next()){
			String member= rs.getString("iri");
			utlSet.add(member);
		}
		for (String member:utlMembers){
			if (!utlSet.contains(member)){
				if (valueSetDocument.getEntities()==null){
					valueSetDocument.addEntity( new TTEntity().setIri(IM.NAMESPACE+"VSET_UnifiedTestList"));
				}
				valueSetDocument.getEntities().get(0).addObject(IM.HAS_MEMBER,TTIriRef.iri(member));
			}
		}
	}


	private void createForwardMaps() {
		for (Map.Entry<TTEntity, List<Snomed>> entry : apexToSnomed.entrySet()) {
			TTEntity apexEntity = entry.getKey();
			forwardMapDocument.addEntity(apexEntity);
			List<Snomed> maps = entry.getValue();
			Snomed snomed = getPreferred(maps);
			TTNode target = new TTNode();
			target.set(IM.MATCHED_TO, TTIriRef.iri(SNOMED.NAMESPACE + snomed.entityId));
			target.set(IM.HAS_TERM_CODE, TTLiteral.literal(snomed.descId));
			apexEntity.set(IM.HAS_MAP, new TTArray());
			apexEntity.get(IM.HAS_MAP).asArray().add(target);
			utlMembers.add(SNOMED.NAMESPACE+snomed.entityId);
		}
	}

	private Snomed getPreferred(List<Snomed> maps) {
		if (maps.size() == 1)
			return maps.get(0);
		for (Snomed snomed : maps)
			if (snomed.name.contains("observable"))
				return snomed;

		return maps.get(0);
	}

	private void createManyToManyMaps() {
		for (TTEntity apexEntity : forwardMapDocument.getEntities()) {
			String apexCode = apexEntity.getCode();
			String read = apexToRead.get(apexCode);
			List<Snomed> maps = r2ToSnomed.get(read);
			if (maps != null) {
				for (Snomed snomed : maps) {
					List<TTEntity> apexList = snomedToApex.computeIfAbsent(snomed.entityId, k -> new ArrayList<>());
					apexList.add(apexEntity);
					List<Snomed> snomedList = apexToSnomed.computeIfAbsent(apexEntity, k -> new ArrayList<>());
					snomedList.add(snomed);
				}
			}
		}
	}

	private void createBackMaps() {
		for (Map.Entry<String, List<TTEntity>> entry : snomedToApex.entrySet()) {
			String entityId = entry.getKey();
			TTEntity snomedEntity = new TTEntity()
				.setIri(SNOMED.NAMESPACE + entityId);
			backMapDocument.addEntity(snomedEntity);
			List<TTEntity> apexList = entry.getValue();
			for (TTEntity apex : apexList) {
				if (snomedEntity.get(IM.HAS_MAP) == null) {
					TTNode map = new TTNode();
					map.set(IM.ANY_OF,new TTArray());
					snomedEntity.set(IM.HAS_MAP, new TTArray());
					snomedEntity.get(IM.HAS_MAP).asArray().add(map);
				}
				TTNode map= snomedEntity.get(IM.HAS_MAP).asArray().getElements().get(0).asNode();
				map.addObject(IM.ANY_OF,TTIriRef.iri(apex.getIri()));
			}
		}
	}


	private void importR2Matches() throws SQLException, ClassNotFoundException {
		System.out.println("Retrieving read 2 snomed map");

		PreparedStatement getR2Matches= conn.prepareStatement("select tc.code as code,c.code as entityId,"+
				"synonym.code as termCode,synonym.term as synonym,c.name as entityName\n" +
			"from term_code tc\n" +
			"join entity s on tc.scheme=s.dbid\n" +
			"join entity c on tc.entity=c.dbid\n" +
			"left join entity cscheme on c.scheme= cscheme.iri\n" +
			"left join term_code synonym on tc.entity_term_code= synonym.code and synonym.scheme=cscheme.dbid\n" +
			"where s.iri='"+ IM.CODE_SCHEME_READ.getIri()+"'");
		ResultSet rs= getR2Matches.executeQuery();
		while (rs.next()){
			Snomed snomed= new Snomed();
			String read= rs.getString("code");
			snomed.entityId= rs.getString("entityId");
			snomed.descId= rs.getString("termCode");
			snomed.name= rs.getString("entityName");
			List<Snomed> maps = r2ToSnomed.computeIfAbsent(read, k -> new ArrayList<>());
			maps.add(snomed);

		}
	}

	private void importApexKings(String folder) throws IOException {
		System.out.println("Importing kings code file");

		Path file = ImportUtils.findFileForId(folder, kingsPath[0]);
		try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
			reader.readLine();
			String line = reader.readLine();
			int count = 0;
			while (line != null && !line.isEmpty()) {
				String[] fields = line.split("\t");
				String readCode= fields[0];
				String code= fields[1]+"-"+(fields[2].toLowerCase());
				String iri = APK.NAMESPACE+ fields[1]+ "-"+(fields[2].replace(" ",""));
				TTEntity entity= new TTEntity()
					.setIri(iri)
					.addType(IM.LEGACY)
					.setName(fields[2])
					.setDescription("Local apex Kings trust pathology system entity ")
					.setCode(code)
					.setScheme(IM.CODE_SCHEME_APEX_KINGS);
				forwardMapDocument.addEntity(entity);
				apexToRead.put(code,readCode);
				count++;
				if (count % 500 == 0) {
					System.out.println("Processed " + count + " records");
				}

				line = reader.readLine();
			}
			System.out.println("Process ended with " + count + " records");
		}

	}


	@Override
	public TTImport validateFiles(String inFolder) {
		ImportUtils.validateFiles(inFolder,kingsPath);
		return null;
	}

	@Override
	public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
		return null;
	}

	@Override
	public void close() throws Exception {

	}
}
