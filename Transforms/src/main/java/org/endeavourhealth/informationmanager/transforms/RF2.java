package org.endeavourhealth.informationmanager.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.model.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Static properties holding the Snomed RF2 fixed values such as the file name patterns
 * <p>Contains the static variables for particular Snomed IDs of significance e.g. Role Group</p>
 */
public class RF2 {

    protected static final String UUIDMapFile = "\\IMUUIDMap.txt";
    protected static final String snomedDocument= "\\Snomed.json";
    protected static final String metaFile= "\\Snomed-meta.txt";
    protected static final String MRCMDocument= "\\MRCMOntology.json";
    protected static Map<String, SnomedMeta> idMap = new HashMap<>();
    protected static  Map<String,String> uuidMap = new HashMap<>();
    protected static final String[] concepts = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_INT_.*\\.txt",
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_.*\\.txt",
            ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_.*\\.txt",
    };

    protected static final String[] refsets = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_INT_.*\\.txt",
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_.*\\.txt",
            ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_.*\\.txt",
    };

    protected static final String[] descriptions = {

            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_INT_.*\\.txt",
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_.*\\.txt",
            ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_.*\\.txt",
    };

    protected static final String[] relationships = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_StatedRelationship_Snapshot_INT_.*\\.txt",
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_StatedRelationship_Snapshot_.*\\.txt",
            ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_StatedRelationship_Snapshot_.*\\.txt",
    };

    protected static final String[] substitutions = {
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Resources\\\\HistorySubstitutionTable\\\\xres2_HistorySubstitutionTable_.*\\.txt",
    };

    protected static final String[] attributeRanges = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Metadata\\\\der2_ssccRefset_MRCMAttributeRangeSnapshot_INT_.*\\.txt",
    };

    protected static final String[] attributeDomains = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Metadata\\\\der2_cissccRefset_MRCMAttributeDomainSnapshot_INT_.*\\.txt",
    };



    protected static final String FULLY_SPECIFIED = "900000000000003001";
    protected static final String CLINICAL_REFSET = "999001261000000100";
    protected static final String PHARMACY_REFSET = "999000691000001104";
    protected static final String NECESSARY_INSUFFICIENT = "900000000000074008";
    protected static final String IS_A = "sn:116680003";
    protected static final String SNOMED_ROOT = "sn:138875005";
    protected static final String HIERARCHY_POSITION = ":CM_ValueTerminology";
    protected static final String CODE_SCHEME = ":891101000252101";
    protected static final String IRI_PREFIX = "sn:";
    protected static final String ROLE_GROUP = "sn:609096000";
    protected static final String MEMBER_OF = "sn:394852005";
    protected static final String ALL_CONTENT="723596005";

    protected static final String ACTIVE = "1";

    protected static String getUUIDMap(String context){
        if (uuidMap.get(context)==null)
            uuidMap.put(context, UUID.randomUUID().toString());
        return uuidMap.get(context);
    }

    protected static void importIMUUIDMap(String outputFolder) {
        Integer i=0;
        try {
            File umap = new File(outputFolder + RF2.UUIDMapFile);
            System.out.println("Importing uuid map for names in " + umap.toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(umap))) {
                String line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");
                    uuidMap.put(fields[0], fields[1]);
                    line= reader.readLine();
                    i++;
                }
            }

        } catch (IOException e){

        }
        System.out.println("Imported " + i + " uuid maps");
    }

    protected static void saveIMUUIDMap(String outputFolder) {
        try {
            FileWriter writer = new FileWriter(outputFolder + RF2.UUIDMapFile);
            BufferedWriter fastWriter = new BufferedWriter(writer);

            uuidMap.forEach((k,v)-> {
                try {
                    fastWriter.write(k+"\t"+ v);
                    fastWriter.newLine();
                } catch (IOException e){
                    System.err.println("Unable to write line in uui map");
                }
            });

            fastWriter.flush();
            fastWriter.close();

        } catch(IOException e){
            System.err.println("Unable to write the uuid map");

        }
    }


    public static void clearMaps() {
        idMap.clear();
        uuidMap.clear();
    }
    protected static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, descriptions,
                relationships, refsets,
                substitutions, attributeRanges,attributeDomains ).flatMap(Stream::of)
                .toArray(String[]::new);

        for(String file: files) {
            List<Path> matches = findFilesForId(path, file);
            if (matches.size() != 1) {
                System.err.println("Could not find " + file);
                System.exit(-1);
            } else {
                System.out.println("Found: " + matches.get(0).toString());
            }
        }
    }

    protected static List<Path> findFilesForId(String path, String regex) throws IOException {
        return Files.find(Paths.get(path), 16,
                (file, attr) -> file.toString().matches(regex))
                .collect(Collectors.toList());
    }

    protected static void outputDocument(Document document,String outFolder,String filename) throws IOException {
        System.out.println("Generating JSON");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFolder + filename))) {
            writer.write(json);
        }
    }
    protected static void outputMeta(List<CPO> cpo,String outFolder, String filename) throws IOException {
        if (cpo!=null) {
            System.out.println("Generating CPO meta");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFolder + filename))) {
                for (CPO c : cpo)
                    writer.write(c.getConcept() + "\t" + c.getProperty() + "\t" + c.getObject() + "\n");
            }
        }
    }

}
