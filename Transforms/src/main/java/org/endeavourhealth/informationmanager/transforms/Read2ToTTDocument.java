package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
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

public class Read2ToTTDocument {
    private static final String concepts = ".*\\\\READ\\\\DESC\\.csv";
    private static final String synonyms = ".*\\\\READ\\\\Term\\.csv";
    private static final String maps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\rcsctmap2_uk_.*\\.txt";
    private static final String altmaps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\codesWithValues_AlternateMaps_READ2_.*\\.txt";

    private Map<String,Read2Term> termMap= new HashMap<>();
    private Map<String,TTConcept> conceptMap = new HashMap<>();
    private Set<String> altMapped = new HashSet<>();
    private TTManager manager= new TTManager();
    private TTDocument document;

    public TTDocument importRead2(String inFolder) throws IOException {
        validateFiles(inFolder);

        TTDocument document = new TTManager().createDocument(IM.GRAPH_READ2.getIri());
        importTerms(inFolder);
        importConcepts(inFolder,document);
        return document;
    }


    private void importTerms(String folder) throws IOException {

        Path file = findFileForId(folder, synonyms);


        try( CSVReader reader = new CSVReader(new FileReader(file.toFile()))){
            reader.readNext();
            int count=0;
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                count++;
                if(count%10000 == 0){
                    System.out.println("Processed " + count +" records");
                }
                if("C".equals(fields[1])) {
                    String termid= fields[0];
                    String term= fields[3];
                    termMap.put(termid,new Read2Term().setName(term));
                 }
            }
            System.out.println("Process ended with " + count +" records");
        }
    }

    private void importConcepts(String folder, TTDocument document) throws IOException {

        Path file = findFileForId(folder, concepts);

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();
            line = reader.readLine();

            int count = 0;
            while (line != null && !line.isEmpty()) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " records");
                }
                String[] fields = line.split(",");
                if ("C".equals(fields[6])) {
                    String code= getTermid(fields[0],fields[1]);
                    String termCode= fields[1];
                    TTConcept c = conceptMap.get(code);
                    if (c == null) {
                        c = new TTConcept()
                            .setCode(code)
                            .setIri("r2:" + code)
                            .setScheme(IM.CODE_SCHEME_READ)
                            .addType(IM.LEGACY);
                        conceptMap.put(code, c);
                        document.addConcept(c);
                    }

                    Read2Term t = termMap.get(fields[1]);
                    c.setName(t.getName()+" (r2 "+ code+")");
                    if ("S".equals(fields[2]))  //Its a term code concept
                        c.set(IM.SIMILAR,iri("r2:"+code));
                    else {
                        String parent = getParent((fields[0]));
                        if (!parent.equals(""))
                            MapHelper.addChildOf(c,iri("r2:" + parent));
                        else
                            c.set(IM.IS_CONTAINED_IN, new TTArray().add(iri(IM.NAMESPACE + "DiscoveryOntology")));
                    }

                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " records");
            System.out.println("Process ended with " + conceptMap.size() + " concepts");
        }
    }

    private String getTermid(String code,String termid){
        String id = termid.substring(0,2);
        if (id.equals("00"))
            id="";
        else {
            id="-"+termid.substring(1,2);
        }
        code= code.replace(".","");
        if (code.equals(""))
            code=".....";
        return (code+id);
    }



    private void importMapsAlt(String folder) throws IOException {
        Path file = findFileForId(folder,altmaps);

        try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

            String line = reader.readLine();
            line = reader.readLine();

            while (line!=null && !line.isEmpty()) {

                String[] fields = line.split("\t");
                if (fields[4].equals("Y")) {
                    String code = getTermid(fields[0],fields[1]);
                    TTConcept c = new TTConcept();
                    document.addConcept(c);
                    addMap(c, fields[2], fields[3],1,"Preferred map");
                }
               line = reader.readLine();

            }
        }
    }

    private void addMap(TTConcept c, String target, String targetTermCode,Integer priority,String advice) {
        MapHelper.addMap(c,iri(IM.NAMESPACE+"NationallyAssuredUK"),"sn:"+target,targetTermCode,null,priority,advice);
    }

    public TTDocument importMaps(String folder) throws IOException {
        document = new TTManager().createDocument(IM.GRAPH_MAP_READ2.getIri());
        importMapsAlt(folder);
        Path file = findFileForId(folder,maps);

        try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

            String line = reader.readLine();
            line = reader.readLine();

            while (line!=null && !line.isEmpty()){

                String[] fields= line.split("\t");
                String code= getTermid(fields[1],fields[2]);

                if("1".equals(fields[7])) {
                    Integer priority;
                    TTConcept c= manager.getConcept("r2:"+code);
                    if (c==null) {
                        c = new TTConcept().setIri("r2: code");
                        document.addConcept(c);
                        priority=1;
                    } else
                        priority=2;
                        addMap(c, fields[3], fields[4],priority,null);
                    }
                }
                line = reader.readLine();
            }
        return document;
    }

    public String getParent(String code) {

        int index = code.indexOf(".");
        if (index == 0) {
            return "";
        }else if (index==1){
            return ".....";
        } else if (index == -1) {
            return code.substring(0,code.length()-1);
        } else {
            return code.substring(0, index - 1);
        }
    }

    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, synonyms, maps, altmaps)
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
