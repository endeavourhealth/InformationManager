package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTDocumentFilerJDBC;
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

public class WinPathKingsImport implements TTImport {


	private static final String[] kingsWinPath = {".*\\\\Kings\\\\Winpath.txt"};
	private TTDocument forwardMapDocument;
	private TTDocument backMapDocument;
	private TTDocument valueSetDocument;
	private final Map<String, List<Snomed>> r2ToSnomed = new HashMap<>();
	private final Map<TTEntity, List<Snomed>> winpathToSnomed = new HashMap();
	private final Map<String, String> winpathToRead = new HashMap<>();
	private final Map<String, List<TTEntity>> snomedTowinpath = new HashMap<>();
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
	public TTImport importData(String inFolder, boolean bulkImport, Map<String,Integer> entityMap) throws Exception {
		TTManager manager = new TTManager();
		TTManager backManager = new TTManager();
		TTManager vsetManager = new TTManager();
		conn = ImportUtils.getConnection();
		forwardMapDocument = manager.createDocument(IM.GRAPH_WINPATH_KINGS.getIri());
		backMapDocument = backManager.createDocument(IM.GRAPH_WINPATH_KINGS.getIri());
		backMapDocument.setCrud(IM.UPDATE);
		valueSetDocument= vsetManager.createDocument(IM.GRAPH_DISCOVERY.getIri());
		valueSetDocument.setCrud(IM.ADD);
		importR2Matches();
		importWinPathKings(inFolder);
		createManyToManyMaps();
		createBackMaps();
		createForwardMaps();
		addToUtlSet();
		TTDocumentFiler filer = new TTDocumentFilerJDBC();
		filer.fileDocument(forwardMapDocument,bulkImport,entityMap);
		filer = new TTDocumentFilerJDBC();
		filer.fileDocument(backMapDocument, bulkImport,entityMap);
		if (valueSetDocument.getEntities()!=null) {
			filer = new TTDocumentFilerJDBC();
			filer.fileDocument(valueSetDocument,bulkImport,entityMap);
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
		for (Map.Entry<TTEntity, List<Snomed>> entry : winpathToSnomed.entrySet()) {
			TTEntity winpathEntity = entry.getKey();
			forwardMapDocument.addEntity(winpathEntity);
			List<Snomed> maps = entry.getValue();
			Snomed snomed = getPreferred(maps);
			TTNode target = new TTNode();
			target.set(IM.MATCHED_TO, TTIriRef.iri(SNOMED.NAMESPACE + snomed.entityId));
			target.set(IM.HAS_TERM_CODE, TTLiteral.literal(snomed.descId));
			winpathEntity.set(IM.HAS_MAP, new TTArray());
			winpathEntity.get(IM.HAS_MAP).asArray().add(target);
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
		for (TTEntity winpathEntity : forwardMapDocument.getEntities()) {
			String winpathCode = winpathEntity.getCode();
			String read = winpathToRead.get(winpathCode);
			List<Snomed> maps = r2ToSnomed.get(read);
			if (maps != null) {
				for (Snomed snomed : maps) {
					List<TTEntity> winpathList = snomedTowinpath.computeIfAbsent(snomed.entityId, k -> new ArrayList<>());
					winpathList.add(winpathEntity);
					List<Snomed> snomedList = winpathToSnomed.computeIfAbsent(winpathEntity, k -> new ArrayList<>());
					snomedList.add(snomed);
				}
			}
		}
	}

	private void createBackMaps() {
		for (Map.Entry<String, List<TTEntity>> entry : snomedTowinpath.entrySet()) {
			String entityId = entry.getKey();
			TTEntity snomedEntity = new TTEntity()
				.setIri(SNOMED.NAMESPACE + entityId);
			backMapDocument.addEntity(snomedEntity);
			List<TTEntity> winpathList = entry.getValue();
			for (TTEntity winpath : winpathList) {
				if (snomedEntity.get(IM.HAS_MAP) == null) {
					TTNode map = new TTNode();
					map.set(IM.SOME_OF,new TTArray());
					snomedEntity.set(IM.HAS_MAP, new TTArray());
					snomedEntity.get(IM.HAS_MAP).asArray().add(map);
				}
				TTNode map= snomedEntity.get(IM.HAS_MAP).asArray().getElements().get(0).asNode();
				map.addObject(IM.SOME_OF,TTIriRef.iri(winpath.getIri()));
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

	private void importWinPathKings(String folder) throws IOException {
		System.out.println("Importing kings code file");

		Path file = ImportUtils.findFileForId(folder, kingsWinPath[0]);
		try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
			reader.readLine();
			String line = reader.readLine();
			int count = 0;
			while (line != null && !line.isEmpty()) {
				String[] fields = line.split("\t");
				String readCode= fields[2];
				String code= fields[0]+"-"+(fields[1].toLowerCase());
				String iri = WPK.NAMESPACE+ fields[0].replace(" ","")+ "-"+(fields[1].replace(" ",""));
				TTEntity entity= new TTEntity()
					.setIri(iri)
					.addType(IM.LEGACY)
					.setName(fields[1])
					.setDescription("Local winpath Kings trust pathology system entity ")
					.setCode(code)
					.setScheme(IM.CODE_SCHEME_WINPATH_KINGS);
				forwardMapDocument.addEntity(entity);
				winpathToRead.put(code,readCode);
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
		ImportUtils.validateFiles(inFolder,kingsWinPath);
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
