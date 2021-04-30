package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
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
import java.util.zip.DataFormatException;


public class ICD10Import {

    private static final String concepts = ".*\\\\icd_df_10.5.0_20151102000001\\\\ICD10_Edition5_.*\\\\Content\\\\ICD10_Edition5_CodesAndTitlesAndMetadata_GB_.*\\.txt";
    private static final String maps = ".*\\\\SNOMED\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Map\\\\der2_iisssciRefset_ExtendedMapSnapshot_GB1000000_.*\\.txt";



    private final Map<String,TTConcept> conceptMap = new HashMap<>();
    private final TTManager manager= new TTManager();

    public void importICD10(String inFolder) throws Exception {
        validateFiles(inFolder);

        TTDocument document = manager.createDocument(IM.GRAPH_ICD10.getIri());

        importConcepts(inFolder, document);
        importMaps(inFolder);
        TTDocumentFiler filer= new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
    }

    public void importMaps(String folder) throws IOException, DataFormatException {
        TTDocument document = manager.createDocument(IM.GRAPH_MAP_SNOMED_ICD10.getIri());
        validateFiles(folder);
        Path file = findFileForId(folder,maps);
        ComplexMapImport mapImport= new ComplexMapImport();
        mapImport.importMap(file.toFile(),document,"999002271000");
    }

    private void importConcepts(String folder, TTDocument document) throws IOException {

        Path file = findFileForId(folder, concepts);

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            reader.readLine();
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
                        .setDescription(fields[4])
                        .setIri("icd10:" + fields[1])
                        .setScheme(IM.CODE_SCHEME_ICD10)
                        .addType(IM.LEGACY);
                    if(fields[4].length()>250){
                        c.setName(fields[4].substring(0,247)+"...");
                    }else {
                        c.setName(fields[4]);
                    }
                    conceptMap.put(fields[1], c);
                    document.addConcept(c);
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " records");
            System.out.println("Process ended with " + conceptMap.size() + " concepts");
        }
    }



    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts)
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
