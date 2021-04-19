package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

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

public class EMISToTTDocument {

    private static final String EMISConcepts = ".*\\\\EMIS\\\\EMISCodes.csv";


    private Map<String,List<String>> emisHierarchy = new HashMap<>();
    private Map<String,String> codeIdMap= new HashMap<>();
    private Map<String,String> descIdMap= new HashMap<>();
    private HashSet<String> readCodes = new HashSet<>();
    private Map<String,String> snomedDescIds= new HashMap<>();
    private HashSet<String> emisNameSpace = new HashSet<>(Arrays.asList("1000006","1000034","1000035","1000171"));
    private Connection conn;
    private TTManager manager= new TTManager();
    private TTDocument document;
    public TTDocument importEMIS(String inFolder) throws IOException, SQLException, ClassNotFoundException {
        validateFiles(inFolder);

        document = manager.createDocument(IM.GRAPH_EMIS.getIri());

        loadRead();//assumes read is already loaded
        importConcepts(inFolder, document);
        setEmisHierarchy();
        return document;
    }


    private void setEmisHierarchy() {
        Set<Map.Entry<String,List<String>>> entries= emisHierarchy.entrySet();
        for (Map.Entry<String,List<String>> entry:entries){
            String child= entry.getKey();
            List<String> parents=entry.getValue();
            TTConcept childConcept= manager.getConcept(child);
            for (String parentId:parents) {
                String parentIri = codeIdMap.get(parentId);
                if (parentIri == null) {
                    childConcept.set(IM.IS_CONTAINED_IN, iri("im:891031000252107"));   //emis local code folder
                } else {
                    if (parentIri.startsWith("sn:")) {
                        MapHelper.addMap(childConcept,
                            iri(IM.NAMESPACE + "SupplierAssured"),
                            parentIri, descIdMap.get(parentId), IM.MATCHED_AS_SUBCLASS,null,null);
                        childConcept.set(IM.IS_CONTAINED_IN, iri("im:891031000252107"));   //emis local code folder
                    } else if (parentIri.startsWith("emis:")) {
                        MapHelper.addChildOf(childConcept, iri(parentIri));
                        //System.out.println(childConcept.getIri() + " | " + childConcept.getName() + " | " + "->is child of " + manager.getConcept(parentIri).getIri() + " | " + manager.getConcept(parentIri).getName() + " | ");
                    }
                }
            }
        }
    }

    private void loadRead() throws SQLException, ClassNotFoundException {
        System.out.println("Getting read codes and Snomed from database");
        conn= IMConnection.getConnection();
        PreparedStatement getRead= conn.prepareStatement("SELECT code from concept where "
        +"scheme='"+ IM.CODE_SCHEME_READ.getIri()+"'");
        ResultSet codes= getRead.executeQuery();
        while (codes.next()){
            readCodes.add(codes.getString("code"));
        }
        if (readCodes.isEmpty())
            throw new SQLException("Read codes not filed yet");

    }

    private void importConcepts(String folder, TTDocument document) throws IOException {

        Path file = findFileForId(folder, EMISConcepts);
        try( CSVReader reader = new CSVReader(new FileReader(file.toFile()))){
            reader.readNext();
            int count=0;
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " records");
                }
                String codeid= fields[0];
                String term= fields[1];
                String emis= fields[2];
                String snomed= fields[3];
                String descid= fields[4];
                String parent= fields[10];

                if (descid.equals(""))
                    descid= null;
                if (descid!=null)
                    descIdMap.put(codeid,descid);

                if (!readCodes.contains(emis)) {
                    String name = (term.length() <=250)
                        ? term
                        : (term.substring(0,247) + "...");
                    TTConcept c = new TTConcept()
                        .setName(name)
                        .setCode(emis)
                        .set(IM.ALTERNATIVE_CODE,new TTArray().add(TTLiteral.literal(descid)))
                        .setIri("emis:" + emis)
                        .setDescription(term)
                        .setScheme(IM.CODE_SCHEME_EMIS)
                        .addType(IM.LEGACY);
                    codeIdMap.put(codeid,c.getIri());
                    if (emis.contains("-")){
                        String read= emis.substring(0,emis.indexOf("-"));
                        if (readCodes.contains(read)) {
                            c.set(IM.SIMILAR, iri("r2:" + read));
                        }
                    }
                    if (!isSnomed(snomed)) {
                        List<String> parentIds= emisHierarchy.get(emis);
                        if (parentIds==null){
                            parentIds= new ArrayList<>();
                        }
                        parentIds.add(parent);
                        emisHierarchy.put(c.getIri(),parentIds);
                    }
                    document.addConcept(c);
                }
            }
            System.out.println("Process ended with " + count + " records");
        }


    }

    public TTDocument importMaps(String folder) throws IOException, SQLException, ClassNotFoundException {
        loadRead();
        Path file = findFileForId(folder, EMISConcepts);
        document = manager.createDocument(IM.GRAPH_MAP_EMIS.getIri());
        try( CSVReader reader = new CSVReader(new FileReader(file.toFile()))){
            reader.readNext();
            int count=0;
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " records");
                }
                String codeid= fields[0];
                String term= fields[1];
                String emis= fields[2];
                String snomed= fields[3];
                String descid= fields[4];
                String parent= fields[10];

                if (descid.equals(""))
                    descid= null;
                if (descid!=null)
                    descIdMap.put(codeid,descid);
                TTConcept c= new TTConcept();

                if (!readCodes.contains(emis)) {
                    c.setIri("emis:"+emis);
                    if (isSnomed(snomed)) {
                        MapHelper.addMap(c, iri(IM.NAMESPACE + "SupplierAssured"), "sn:" + snomed,
                            descid, null,null,null);
                        document.addConcept(c);
                    }
                }

            }
            System.out.println("Process ended with " + count + " records");
        }
        return document;

    }

    public Boolean isSnomed(String s){
        if(s.length()<=10){
            return true;
        }else {
            return !emisNameSpace.contains(getNameSpace(s));
        }
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

    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(EMISConcepts)
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


}
