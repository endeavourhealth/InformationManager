package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.FileNotFoundException;
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
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EMISReadImport {

    private static final String EMISConcepts = ".*\\\\EMIS\\\\EMISCodes.csv";


    private Map<String,List<String>> emisHierarchy = new HashMap<>();
    private Map<String,TTConcept> codeIdMap= new HashMap<>();
    private Map<String,TTConcept> emisMap= new HashMap<>();
    private Map<String,List<String>> parentMap = new HashMap<>();
    private Map<String,String> descIdMap= new HashMap<>();
    private HashSet<String> readCodes = new HashSet<>();
    private Map<String,String> snomedDescIds= new HashMap<>();
    private HashSet<String> emisNameSpace = new HashSet<>(Arrays.asList("1000006","1000034","1000035","1000171"));
    private Connection conn;
    private TTManager manager= new TTManager();
    private TTDocument document;

    public void importEMIS(String inFolder) throws Exception {
        validateFiles(inFolder);

        document = manager.createDocument(IM.GRAPH_EMIS.getIri());

        importConcepts(inFolder, document);
        setEmisHierarchy();
        importMaps(inFolder,document);
        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);

    }


    private void setEmisHierarchy() {
        for (Map.Entry<String,List<String>> entry:parentMap.entrySet()){
            String childId= entry.getKey();
            TTConcept childConcept= codeIdMap.get(childId);
            List<String> parents=entry.getValue();
            for (String parentId:parents) {
                String parentIri = codeIdMap.get(parentId).getIri();
                if (parentIri == null) {
                    childConcept.set(IM.IS_CHILD_OF, new TTArray().add(iri("r2:EMISUnlinkedCodes")));   //emis local code folder
                } else {
                    MapHelper.addChildOf(childConcept, iri(parentIri));
                }
            }
        }
    }

    private void importMaps(String folder, TTDocument document) throws IOException {
        Path file = findFileForId(folder, EMISConcepts);
        try( CSVReader reader = new CSVReader(new FileReader(file.toFile()))) {
            reader.readNext();
            int count = 0;
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " records looking for snomed maps");
                }
                String codeid = fields[0];
                String term = fields[1];
                String emis = fields[2];
                String snomed = fields[3];
                String descid = fields[4];
                String parent = fields[10];
                TTConcept c = emisMap.get(emis);
                if (isSnomed(snomed)) {
                    MapHelper.addMap(c, iri(IM.NAMESPACE + "SupplierAssured"), "sn:" + snomed, descid, null, null, null);
                }
            }
        }

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
                String name = (term.length() <=250)
                        ? term
                        : (term.substring(0,247) + "...");
                TTConcept c = emisMap.get(emis);
                if (c==null) {
                    c = new TTConcept()
                        .setName(name)
                        .setCode(emis)
                        .set(IM.ALTERNATIVE_CODE, new TTArray().add(TTLiteral.literal(descid)))
                        .setIri("emis:" + emis)
                        .setDescription(term)
                        .setScheme(IM.CODE_SCHEME_EMIS)
                        .addType(IM.LEGACY);
                    if (!codeid.equals(descid))
                        c.get(IM.ALTERNATIVE_CODE).asArray().add(TTLiteral.literal(codeid));
                    document.addConcept(c);
                    if (isRead(emis))
                        if (emis.contains("-"))
                            addSimilar(c, emis.split("-")[0]);
                }
                emisMap.put(emis,c);
                codeIdMap.put(codeid,c);
                if (parent!=null) {
                    if (parentMap.get(codeid) == null)
                        parentMap.put(codeid,new ArrayList<>());
                    parentMap.get(codeid).add(parent);
                }

            }
            System.out.println("Process ended with " + count + " records");
        }

    }

    private void addSimilar(TTConcept concept, String similarTo){
        if (concept.get(IM.SIMILAR)==null)
            concept.set(IM.SIMILAR,new TTArray());
        concept.get(IM.SIMILAR).asArray().add(TTIriRef.iri("emis:"+ similarTo));
    }

    public Boolean isSnomed(String s){
        if(s.length()<=10){
            return true;
        }else {
            return !emisNameSpace.contains(getNameSpace(s));
        }
    }

    public boolean isRead(String code){
        if (!code.contains("-")) {
            if (code.length() < 6)
                return true;
            else
                return false;
        }
        String mainCode= code.substring(0,code.indexOf("-"));
        if (mainCode.length()<6){
            if (code.substring(code.indexOf('-')+1).length()>2)
                if (!code.contains("-z"))
                    return false;
                else
                    return true;
            else
                return true;

        } else
            return false;

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
