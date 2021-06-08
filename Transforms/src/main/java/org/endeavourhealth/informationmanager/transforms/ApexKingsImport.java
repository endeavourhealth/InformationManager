package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.APK;
import org.endeavourhealth.imapi.vocabulary.IM;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApexKingsImport implements TTImport {


	private static final String[] kingsPath = {".*\\\\Kings\\\\KingsPathMap.txt"};
	private TTManager manager;
	private TTManager backManager;
	private TTDocument forwardMapDocument;
	private TTDocument backMapDocument;
	private Connection conn;
	private Map<String, List<Snomed>> r2ToSnomed = new HashMap<>();
	private Map<TTConcept, List<Snomed>> apexToSnomed = new HashMap();
	private Map<String, String> apexToRead = new HashMap<>();
	private Map<String, List<TTConcept>> snomedToApex = new HashMap<>();


	private class Snomed {
		private String conceptId;
		private String descId;
		private String name;
		private String synonym;
	}

	private class ReadMatch {
		private String readCode;
		private List<Snomed> snomedList;

		private ReadMatch addSnomed(Snomed snomed) {
			if (snomedList == null)
				snomedList = new ArrayList<>();
			snomedList.add(snomed);
			return this;
		}
	}


	@Override
	public TTImport importData(String inFolder) throws Exception {
		manager = new TTManager();
		backManager = new TTManager();
		forwardMapDocument = manager.createDocument(IM.GRAPH_APEX_KINGS.getIri());
		backMapDocument = backManager.createDocument(IM.GRAPH_SNOMED.getIri());
		backMapDocument.setCrudOperation(IM.ADD_OBJECTS);
		importR2Matches();
		importApexKings(inFolder);
		createManyToManyMaps();
		createBackMaps();
		createForwardMaps();
		manager.saveDocument(new File("c:/temp/forwardmaps.json"));
		backManager.saveDocument(new File("c:/temp/backmaps.json"));
		TTDocumentFiler filer = new TTDocumentFiler(forwardMapDocument.getGraph());
		filer.fileDocument(forwardMapDocument);
		filer = new TTDocumentFiler(backMapDocument.getGraph());
		filer.fileDocument(backMapDocument);
		return this;
	}

	private void createForwardMaps() {
		for (Map.Entry<TTConcept, List<Snomed>> entry : apexToSnomed.entrySet()) {
			TTConcept apexConcept = entry.getKey();
			forwardMapDocument.addConcept(apexConcept);
			List<Snomed> maps = entry.getValue();
			Snomed snomed = getPreferred(maps);
			TTNode target = new TTNode();
			target.set(IM.MATCHED_TO, TTIriRef.iri(SNOMED.NAMESPACE + snomed.conceptId));
			target.set(IM.HAS_TERM_CODE, TTLiteral.literal(snomed.descId));
			apexConcept.set(IM.HAS_MAP, new TTArray());
			apexConcept.get(IM.HAS_MAP).asArray().add(target);
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
		for (TTConcept apexConcept : forwardMapDocument.getConcepts()) {
			String apexCode = apexConcept.getCode();
			String read = apexToRead.get(apexCode);
			List<Snomed> maps = r2ToSnomed.get(read);
			if (maps != null) {
				for (Snomed snomed : maps) {
					List<TTConcept> apexList = snomedToApex.get(snomed.conceptId);
					if (apexList == null) {
						apexList = new ArrayList<>();
						snomedToApex.put(snomed.conceptId, apexList);
					}
					apexList.add(apexConcept);
					List<Snomed> snomedList = apexToSnomed.get(apexConcept);
					if (snomedList == null) {
						snomedList = new ArrayList<>();
						apexToSnomed.put(apexConcept, snomedList);
					}
					snomedList.add(snomed);
				}
			}
		}
	}

	private void createBackMaps() {
		for (Map.Entry<String, List<TTConcept>> entry : snomedToApex.entrySet()) {
			String conceptId = entry.getKey();
			TTConcept snomedConcept = new TTConcept()
				.setIri(SNOMED.NAMESPACE + conceptId);
			backMapDocument.addConcept(snomedConcept);
			List<TTConcept> apexList = entry.getValue();
			for (TTConcept apex : apexList) {
				if (snomedConcept.get(IM.HAS_MAP) == null) {
					TTNode map = new TTNode();
					map.set(IM.ANY_OF,new TTArray());
					snomedConcept.set(IM.HAS_MAP, new TTArray());
					snomedConcept.get(IM.HAS_MAP).asArray().add(map);
				}
				TTNode map= snomedConcept.get(IM.HAS_MAP).asArray().getElements().get(0).asNode();
				map.addObject(IM.ANY_OF,TTIriRef.iri(apex.getIri()));
			}
		}
	}


	private void importR2Matches() throws SQLException, ClassNotFoundException {
		System.out.println("Retrieving read 2 snomed map");
		conn= ImportUtils.getConnection();
		PreparedStatement getR2Matches= conn.prepareStatement("select tc.code as code,c.code as conceptId,"+
				"synonym.code as termCode,synonym.term as synonym,c.name as conceptName\n" +
			"from term_code tc\n" +
			"join concept s on tc.scheme=s.dbid\n" +
			"join concept c on tc.concept=c.dbid\n" +
			"left join concept cscheme on c.scheme= cscheme.iri\n" +
			"left join term_code synonym on tc.concept_term_code= synonym.code and synonym.scheme=cscheme.dbid\n" +
			"where s.iri='"+ IM.CODE_SCHEME_READ.getIri()+"'");
		ResultSet rs= getR2Matches.executeQuery();
		while (rs.next()){
			Snomed snomed= new Snomed();
			String read= rs.getString("code");
			snomed.conceptId= rs.getString("conceptId");
			snomed.descId= rs.getString("termCode");
			snomed.name= rs.getString("conceptName");
			snomed.synonym= rs.getString("synonym");
			List<Snomed> maps= r2ToSnomed.get(read);
			if (maps==null){
				maps= new ArrayList<>();
				r2ToSnomed.put(read,maps);
			}
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
				String code= fields[1]+"/"+(fields[2].toLowerCase());
				String iri = APK.NAMESPACE+ fields[1]+ "/"+(fields[2].replace(" ",""));
				TTConcept concept= new TTConcept()
					.setIri(iri)
					.addType(IM.LEGACY)
					.setName(fields[2])
					.setDescription("Local apex Kings trust pathology system concept ")
					.setCode(code)
					.setScheme(IM.CODE_SCHEME_APEX_KINGS);
				forwardMapDocument.addConcept(concept);
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
