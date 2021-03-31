package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
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

public class EMISToTTDocument {

    private static final String readConcepts = ".*\\\\READ\\\\DESC\\.csv";
    private static final String EMISConcepts = ".*\\\\373783-374080_Coding_ClinicalCode_.*\\.csv";


    private HashSet<String> readCodes = new HashSet<>();
    private HashSet<String> emisNameSpace = new HashSet<>(Arrays.asList("1000006","1000034","1000035","1000171"));

    public TTDocument importEMIS(String inFolder) throws IOException {
        validateFiles(inFolder);

        TTDocument document = new TTManager().createDocument(IM.GRAPH_EMIS.getIri());

        importReadConcepts(inFolder);
        importConcepts(inFolder, document);

        return document;
    }

    private void importReadConcepts(String folder) throws IOException {

        Path file = findFileForId(folder, readConcepts);

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
                if(!".....".equals(fields[0])){
                    readCodes.add(getEmisCode(fields[0],fields[1]));
                }

                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " records");
            System.out.println("Process ended with " + readCodes.size() + " concepts");
        }
    }

    private void importConcepts(String folder, TTDocument document) throws IOException {

        Path file = findFileForId(folder, EMISConcepts);

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
                int i = 1;
                String description = fields[i];
                if (description.startsWith("\"")) {
                    i++;
                    while (!description.endsWith("\"")) {
                        description += "," + fields[i++];
                    }
                    description = description.substring(1, description.length() - 1);
                }
                i++;
                if (!readCodes.contains(fields[i])) {
                    String name = (description.length() <=250)
                        ? description
                        : (description.substring(0,247) + "...");
                    TTConcept c = new TTConcept()
                        .setName(name)
                        .setCode(fields[i])
                        .setIri("emis:" + fields[i])
                        .setDescription(description)
                        .setScheme(IM.CODE_SCHEME_EMIS)
                        .addType(IM.LEGACY);
                    if (isSnomed(fields[i+1])) {
                        if (c.get(IM.MAPPED_FROM)!=null)
                            c.get(IM.MAPPED_FROM).asArray().add(iri("sn:" + fields[i+1]));
                        else
                            c.set(IM.MAPPED_FROM, new TTArray().add(iri("sn:"+fields[i+1])));
                    }
                    document.addConcept(c);
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " records");
        }
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
        String[] files =  Stream.of(readConcepts, EMISConcepts)
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
