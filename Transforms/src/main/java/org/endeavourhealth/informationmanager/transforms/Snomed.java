package org.endeavourhealth.informationmanager.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.models.ConceptOrigin;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Snomed {
    private static final String[] concepts = {
        ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_INT_.*\\.txt",
        ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_.*\\.txt",
        ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_.*\\.txt",
    };

    private static final String[] refsets = {
        ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_INT_.*\\.txt",
        ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_.*\\.txt",
        ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_.*\\.txt",
    };

    private static final String[] descriptions = {

        ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_INT_.*\\.txt",
        ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_.*\\.txt",
        ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_.*\\.txt",
    };

    private static final String[] relationships = {
        ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_StatedRelationship_Snapshot_INT_.*\\.txt",
        ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_StatedRelationship_Snapshot_.*\\.txt",
        ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_StatedRelationship_Snapshot_.*\\.txt",
    };

    private static final String[] substitutions = {
        ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Resources\\\\HistorySubstitutionTable\\\\xres2_HistorySubstitutionTable_Concepts_.*\\.txt",
    };

    private static final String[] attributeRanges = {
        ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Metadata\\\\der2_ssccRefset_MRCMAttributeRangeSnapshot_INT_.*\\.txt",
    };

    private static final Map<String, SnomedMeta> idMap = new HashMap<>();
    private static final Set<String> clinicalPharmacyRefsetIds = new HashSet<>();

    private static final String FULLY_SPECIFIED = "900000000000003001";
    private static final String CLINICAL_REFSET = "999001261000000100";
    private static final String PHARMACY_REFSET = "999000691000001104";
    private static final String IS_A = "116680003";

    private static final String ACTIVE = "1";

    public static void main(String[] argv) throws IOException {
        if (argv.length != 1) {
            System.err.println("You need to provide the path to the SNOMED data files!");
            System.exit(-1);
        }

        validateFiles(argv[0]);

        Ontology snomed = new Ontology();
        snomed.addNamespace(new Namespace()
            .setIri("http://www.DiscoveryDataService.org/InformationModel/Snomed-CT#")
            .setPrefix("sn:")
        );

        importConceptFiles(argv[0], snomed);
        importRefsetFiles(argv[0]);
        importDescriptionFiles(argv[0]);
        importRelationshipFiles(argv[0]);

        outputJson(snomed);
    }

    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, descriptions, relationships, refsets, substitutions, attributeRanges ).flatMap(Stream::of)
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
    private static List<Path> findFilesForId(String path, String regex) throws IOException {
        return Files.find(Paths.get(path), 16,
            (file, attr) -> file.toString().matches(regex))
            .collect(Collectors.toList());
    }

    private static void importConceptFiles(String path, Ontology snomed) throws IOException {
        int i = 0;
        for(String conceptFile: concepts) {
            Path file = findFilesForId(path, conceptFile).get(0);
            System.out.println("Processing concepts in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    if (!idMap.containsKey(fields[0])) {
                        Clazz c = new Clazz();
                        c.setIri("sn:" + fields[0]);
                        c.setStatus(ACTIVE.equals(fields[2]) ? ConceptStatus.ACTIVE : ConceptStatus.INACTIVE);
                        c.setOrigin(ConceptOrigin.CORE);

                        snomed.addClazz(c);

                        idMap.put(fields[0], new SnomedMeta().setClazz(c).setModuleId(fields[3]));
                        i++;
                    }

                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " concepts");
    }
    private static void importRefsetFiles(String path) throws IOException {
        int i = 0;
        for(String refsetFile: refsets) {
            Path file = findFilesForId(path, refsetFile).get(0);
            System.out.println("Processing refsets in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    if (!clinicalPharmacyRefsetIds.contains(fields[5])) {
                        if (ACTIVE.equals(fields[2])
                            && (
                                CLINICAL_REFSET.equals(fields[4]) || PHARMACY_REFSET.equals(fields[4])
                        )) {
                            clinicalPharmacyRefsetIds.add(fields[5]);
                            i++;
                        }
                    }

                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " refset");
    }
    private static void importDescriptionFiles(String path) throws IOException {
        int i = 0;
        for(String descriptionFile: descriptions) {
            Path file = findFilesForId(path, descriptionFile).get(0);
            System.out.println("Processing descriptions in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine(); // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    SnomedMeta m = idMap.get(fields[4]);
                    if (FULLY_SPECIFIED.equals(fields[6])
                        && ACTIVE.equals(fields[2])
                        && m != null
                        && m.getModuleId().equals(fields[3])
                        && clinicalPharmacyRefsetIds.contains(fields[0])) {
                        m.getClazz().setName(fields[7]);
                        m.getClazz().setDescription(fields[7]);
                        i++;
                    }

                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " descriptions");
    }
    private static void importRelationshipFiles(String path) throws IOException {
        int i = 0;
        for(String relationshipFile: relationships) {
            Path file = findFilesForId(path, relationshipFile).get(0);
            System.out.println("Processing relationships in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine(); // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    SnomedMeta m = idMap.get(fields[4]);
                    if (m != null && ACTIVE.equals(fields[2])) {
                        if (IS_A.equals(fields[7])) {
                            ClassExpression s = new ClassExpression();
                            s.setClazz("sn:" + fields[5]);
                            m.getClazz().addSubClassOf(s);
                        } else {
                            ClassExpression e;
                            if (m.getClazz().getEquivalentTo() == null) {
                                m.getClazz().addEquivalentTo(e = new ClassExpression());
                            } else {
                                e = m.getClazz().getEquivalentTo().get(0);
                            }

                            OPERestriction osr = new OPERestriction();
                            osr.setProperty(fields[7])
                              .setClazz(fields[5]);

                            ClassExpression os = new ClassExpression().setObjectSome(osr);

                            e.addIntersection(os);
                        }

                        i++;
                    }

                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " relationships");
    }

    private static void outputJson(Ontology snomed) throws IOException {
        System.out.println("Generating JSON");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(snomed);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Snomed.json"))) {
            writer.write(json);
        }
    }
}
