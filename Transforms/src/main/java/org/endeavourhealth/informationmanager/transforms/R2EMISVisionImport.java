package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class R2EMISVisionImport {

    private static final String EMISConcepts = ".*\\\\EMIS\\\\EMISCodes.csv";
    private static final String r2Concepts = ".*\\\\READ\\\\DESC\\.csv";
    private static final String r2Terms = ".*\\\\READ\\\\Term\\.csv";


    private final Set<String> visionCodes= new HashSet<>();
    private final Set<String> snomedCodes= new HashSet<>();
    private final Map<String,TTConcept> codeIdMap= new HashMap<>();
    private final Map<String,List<String>> parentMap = new HashMap<>();
    private final Map<String,String> descIdMap= new HashMap<>();
   // private final HashSet<String> emisNameSpace = new HashSet<>(Arrays.asList("1000006","1000034","1000035","1000171"));
    private Connection conn;
    private final TTManager manager= new TTManager();
    private TTDocument document;
    private final Map<String,TTConcept> conceptMap = new HashMap<>();


    /**
     * Imports EMIS , Read and EMIS codes and creates term code map to Snomed or local legacy concepts
     * Requires vision maps to be populated
     * @param inFolder root folder with sub folder of EMIS, READ
     * @throws Exception From document filer
     */

    public void importR2EMISVision(String inFolder) throws Exception {
        validateFiles(inFolder);
        conn = IMConnection.getConnection();
        validateVisionTables();
        importSnomed();
        document = manager.createDocument(IM.GRAPH_EMIS.getIri());
        // was needed for Vision import. Now uses EMIS
        //importR2Terms(inFolder);
        //Maps core read code to its term as Vision doesnt provide correct terms
        //importR2Concepts(inFolder)


        importEMISCodes(inFolder);
        setEmisHierarchy();

        importVisionCodes();

        addVisionMaps();

        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);

    }

    private void importSnomed() throws SQLException {
        PreparedStatement getSnomed= conn.prepareStatement("SELECT code from concept "
        +"where iri like 'http://snomed.info/sct%'");
        ResultSet rs= getSnomed.executeQuery();
        while (rs.next())
            snomedCodes.add(rs.getString("code"));
        if (snomedCodes.isEmpty()) {
            System.err.println("Snomed must be loaded first");
            System.exit(-1);
        }
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


/*
    private void importR2Terms(String folder) throws IOException {

        Path file = findFileForId(folder, r2Terms);
        System.out.println("Importing R2 terms");


        try( CSVReader reader = new CSVReader(new FileReader(file.toFile()))){
            reader.readNext();
            int count=0;
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                count++;
                if(count%50000 == 0){
                    System.out.println("Processed " + count +" terms");
                }
                if("C".equals(fields[1])) {
                    String termid= fields[0];
                    String term= fields[3];
                    termMap.put(termid,term);
                }
            }
            System.out.println("Process ended with " + count +" terms");
        }
    }
*/
    /*
    private void importR2Concepts(String folder) throws IOException {

        Path file = findFileForId(folder, r2Concepts);
        System.out.println("Importing R2 concepts");

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            reader.readLine();
            String line = reader.readLine();

            int count = 0;
            while (line != null && !line.isEmpty()) {

                String[] fields = line.split(",");
                if ("C".equals(fields[6])) {
                    count++;
                    if (count % 50000 == 0) {
                        System.out.println("Processed " + count + " concepts");
                    }
                    String code= fields[0].replace(".","");
                    if (code.equals(""))
                        code=".....";
                    String term = termMap.get(fields[1]);

                    /* no longer creating read code concepts
                    if (fields[1].startsWith("00")) {
                        String name = term;
                        TTConcept c = conceptMap.get(code);
                        if (c == null) {
                            c = new TTConcept()
                                .setName(name)
                                .setCode(code)
                                .setIri("r2:" + code)
                                .setScheme(IM.CODE_SCHEME_READ)
                                .addType(IM.LEGACY);
                            conceptMap.put(code, c);
                            document.addConcept(c);
                            readCodes.add(code);
                            String parent = getR2Parent(code);
                            if (!parent.equals(""))
                                MapHelper.addChildOf(c, iri("r2:" + parent));
                            else
                                c.set(IM.IS_CONTAINED_IN, new TTArray().add(iri(IM.NAMESPACE + "DiscoveryOntology")));
                        }
                    }



                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " concepts");
        }
    }

*/

    private void setEmisHierarchy() {
        for (Map.Entry<String,List<String>> entry:parentMap.entrySet()){
            String childId= entry.getKey();
            TTConcept childConcept= codeIdMap.get(childId);
            List<String> parents=entry.getValue();
            for (String parentId:parents) {
                if (codeIdMap.get(parentId)!=null) {
                    String parentIri = codeIdMap.get(parentId).getIri();
                    TTManager.addChildOf(childConcept, iri(parentIri));
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

        Path file = findFileForId(folder, EMISConcepts);
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
                if (descid!=null)
                    descIdMap.put(codeid,descid);
                String name = (term.length() <= 250)
                        ? term
                        : (term.substring(0, 247) + "...");
                //is it a snomed code in disguise?
                if (isSnomed(snomed)){
                    document.addIndividual(TTManager.getTermCode(SNOMED.NAMESPACE+snomed,name,emis,IM.CODE_SCHEME_EMIS,descid));
                } else {
                        TTConcept c;
                        c = new TTConcept()
                            .setName(name)
                            .setCode(emis)
                            .set(IM.ALTERNATIVE_CODE, new TTArray().add(TTLiteral.literal(descid)))
                            .setIri("emis:" + emis)
                            .setDescription(term)
                            .setScheme(IM.CODE_SCHEME_EMIS)
                            .addType(IM.LEGACY);
                        codeIdMap.put(codeid, c);
                        if (!codeid.equals(descid))
                            c.get(IM.ALTERNATIVE_CODE).asArray().add(TTLiteral.literal(codeid));
                        if (emis.equals("EMISNHH2")) {
                            c.set(IM.IS_CONTAINED_IN, new TTArray()
                                .add(iri(IM.NAMESPACE + "DiscoveryOntology")));
                        }
                        document.addConcept(c);
                        conceptMap.put(emis, c);
                        if (parent == null && !emis.equals("EMISNHH2")) {
                            c.set(IM.IS_CHILD_OF, new TTArray().add(iri("emis:EMISUnlinkedCodes")));
                        }
                        if (parent != null) {
                            parentMap.computeIfAbsent(codeid, k -> new ArrayList<>());
                            parentMap.get(codeid).add(parent);
                        }
                }
            }
            System.out.println("Process ended with " + count + " records");
        }

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

    private static void validateFiles(String path) {
        String[] files =  Stream.of(EMISConcepts,r2Concepts,r2Terms)
            .toArray(String[]::new);

        for(String file: files) {
            try {
                Path p = findFileForId(path, file);
                System.out.println("Found " + p.toString());
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        }
    }

    private void validateVisionTables() throws SQLException {
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

    }

    private static Path findFileForId(String path, String regex) throws IOException {
        List<Path> paths = Files.find(Paths.get(path), 16,
            (file, attr) -> file.toString().matches(regex))
            .collect(Collectors.toList());

        if (paths.size() == 1)
            return paths.get(0);

        if (paths.isEmpty())
            throw new IOException("No files found in [" + path + "] for expression [" + regex + "]");
        else
            throw new IOException("Multiple files found in [" + path + "] for expression [" + regex + "]");
    }


}
