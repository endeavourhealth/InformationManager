package org.endeavourhealth.informationmanager.transforms;

import org.eclipse.rdf4j.query.algebra.Str;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class CTV3ToTTDocument {

    private static final String concepts = ".*\\\\CTV3\\\\Concept\\.v3";
    private static final String descriptions = ".*\\\\CTV3\\\\Descrip\\.v3";
    private static final String terms = ".*\\\\CTV3\\\\Terms\\.v3";
    private static final String hierarchies = ".*\\\\CTV3\\\\V3hier\\.v3";
    private static final String maps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\ctv3sctmap2_uk_20200401000001.*\\.txt";
    private static final String altmaps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\codesWithValues_AlternateMaps_CTV3_20200401000001.*\\.txt";

    private Map<String,CTV3Term> termMap= new HashMap<>();

    private Map<String, TTConcept> conceptMap = new HashMap<>();

    private Set<String> altMapped = new HashSet<>();

    public TTDocument importCTV3(String inFolder) throws IOException {

        validateFiles(inFolder);

        TTDocument document = new TTManager().createDocument(IM.GRAPH_CTV3.getIri());

        importTerms(inFolder);
        importConcepts(inFolder,document);
        importDescriptions(inFolder);
        importHierarchies(inFolder);
        importMapsAlt(inFolder);
        importMaps(inFolder);

        return document;

    }

    private void importTerms(String folder) throws IOException{
        Path file = findFileForId(folder, terms);

        try( BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();
            int count=0;
            while(line!=null && !line.isEmpty()) {
                count++;
                if(count%10000 == 0){
                    System.out.println("Processed " + count +" records");
                }
                String[] fields= line.split("\\|");
                if("C".equals(fields[1])) {
                    CTV3Term t = new CTV3Term()
                        .setName(fields[2]);

                    if(fields.length==4 && !fields[3].isEmpty()){
                        t.setDescription(fields[3]);
                    }else{
                        t.setDescription(fields[2]);
                    }
                    termMap.put(fields[0],t);
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count +" records");
            System.out.println("Process ended with " + termMap.size() + " terms");
        }
    }

    private void importConcepts(String folder, TTDocument document) throws IOException {
        Path file = findFileForId(folder, concepts);

        try( BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();
            int count = 0;
            while (line != null && !line.isEmpty()) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " records");
                }
                String[] fields = line.split("\\|");
                TTConcept c = conceptMap.get(fields[0]);
                if (c == null) {
                    c = new TTConcept()
                        .setCode(fields[0])
                        .setIri("ctv3:" + fields[0])
                        .setScheme(IM.CODE_SCHEME_CTV3);
                    conceptMap.put(fields[0], c);
                    document.addConcept(c);
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " records");
            System.out.println("Process ended with " + conceptMap.size() + " concepts");
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
                    System.out.println("Processed " + count + " records");
                }
                String[] fields = line.split("\\|");

                TTConcept c = conceptMap.get(fields[0]);
                if(c!=null) {
                    CTV3Term t = termMap.get(fields[1]);
                    if(t!=null) {
                        if ("P".equals(fields[2])) {
                            c
                                .setName(t.getName())
                                .setDescription(t.getDescription());
                        } else {
                            TTNode s = new TTNode();
                            s.set(IM.CODE, literal(fields[1].substring(0, 2)));
                            s.set(RDFS.LABEL, literal(t.getDescription()));
                            if (c.get(IM.SYNONYM)!=null)
                                c.get(IM.SYNONYM).asArray().add(s);
                            else
                                c.set(IM.SYNONYM, new TTArray().add(s));
                        }
                    }
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " records");
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
                    System.out.println("Processed " + count + " records");
                }
                String[] fields = line.split("\\|");

                TTConcept c = conceptMap.get(fields[0]);
                if(c!=null){
                    c.set(IM.IS_CHILD_OF, iri("ctv3:" + fields[1]));
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " records");
        }
    }

    private void importMapsAlt(String folder) throws IOException {
        Path file = findFileForId(folder,altmaps);

        try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

            String line = reader.readLine();
            line = reader.readLine();

            while (line!=null && !line.isEmpty()){

                String[] fields= line.split("\t");

                if("Y".equals(fields[4])){

                    TTConcept c = conceptMap.get(fields[0]);
                    if (c != null) {
                        altMapped.add(c.getIri());
                        if (c.get(IM.MAPPED_FROM)!=null)
                            c.get(IM.MAPPED_FROM).asArray().add(iri("sn:" + fields[2]));
                        else
                            c.set(IM.MAPPED_FROM, new TTArray().add(iri("sn:"+fields[2])));
                    }
                }
                line = reader.readLine();
            }
        }
    }

    private void importMaps(String folder) throws IOException {
        Path file = findFileForId(folder,maps);

        try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

            String line = reader.readLine();
            line = reader.readLine();

            while (line!=null && !line.isEmpty()){

                String[] fields= line.split("\t");

                if("1".equals(fields[6]) && "1".equals(fields[8]) && !"S".equals(fields[3])){

                    TTConcept c = conceptMap.get(fields[1]);

                    if (c!=null && !altMapped.contains(c.getIri())) {
                        if (c.get(IM.MAPPED_FROM)!=null)
                            c.get(IM.MAPPED_FROM).asArray().add(iri("sn:" + fields[4]));
                        else
                            c.set(IM.MAPPED_FROM, new TTArray().add(iri("sn:"+fields[4])));
                    }
                }
                line = reader.readLine();
            }
        }
    }

    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, descriptions, terms, hierarchies, altmaps, maps)
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
