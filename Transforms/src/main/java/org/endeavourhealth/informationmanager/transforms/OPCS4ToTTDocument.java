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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class OPCS4ToTTDocument {

    private static final String concepts = ".*\\\\nhs_opcs4df_9.0.0_.*\\\\OPCS49 CodesAndTitles.*\\.txt";
    private static final String maps = ".*\\\\SNOMED\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Map\\\\der2_iisssciRefset_ExtendedMapSnapshot_GB1000000_.*\\.txt";


    private Map<String,TTConcept> conceptMap = new HashMap<>();

    public TTDocument importOPCS4(String inFolder) throws IOException {
        validateFiles(inFolder);

        TTDocument document = new TTManager().createDocument(IM.GRAPH_OPCS4.getIri());

        importConcepts(inFolder,document);
        importMaps(inFolder);

        return document;
    }

    private void importConcepts(String folder, TTDocument document) throws IOException {

        Path file = findFileForId(folder, concepts);

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();

            int count = 0;
            while (line != null && !line.isEmpty()) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " records");
                }
                String[] fields = line.split("\t");
                TTConcept c = new TTConcept()
                        .setCode(fields[0])
                        .setDescription(fields[1])
                        .setIri("opcs4:" + fields[0])
                        .setScheme(IM.CODE_SCHEME_OPCS4);
                    if(fields[1].length()>250){
                        c.setName(fields[1].substring(0,247)+"...");
                    }else {
                        c.setName(fields[1]);
                    }
                    conceptMap.put(fields[0].replace(".",""), c);
                    document.addConcept(c);
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " records");
            System.out.println("Process ended with " + conceptMap.size() + " concepts");
        }
    }

    private void importMaps(String folder) throws IOException {
        Path file = findFileForId(folder,maps);

        try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

            String line = reader.readLine();
            line = reader.readLine();

            while (line!=null && !line.isEmpty()){

                String[] fields= line.split("\t");

                if("1".equals(fields[2]) && "1126441000000105".equals(fields[4])){

                    TTConcept c = conceptMap.get(fields[10]);
                    if (c!=null) {
                        if (c.get(IM.MAPPED_FROM)!=null)
                            c.get(IM.MAPPED_FROM).asArray().add(iri("sn:" + fields[5]));
                        else
                            c.set(IM.MAPPED_FROM, new TTArray().add(iri("sn:"+fields[5])));
                    }
                }
                line = reader.readLine();
            }
        }
    }

    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, maps )
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
