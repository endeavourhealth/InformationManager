package org.endeavourhealth.informationmanager.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.utils.Obj;
import org.apache.commons.collections.map.MultiValueMap;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.Common;
import org.endeavourhealth.informationmanager.common.transform.DOWLManager;
import org.endeavourhealth.informationmanager.common.transform.Entailment;
import org.endeavourhealth.informationmanager.common.transform.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Snomed {

   
    private static final Set<String> clinicalPharmacyRefsetIds = new HashSet<>();
    private static final List<CPO> cpo = new ArrayList<>();

    protected static final String IRI_PREFIX = "sn:";
    protected static final String CODE_SCHEME = ":891101000252101";
    
    public static void main(String[] argv) throws IOException {
        if (argv.length != 2) {
            System.err.println("You need to provide the path to the SNOMED data files and the output folder!");
            System.exit(-1);
        }

        RF2.validateFiles(argv[0]);
        DOWLManager dmanager = new DOWLManager();
        Ontology ontology = dmanager.createOntology(
                "http://www.DiscoveryDataService.org/InformationModel/Snomed");


        ontology.setDocumentInfo(
            new DocumentInfo().setDocumentIri("http://www.DiscoveryDataService.org/InformationModel")
        );
        RF2.importIMUUIDMap(argv[1]);
        importConceptFiles(argv[0], ontology);
        importRefsetFiles(argv[0]);
        importDescriptionFiles(argv[0], ontology);
        importRelationshipFiles(argv[0]);
        Document document = new Document();
        document.setInformationModel(ontology);
        RF2.saveIMUUIDMap(argv[1]);
        RF2.clearMaps();
        RF2.outputDocument(document,argv[1],RF2.snomedDocument);
    }

   

    private static void importConceptFiles(String path, Ontology snomed) throws IOException {
        int i = 0;
        for(String conceptFile: RF2.concepts) {
            Path file = RF2.findFilesForId(path, conceptFile).get(0);
            System.out.println("Processing concepts in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    if (!RF2.idMap.containsKey(fields[0])) {
                        Clazz c = new Clazz();
                        String id = RF2.getUUIDMap("SnomedCT/Concept/"+ fields[0]);
                        c.setId(id);
                        c.setIri(IRI_PREFIX + fields[0]);
                        c.setCode(fields[0]);
                        c.setScheme(CODE_SCHEME);
                        c.setStatus(RF2.ACTIVE.equals(fields[2]) ? ConceptStatus.ACTIVE : ConceptStatus.INACTIVE);

                        if (RF2.SNOMED_ROOT.equals(fields[0]))
                            c.addSubClassOf((ClassAxiom)new ClassAxiom().setClazz(RF2.HIERARCHY_POSITION));

                        snomed.addClazz(c);

                        RF2.idMap.put(fields[0], new SnomedMeta()
                            .setConcept(c)
                            .setModuleId(fields[3])
                            .setSubclass(RF2.NECESSARY_INSUFFICIENT.equals(fields[4]))
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
        for(String refsetFile: RF2.refsets) {
            Path file = RF2.findFilesForId(path, refsetFile).get(0);
            System.out.println("Processing refsets in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    if (!clinicalPharmacyRefsetIds.contains(fields[5])) {
                        if (RF2.ACTIVE.equals(fields[2])
                            && (
                                RF2.CLINICAL_REFSET.equals(fields[4]) || RF2.PHARMACY_REFSET.equals(fields[4])
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
        for(String descriptionFile: RF2.descriptions) {
            Path file = RF2.findFilesForId(path, descriptionFile).get(0);
            System.out.println("Processing descriptions in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine(); // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    SnomedMeta m = RF2.idMap.get(fields[4]);
                    if (RF2.FULLY_SPECIFIED.equals(fields[6])
                        && RF2.ACTIVE.equals(fields[2])
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
        for(String relationshipFile: RF2.relationships) {
            Path file = RF2.findFilesForId(path, relationshipFile).get(0);
            System.out.println("Processing relationships in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine(); // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    SnomedMeta m = RF2.idMap.get(fields[4]);
                    if (m != null && RF2.ACTIVE.equals(fields[2])) {
                        if (m.getConcept() instanceof ObjectProperty) {
                            ((ObjectProperty)m.getConcept()).addSubObjectPropertyOf(
                                new PropertyAxiom().setProperty(IRI_PREFIX + fields[5])
                            );
                        } else {
                            String group = fields[6];

                            ClassExpression cex;
                            if (RF2.IS_A.equals(fields[7])) {
                                cex = new ClassExpression().setClazz(IRI_PREFIX + fields[5]);
                                cpo.add(new CPO(IRI_PREFIX + fields[4], IRI_PREFIX + RF2.IS_A, IRI_PREFIX + fields[5]));
                            } else {
                                OPECardinalityRestriction osr = new OPECardinalityRestriction();
                                osr.setProperty(IRI_PREFIX + fields[7])
                                    .setClazz(IRI_PREFIX + fields[5]);

                                cex = new ClassExpression().setPropertyObject(osr);
                            }

                            List<ClassExpression> expressions;
                            Clazz c = (Clazz)m.getConcept();
                            if (m.isSubclass()) {
                                if (c.getSubClassOf() == null)
                                    c.setSubClassOf(new ArrayList<>());

                                expressions = new ArrayList<>(c.getSubClassOf());
                            } else {
                                if (c.getEquivalentTo() == null)
                                    c.setEquivalentTo(new ArrayList<>());
                                expressions = new ArrayList<>(c.getEquivalentTo());
                            }

                            if (!"0".equals(group)) {
                                ClassExpression rg = m.getRoleGroups().get(group);
                                if (rg == null) {
                                    OPECardinalityRestriction os = new OPECardinalityRestriction()
                                             .setProperty(RF2.ROLE_GROUP);
                                    os.setIntersection(new ArrayList<>());
                                    expressions.add(new ClassExpression().setPropertyObject(os));
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


}
