package org.endeavourhealth.informationmanager.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Read2 {
    private static final String concepts = ".*\\\\V2\\\\Unified\\\\Corev2.all";
    private static final String synonyms = ".*\\\\V2\\\\Unified\\\\Keyv2.all";
    private static final String maps = ".*\\\\nhs_datamigration_.*\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\rcsctmap2_uk_.*\\.txt";
    private static final String altmaps = ".*\\\\nhs_datamigration_.*\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\codesWithValues_AlternateMaps_READ2_.*\\.txt";

    private static final Map<String, SnomedMeta> idMap = new HashMap<>();
    private static final Set<String> clinicalPharmacyRefsetIds = new HashSet<>();

    private static final String FULLY_SPECIFIED = "900000000000003001";
    private static final String CLINICAL_REFSET = "999001261000000100";
    private static final String PHARMACY_REFSET = "999000691000001104";
    private static final String NECESSARY_INSUFFICIENT = "900000000000074008";
    private static final String IS_A = "116680003";
    private static final String SNOMED_ROOT = "138875005";
    private static final String HIERARCHY_POSITION = ":CM_ValueTerminology";
    private static final String CODE_SCHEME = ":CM_Snomed-CT";
    private static final String IRI_PREFIX = ":SN_";
    private static final String ROLE_GROUP = ":SN_609096000";

    private static final String ACTIVE = "1";

    public static void main(String[] argv) throws IOException {
        if (argv.length != 2) {
            System.err.println("You need to provide the path to the SNOMED data files and the output folder!");
            System.exit(-1);
        }

        validateFiles(argv[0]);

/*        Ontology ontology = new Ontology();
        ontology.addNamespace(new Namespace()
            .setIri("http://www.DiscoveryDataService.org/InformationModel#")
            .setPrefix(":")
        );

        ontology.setDocumentInfo(
            new DocumentInfo().setDocumentIri("http://www.DiscoveryDataService.org/InformationModel")
        );

        importConceptFiles(argv[0], ontology);
        importRefsetFiles(argv[0]);
        importDescriptionFiles(argv[0], ontology);
        importRelationshipFiles(argv[0]);

        Document document = new Document()
            .setInformationModel(ontology);

        outputJson(document, argv[1]);*/
    }

    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, synonyms, maps, altmaps).flatMap(Stream::of)
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

    /*
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
                        c.setIri(IRI_PREFIX + fields[0]);
                        c.setCode(fields[0]);
                        c.setScheme(CODE_SCHEME);
                        c.setStatus(ACTIVE.equals(fields[2]) ? ConceptStatus.ACTIVE : ConceptStatus.INACTIVE);

                        if (SNOMED_ROOT.equals(fields[0]))
                            c.addSubClassOf(new ClassExpression().setClazz(HIERARCHY_POSITION));

                        snomed.addClazz(c);

                        idMap.put(fields[0], new SnomedMeta()
                            .setConcept(c)
                            .setModuleId(fields[3])
                            .setSubclass(NECESSARY_INSUFFICIENT.equals(fields[4]))
                        );
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
    private static void importDescriptionFiles(String path, Ontology snomed) throws IOException {
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

                        Concept c = m.getConcept();
                        if (fields[7].endsWith("(attribute)") && (c instanceof Clazz)) {
                            // Switch to ObjectProperty
                            ObjectProperty op = new ObjectProperty();
                            op.setIri(c.getIri())
                                .setCode(c.getCode())
                                .setScheme(c.getScheme())
                                .setStatus(c.getStatus());
                            snomed.addObjectProperty(op);
                            snomed.deleteClazz((Clazz)c);
                            c = op;
                            m.setConcept(c);
                        }

                        c.setName(fields[7]);
                        c.setDescription(fields[7]);
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
                        if (m.getConcept() instanceof ObjectProperty) {
                            ((ObjectProperty)m.getConcept()).addSubObjectPropertyOf(IRI_PREFIX + fields[5]);
                        } else {
                            String group = fields[6];

                            ClassExpression cex;
                            if (IS_A.equals(fields[7])) {
                                cex = new ClassExpression().setClazz(IRI_PREFIX + fields[5]);
                                cpo.add(new CPO(IRI_PREFIX + fields[4], IRI_PREFIX + IS_A, IRI_PREFIX + fields[5]));
                            } else {
                                OPERestriction osr = new OPERestriction();
                                osr.setProperty(IRI_PREFIX + fields[7])
                                    .setClazz(IRI_PREFIX + fields[5]);

                                cex = new ClassExpression().setObjectSome(osr);
                            }

                            List<ClassExpression> expressions;
                            Clazz c = (Clazz)m.getConcept();
                            if (m.isSubclass()) {
                                expressions = c.getSubClassOf();
                                if (expressions == null)
                                    c.setSubClassOf(expressions = new ArrayList<>());
                            } else {
                                expressions = c.getEquivalentTo();
                                if (expressions == null)
                                    c.setEquivalentTo(expressions = new ArrayList<>());
                            }

                            if (!"0".equals(group)) {
                                ClassExpression rg = m.getRoleGroups().get(group);
                                if (rg == null) {
                                    OPERestriction os = new OPERestriction().setProperty(ROLE_GROUP);
                                    os.setIntersection(new ArrayList<>());
                                    expressions.add(new ClassExpression().setObjectSome(os));
                                    m.getRoleGroups().put(group, rg = os);
                                }
                                expressions = rg.getIntersection();
                            }

                            expressions.add(cex);
                        }
                        i++;
                    }

                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " relationships");
    }

    private static void outputJson(Document document, String outFolder) throws IOException {
        System.out.println("Generating JSON");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFolder + "json\\Snomed.json"))) {
            writer.write(json);
        }

        System.out.println("Generating CPO meta");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFolder + "cpo\\Snomed-meta.txt"))) {
            for (CPO c : cpo)
                writer.write(c.getConcept() + "\t" + c.getProperty() + "\t" + c.getObject() + "\n");
        }
    }
*/
}
