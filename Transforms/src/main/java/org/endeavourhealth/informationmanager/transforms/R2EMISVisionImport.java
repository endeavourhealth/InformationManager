package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class R2EMISVisionImport implements TTImport {

    private static final String[] emisConcepts = {".*\\\\EMIS\\\\EMISCodes.csv"};

    private final Set<String> visionCodes= new HashSet<>();
    private Set<String> snomedCodes;
    private final Map<String,TTConcept> codeIdToConcept= new HashMap<>();
    private final Map<String,String> codeIdToSnomed = new HashMap<>();
    private final Map<String,List<String>> parentMap = new HashMap<>();

    private Connection conn;
    private final TTManager manager= new TTManager();
    private TTDocument document;
    private final Map<String,TTConcept> conceptMap = new HashMap<>();



    public R2EMISVisionImport(){}


    /**
     * Imports EMIS , Read and EMIS codes and creates term code map to Snomed or local legacy concepts
     * Requires vision maps to be populated
     * @param inFolder root folder with sub folder of EMIS, READ
     * @throws Exception From document filer
     */


    public TTImport importData(String inFolder) throws Exception {
        conn= ImportUtils.getConnection();
        System.out.println("Retrieving filed snomed codes");
        snomedCodes= ImportUtils.importSnomedCodes(conn);
        document = manager.createDocument(IM.GRAPH_EMIS.getIri());

        System.out.println("importing emis code file");
        importEMISCodes(inFolder);
        setEmisHierarchy();

        System.out.println("importing vision codes");
        importVisionCodes();

        addVisionMaps();
        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        return this;

    }



    private void addVisionMaps() throws SQLException {
        PreparedStatement getMaps = conn.prepareStatement("SELECT * from vision_read2_to_snomed_map");
        System.out.println("Retrieving Vision snomed maps");
        ResultSet rs = getMaps.executeQuery();
        while (rs.next()) {
            String code = rs.getString("read_code");
            String snomed = rs.getString("snomed_concept_id");
            if (isSnomed(snomed)) {
                if (visionCodes.contains(code)) {
                    TTConcept visionConcept = conceptMap.get(code);
                    TTManager.addMatch(visionConcept, iri(SNOMED.NAMESPACE + snomed));
                }
            }
        }
    }

    private void importVisionCodes() throws SQLException {
        PreparedStatement getTerms= conn.prepareStatement("SELECT * from vision_read2_code");
        System.out.println("Retrieving terms from vision read+lookup2");
        ResultSet rs= getTerms.executeQuery();
        int count=0;
        while (rs.next()){
            count++;
            if(count%10000 == 0){
                System.out.println("Processed " + count +" terms");
            }
            String code= rs.getString("read_code");
            String term= rs.getString("read_term");
            int isVision = rs.getInt("is_vision_code");
            if (isVision==1&!visionCodes.contains(code)){
                TTConcept c= new TTConcept()
                    .setIri("vis:"+code)
                    .setName(term)
                    .setCode(code)
                    .setScheme(IM.CODE_SCHEME_VISION);
                c.set(IM.IS_CHILD_OF,iri("im:VisionLocalCodes"));
                visionCodes.add(code);
                conceptMap.put(code,c);
                document.addConcept(c);
            }
        }
        System.out.println("Process ended with " + count +" additional Vision read like codes created");
    }


    private void setEmisHierarchy() {
        for (Map.Entry<String,List<String>> entry:parentMap.entrySet()){
            String childId= entry.getKey();
            TTConcept childConcept= codeIdToConcept.get(childId);
          // if (childConcept.getCode().equals("^ESCT1270257"))
              //  System.out.println("parent is snomed");
            List<String> parents=entry.getValue();
            for (String parentId:parents) {
                if (codeIdToConcept.get(parentId)!=null) {
                    String parentIri = codeIdToConcept.get(parentId).getIri();
                    TTManager.addChildOf(childConcept, iri(parentIri));
                } else {
                    String parentIri= "sn:"+ codeIdToSnomed.get(parentId);
                    TTManager.addMatch(childConcept,iri(parentIri));

                }
            }
        }
    }

    private void addEMISUnlinked(){
        TTConcept c= new TTConcept().setIri("emis:EMISUnlinkedCodes")
            .set(IM.IS_CHILD_OF,new TTArray().add(iri("emis:"+"EMISNHH2")))
            .setName("EMIS unlinked local codes")
            .setCode("^EMISUnlinkedCodes")
            .setScheme(IM.CODE_SCHEME_EMIS);
        document.addConcept(c);
    }
    private void importEMISCodes(String folder) throws IOException {

        Path file = ImportUtils.findFileForId(folder, emisConcepts[0]);
        addEMISUnlinked();  //place holder for unlinked emis codes betlow the emis root code
        try( CSVReader reader = new CSVReader(new FileReader(file.toFile()))){
            reader.readNext();
            int count=0;
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " records looking for new concepts");
                }
                String codeid= fields[0];
                String term= fields[1];
                String emis= fields[2];
                String snomed= fields[3];
                String descid= fields[4];
                String parent= fields[10];

                if (parent.equals(""))
                    parent=null;
                if (descid.equals(""))
                    descid= null;

                String name = (term.length() <= 250)
                        ? term
                        : (term.substring(0, 247) + "...");
                //is it a snomed code in disguise?
                if (isSnomed(snomed)){
                    document.addIndividual(TTManager
                      .getTermCode(SNOMED.NAMESPACE+snomed,name,emis,
                        IM.CODE_SCHEME_EMIS,descid));
                    codeIdToSnomed.put(codeid,snomed);
                } else {
                    TTConcept c = setLegacyConcept(IM.CODE_SCHEME_EMIS, name, emis, descid, parent);
                    codeIdToConcept.put(codeid, c);
                    if (!codeid.equals(descid))
                        c.get(IM.ALTERNATIVE_CODE).asArray()
                            .add(TTLiteral.literal(codeid));
                    if (emis.equals("EMISNHH2"))
                        c.set(IM.IS_CONTAINED_IN, new TTArray()
                            .add(iri(IM.NAMESPACE + "DiscoveryOntology")));
                    document.addConcept(c);
                    conceptMap.put(emis, c);
                    if (parent == null && !emis.equals("EMISNHH2"))
                        c.set(IM.IS_CHILD_OF, new TTArray().add(iri("emis:EMISUnlinkedCodes")));
                    if (parent != null) {
                        parentMap.computeIfAbsent(codeid, k -> new ArrayList<>());
                        parentMap.get(codeid).add(parent);
                    }
                }
            }
            System.out.println("Process ended with " + count + " records");
        }

    }

    private TTConcept setLegacyConcept(TTIriRef codeScheme, String name, String code, String descid, String parent) {
        TTConcept c;
        String prefix="emis:";
        if (codeScheme.equals(IM.CODE_SCHEME_VISION))
            prefix="r2:";
        c = new TTConcept()
            .setName(name)
            .setCode(code)
            .set(IM.ALTERNATIVE_CODE, new TTArray().add(TTLiteral.literal(descid)))
            .setIri(prefix + code)
            .setScheme(codeScheme)
            .addType(IM.LEGACY);
        return c;
    }


    public Boolean isSnomed(String s){
        return snomedCodes.contains(s);
    }


    public String getNameSpace(String s){
        s = s.substring(s.length()-10, s.length()-3);
        return s;
    }

    public String getEmisCode(String code, String term) {

        int index = code.indexOf(".");
        if (index != -1) {
            code = code.substring(0, index);
        }
        if ("00".equals(term.substring(0, 2))) {
            return code;
        } else if(term.startsWith("1")){
            return code + "-" + term.charAt(1);
        }else {
            return code + "-" + term.substring(0,2);
        }
    }



    public R2EMISVisionImport validateVisionTables(Connection conn) throws SQLException {
        PreparedStatement getVision = conn.prepareStatement("Select read_code from vision_read2_code limit 1");
        ResultSet rs= getVision.executeQuery();
        if (!rs.next()) {
            System.err.println("No Vision read look up table (vision_read2_code)");
            System.exit(-1);
        }
        PreparedStatement getVisions = conn.prepareStatement("Select read_code from vision_read2_to_snomed_map limit 1");
        rs= getVisions.executeQuery();
        if (!rs.next()) {
            System.err.println("No Vision Snomed look up table (vision_read2_to_snomed_map)");
            System.exit(-1);
        }
        return this;
    }

    public R2EMISVisionImport validateFiles(String inFolder){
        ImportUtils.validateFiles(inFolder,emisConcepts);
        return this;
    }

    @Override
    public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
        validateVisionTables(conn);
        return this;
    }


    @Override
    public void close() throws Exception {
        if (conn!=null)
            if (!conn.isClosed())
                conn.close();
        visionCodes.clear();
        if (snomedCodes!=null)
            snomedCodes.clear();
        codeIdToConcept.clear();;
        codeIdToSnomed.clear();
        parentMap.clear();

        conceptMap.clear();
        if (document!=null) {
            if (document.getConcepts() != null)
                document.getConcepts().clear();
            if (document.getIndividuals() != null)
                document.getIndividuals().clear();
        }

    }
}
