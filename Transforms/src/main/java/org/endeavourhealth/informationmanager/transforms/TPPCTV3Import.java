package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TPPCTV3Import {

    private static final String concepts = ".*\\\\CTV3\\\\Concept\\.v3";
    private static final String descriptions = ".*\\\\CTV3\\\\Descrip\\.v3";
    private static final String terms = ".*\\\\CTV3\\\\Terms\\.v3";
    private static final String hierarchies = ".*\\\\CTV3\\\\V3hier\\.v3";
    private static final String maps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\ctv3sctmap2_uk_20200401000001.*\\.txt";
    private static final String altmaps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\codesWithValues_AlternateMaps_CTV3_20200401000001.*\\.txt";
    private Set<String> altMapped = new HashSet<>();
    private Map<String,CTV3Term> termMap= new HashMap<>();
    private Map<String, TTConcept> conceptMap = new HashMap<>();
    private Set<String> preferred = new HashSet<>();
    private TTManager manager= new TTManager();
    private TTDocument document;
    private Connection conn;
    public void importCTV3(String inFolder) throws Exception {

        validateFiles(inFolder);
        conn= IMConnection.getConnection();
        document = manager.createDocument(IM.GRAPH_CTV3.getIri());
        importTerms();
        importHierarchies(inFolder);
        importMaps();
        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);

    }



    private void importTerms() throws IOException, SQLException {
        PreparedStatement getTerms= conn.prepareStatement("SELECT * from tpp_ctv3_lookup_2");
        System.out.println("Retrieving terms from tpp_ctv3+lookup2");
        ResultSet rs= getTerms.executeQuery();
        int count=0;
        while (rs.next()){
            count++;
            if(count%10000 == 0){
                System.out.println("Processed " + count +" terms");
            }
            String code= rs.getString("ctv3_code");
            String term= rs.getString("ctv3_term");
            TTConcept c= new TTConcept();
            c.setIri("tpp:"+code);
            c.setName(term);
            c.setCode(code);
            c.setScheme(IM.CODE_SCHEME_TPP);
            c.setStatus(IM.ACTIVE);
            conceptMap.put(code,c);
            document.addConcept(c);
        }
        System.out.println("Process ended with " + count +" concepts created");
    }

    private void importConcepts(String folder, TTDocument document) throws IOException {
        Path file = findFileForId(folder, concepts);

        try( BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();
            int count = 0;
            while (line != null && !line.isEmpty()) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " concepts");
                }
                String[] fields = line.split("\\|");
                String code= fields[0];
                if (!code.startsWith(".")) {
                    TTConcept c = conceptMap.get(fields[0]);
                    if (c == null) {
                        c = new TTConcept()
                            .setCode(fields[0])
                            .setIri("ctv3:" + fields[0])
                            .setScheme(IM.CODE_SCHEME_CTV3)
                            .addType(IM.LEGACY);
                        conceptMap.put(fields[0], c);
                        document.addConcept(c);
                    }
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " concepts");

        }
    }

    private void importDescriptions(String folder) throws IOException {
        Path file= findFileForId(folder, descriptions);

        try( BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();
            int count = 0;
            while (line != null && !line.isEmpty()) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " code term links");
                }
                String[] fields = line.split("\\|");
                String code= fields[0];
                if (!code.startsWith(".")) {
                    if (fields[2].equals("P"))
                        preferred.add(fields[1]);

                    TTConcept c = conceptMap.get(fields[0]);
                    if (c != null) {
                        CTV3Term t = termMap.get(fields[1]);
                        if (t != null) {
                            if ("P".equals(fields[2])) {
                                c
                                    .setName(t.getName());
                                if (t.getDescription() != null)
                                    c.setDescription(t.getDescription());
                            } else {
                                TTNode s = new TTNode();
                                s.set(IM.CODE, literal(fields[1].substring(0, 2)));
                                s.set(RDFS.LABEL, literal(t.getName()));
                                if (c.get(IM.SYNONYM) != null)
                                    c.get(IM.SYNONYM).asArray().add(s);
                                else
                                    c.set(IM.SYNONYM, new TTArray().add(s));
                            }
                        }
                    }
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " code term links");
        }
    }

    private void importHierarchies(String folder) throws IOException {
        Path file = findFileForId(folder, hierarchies);

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();
            int count = 0;
            while (line != null && !line.isEmpty()) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " hierarchy nodes");
                }
                String[] fields = line.split("\\|");

                TTConcept c = conceptMap.get(fields[0]);
                if(c!=null){
                    MapHelper.addChildOf(c,iri("ctv3:" + fields[1]));
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " hierarchy nodes");
        }
    }



    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, descriptions, terms, hierarchies)
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

    private void importMapsAlt(String folder) throws IOException {
        Path file = findFileForId(folder,altmaps);

        try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

            String line = reader.readLine();
            line = reader.readLine();

            while (line!=null && !line.isEmpty()){

                String[] fields= line.split("\t");
                String code= fields[0];

                if("Y".equals(fields[4])){
                    TTConcept c = conceptMap.get(code);
                    MapHelper.addMap(c, iri(IM.NAMESPACE+"NationallyAssuredUK"),"sn:"+fields[2], fields[3],null,1,"Preferred map");

                }
                line = reader.readLine();
            }
        }
    }

    public  void importMaps() throws IOException, SQLException {
        PreparedStatement getMaps = conn.prepareStatement("SELECT * from tpp_ctv3_to_snomed");
        System.out.println("Retrieving TPP maps");
        ResultSet rs = getMaps.executeQuery();
        int count = 0;
        while (rs.next()) {

            String code = rs.getString("ctv3_code");
            String snomed = rs.getString("snomed_concept_id");
            TTConcept c = conceptMap.get(code);
            if (c != null) {
                MapHelper.addMap(c, iri(IM.NAMESPACE + "SupplierAssured"), "sn:" + snomed, null, null, 1, null);
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + "mapped concepts");
                }
            }
        }
    }


}
