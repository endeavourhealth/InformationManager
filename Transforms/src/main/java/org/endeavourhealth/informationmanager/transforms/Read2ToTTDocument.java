package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public TTDocument importRead2(String inFolder) throws IOException {
        validateFiles(inFolder);

        TTDocument document = new TTDocument(IM.GRAPH_READ2);
        document.addPrefix(SNOMED.NAMESPACE, SNOMED.PREFIX);
        document.addPrefix("http://endhealth.info/READ2#","r2");

        importTerms(inFolder);
        importConcepts(inFolder,document);
        createHierarchy();
        importMapsAlt(inFolder);
        importMaps(inFolder);

        return document;
    }

    private void importTerms(String folder) throws IOException {

        Path file = findFileForId(folder, synonyms);

        try( BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){
            String line = reader.readLine();
            line = reader.readLine();

            int count=0;
            while(line!=null && !line.isEmpty()){
                count++;
                if(count%10000 == 0){
                    System.out.println("Processed " + count +" records");
                }
                String[] fields= line.split(",");
                if("C".equals(fields[1])) {
                    int i=2;
                    String name =fields[i];
                    if(name.startsWith(("\""))){
                        i++;
                        while (!name.endsWith("\"")){
                            name+= "," + fields[i++];
                        }
                        name= name.substring(1,name.length()-1);
                    }
                    String description = fields[i];
                    if(description.startsWith("\"")){
                        i++;
                        while(!description.endsWith("\"")){
                            description +=","+ fields[i++];
                        }
                        description= description.substring(1,description.length()-1);
                    }
                    Read2Term t = new Read2Term()
                        .setName(name)
                        .setDescription(description);
                    termMap.put(fields[0],t);
                }
                line = reader.readLine();
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

                    TTConcept c = conceptMap.get(fields[0]);
                    if (c == null) {
                        c = new TTConcept()
                            .setCode(fields[0])
                            .setIri("r2:" + fields[0])
                            .setScheme(IM.CODE_SCHEME_READ);
                        conceptMap.put(fields[0], c);
                        document.addConcept(c);
                    }

                    Read2Term t = termMap.get(fields[1]);
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
            System.out.println("Process ended with " + conceptMap.size() + " concepts");
        }
    }

    private void createHierarchy() {

        for( Map.Entry<String,TTConcept> entry: conceptMap.entrySet()){

            if(!getParent(entry.getKey()).isEmpty() && conceptMap.containsKey(getParent(entry.getKey()))){

                entry.getValue().set(IM.IS_CHILD_OF, iri("r2:" + getParent(entry.getKey())));

            }
        }
    }

    private void importMapsAlt(String folder) throws IOException {
        Path file = findFileForId(folder,altmaps);

        try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

            String line = reader.readLine();
            line = reader.readLine();

            while (line!=null && !line.isEmpty()){

                String[] fields= line.split("\t");

                if("00".equals(fields[1]) && "Y".equals(fields[4])){

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

                if("00".equals(fields[2]) && "1".equals(fields[7])){

                    TTConcept c = conceptMap.get(fields[1]);

                    if (c!=null &&  !altMapped.contains(c.getIri())) {
                        if (c.get(IM.MAPPED_FROM)!=null)
                            c.get(IM.MAPPED_FROM).asArray().add(iri("sn:" + fields[3]));
                        else
                            c.set(IM.MAPPED_FROM, new TTArray().add(iri("sn:"+fields[3])));
                    }
                }
                line = reader.readLine();
            }
        }
    }

    public String getParent(String code) {

        int index = code.indexOf(".");
        if (index == 0) {
            return "";
        } else if (index == -1) {
            return code.substring(0,code.length()-1) + ".";
        } else {
            return code.substring(0, index - 1) + "." + code.substring(index);
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
