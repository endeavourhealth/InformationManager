package org.endeavourhealth.informationmanager.transforms;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.DataFormatException;


public class PRSBImport implements TTImport {

	private static final String[] prsbConcepts = {".*\\\\PRSB\\\\RetrieveTransaction.json"};
	private TTDocument document;
	private Map<String, TTArray> axiomMap;

	@Override
	public TTImport importData(String inFolder) throws Exception {
		validateFiles(inFolder);
		TTManager dmanager= new TTManager();
		document= dmanager.createDocument(IM.GRAPH_PRSB.getIri());
		importConceptFiles(inFolder);
		//TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
		dmanager.saveDocument(new File("c:\\temp\\prsb.json"));
		//filer.fileDocument(document);
		return this;
	}

	private void initializeMaps() {
		List<TTArray> axioms = new ArrayList();
		axiomMap= new HashMap<>();
		axiomMap= new HashMap<String,TTArray>()  {
			{
				put("prsb03-dataelement-10868", getAxioms(IM.NAMESPACE+"Person"));

			}
		};
	}

	private TTArray getAxioms(String prsb) {
		return null;
	}

	private void importConceptFiles(String path) throws IOException {
		int i = 0;
		for (String prsbFile : prsbConcepts) {
			Path file = ImportUtils.findFilesForId(path, prsbFile).get(0);
			System.out.println("Processing concepts in " + file.getFileName().toString());
			JSONParser jsonParser = new JSONParser();
			try (FileReader reader = new FileReader(file.toFile()))
			{
				//Read JSON file
				Object obj = jsonParser.parse(reader);

				JSONArray prsbModel = (JSONArray) ((JSONObject) obj).get("dataset");

				//Iterate over prsbModel
				prsbModel.forEach( mod -> {
					try {
						parsePRSBModel( (JSONObject) mod );
					} catch (DataFormatException e) {
						e.printStackTrace();
					}
				});

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Imported " + i + " concepts");
	}

	private void parsePRSBModel(JSONObject dataModel) throws DataFormatException {
		TTConcept dm= newConcept(dataModel,IM.DATA_MODEL);
		dm.addObject(IM.IS_CONTAINED_IN,TTIriRef.iri(IM.NAMESPACE+"DiscoveryOntology"));
		JSONArray recordTypes= (JSONArray) dataModel.entrySet();
		dataModel.entrySet().forEach(c ->{
			try {
				parseRecordType((JSONObject) c);
			} catch (DataFormatException e) {
				e.printStackTrace();
			}
		});
	}

	private void parseRecordType(JSONObject c) throws DataFormatException {
		String prsbId= (String) c.get("iddisplay");
		TTConcept rt= newConcept(c,IM.RECORD,SHACL.NODESHAPE);
		TTArray axioms= axiomMap.get(prsbId);

	}

	private TTIriRef mapStatus(String status) throws DataFormatException {
		if (status.equals("draft"))
			return IM.DRAFT;
			else
				throw new DataFormatException("unknown status type - "+ status);

	}

	private TTConcept newConcept(JSONObject c, TTIriRef... types) throws DataFormatException {
		TTConcept concept= new TTConcept();
		concept.set(IM.STATUS,mapStatus(c.get("statusCode").toString()));
		Arrays.stream(types).forEach( type-> concept.addType(type));
		String prsbId= c.get("iddisplay").toString();
		concept.setCode(prsbId);
		concept.setScheme(IM.CODE_SCHEME_PRSB);
		String name= getObjectArrayliteral(c,"name","#text");
		String iri= (PRSB.NAMESPACE + prsbId);

		concept.setName(name);
		if (c.get("shortName")!=null) {
			String shortName = (String) c.get("shortName");
			concept.set(TTIriRef.iri(IM.NAMESPACE + "shortName"), TTLiteral.literal((shortName)));
		}
		concept.setIri(iri);
		String description= getObjectArrayliteral(c,"desc","#text");
		if (description!=null)
			concept.setDescription(description);
		String background= getObjectArrayliteral(c,"context","#text");
		if (background!=null)
			concept.set(TTIriRef.iri(IM.NAMESPACE+"backgroundContext"),TTLiteral.literal(background));
		if (concept.isType(IM.DATA_MODEL))
			return concept;
		return concept;

	}

	private String getObjectArrayliteral(JSONObject ob,String name,String subname){
		JSONArray arr= (JSONArray) ob.get(name);
		JSONObject subob = (JSONObject) arr.get(0);
		if (subob.get(subname)!=null)
			return (String) subob.get(subname);
		else
			return null;
	}

	@Override
	public TTImport validateFiles(String inFolder) {
		ImportUtils.validateFiles(inFolder,prsbConcepts);
		return this;
	}

	@Override
	public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
		return this;
	}

	@Override
	public void close() throws Exception {

	}
}
