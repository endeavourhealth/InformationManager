package org.endeavourhealth.informationmanager.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.models.ConceptType;
import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.informationmanager.common.transform.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RF2ToDiscovery {

    public static final String snomedDocument= "\\Snomed.json";
    public static final String metaFile= "\\Snomed-meta.txt";
    public static final String MRCMDocument= "\\MRCMOntology.json";
    public static final String MRCMOntologyIri = "http://www.DiscoveryDataService.org/InformationModel/SnomedMRCM";
    public static Map<String, SnomedMeta> idMap = new HashMap<>();

    public static final String[] concepts = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_INT_.*\\.txt",
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_.*\\.txt",
            ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_.*\\.txt",
    };

    public static final String[] refsets = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_INT_.*\\.txt",
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_.*\\.txt",
            ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_.*\\.txt",
    };

    public static final String[] descriptions = {

            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_INT_.*\\.txt",
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_.*\\.txt",
            ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_.*\\.txt",
    };

    public static final String[] relationships = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Relationship_Snapshot_INT_.*\\.txt",
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Relationship_Snapshot_.*\\.txt",
            ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Relationship_Snapshot_.*\\.txt",
    };

    public static final String[] substitutions = {
            ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Resources\\\\HistorySubstitutionTable\\\\xres2_HistorySubstitutionTable_.*\\.txt",
    };

    public static final String[] attributeRanges = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Metadata\\\\der2_ssccRefset_MRCMAttributeRangeSnapshot_INT_.*\\.txt",
    };

    public static final String[] attributeDomains = {
            ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Metadata\\\\der2_cissccRefset_MRCMAttributeDomainSnapshot_INT_.*\\.txt",
    };




    public static final String FULLY_SPECIFIED = "900000000000003001";
    public static final String CLINICAL_REFSET = "999001261000000100";
    public static final String PHARMACY_REFSET = "999000691000001104";
    public static final String NECESSARY_INSUFFICIENT = "900000000000074008";
    public static final String IS_A = "116680003";
    public static final String SNOMED_ROOT = "sn:138875005";
    public static final String HIERARCHY_POSITION = ":CM_ValueTerminology";
    public static final String CODE_SCHEME = ":891101000252101";
    public static final String IRI_PREFIX = "sn:";
    public static final String ROLE_GROUP = "sn:609096000";
    public static final String MEMBER_OF = "sn:394852005";
    public static final String ALL_CONTENT="723596005";
    public static final String ACTIVE = "1";



    private static final Set<String> clinicalPharmacyRefsetIds = new HashSet<>();
    private static final List<CPO> cpo = new ArrayList<>();
    private static ECLToDiscovery eclConverter= new ECLToDiscovery();
    private static final Map<String,Clazz> mrcmClasses = new HashMap<>();
    private static List<Concept> missingNames;
    private static Map<String,Concept> conceptList;
    private Entailment entailmentType;



    public static void main(String[] argv) throws Exception {
        if (argv.length != 4) {
            System.err.println("Parameters: <input folder> <output folder> <ASSERTED|INFERRED> <FILE|FOLDER>");
            System.exit(-1);
        }

        EntailmentType entailment = null;
        if ("ASSERTED".equalsIgnoreCase(argv[2]))
            entailment = EntailmentType.ASSERTED;
        else if ("INFERRED".equalsIgnoreCase(argv[2]))
            entailment = EntailmentType.INFERRED;
        else {
            System.err.println("Unknown entailment type [" + argv[2] + "]");
            System.exit(-1);
        }

        ConversionType conversion = null;
        if ("FILE".equalsIgnoreCase(argv[3]))
            conversion = ConversionType.RF2_TO_DISCOVERY_FILE;
        else if ("FOLDER".equalsIgnoreCase(argv[3]))
            conversion = ConversionType.RF2_TO_DISCOVERY_FILE;
        else {
            System.err.println("Unknown conversion type [" + argv[3] + "]");
            System.exit(-1);
        }

        importRF2ToDiscovery(argv[0], argv[1], entailment, conversion);
    }

    private static void importRF2ToDiscovery(String inFolder,String outFolder, EntailmentType entailment,
                                             ConversionType conversionType) throws Exception {
        validateFiles(inFolder);
        Ontology ontology = DOWLManager.createOntology(
            OntologyIri.DISCOVERY.getValue(),
            OntologyModuleIri.SNOMED.getValue()
        );

        importConceptFiles(inFolder, ontology);
        importRefsetFiles(inFolder);
        importDescriptionFiles(inFolder, ontology);
        importRelationshipFiles(inFolder, entailment);
        importMRCMDomainFiles(inFolder);
        importMRCMRangeFiles(inFolder);
        Document document = new Document();
        document.setInformationModel(ontology);
        if (conversionType == ConversionType.RF2_TO_DISCOVERY_FOLDER)
            outputDocuments(document, outFolder, snomedDocument, entailment);
        else if (conversionType == ConversionType.RF2_TO_DISCOVERY_FILE) {
            String filename = "\\Snomed-";
            filename += (entailment == EntailmentType.ASSERTED) ? "asserted" : "inferred";
            filename += ".json";
            outputDocument(document, outFolder + filename);
        }
    }


    static void importConceptFiles(String path, Ontology snomed) throws IOException {
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
                            c.addSubClassOf((ClassAxiom)new ClassAxiom().setClazz(HIERARCHY_POSITION));

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
    
    static void importRefsetFiles(String path) throws IOException {
        int i = 0;
        for(String refsetFile: refsets) {
            List<Path> paths = findFilesForId(path, refsetFile);
            Path file = paths.get(0);
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

    static void importDescriptionFiles(String path, Ontology snomed) throws IOException {
        int i = 0;
        for(String descriptionFile: descriptions) {
            Path file = findFilesForId(path, descriptionFile).get(0);
            System.out.println("Processing descriptions in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine(); // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");
                 //  if (fields[4].equals("103379005")){
                     //   System.err.println("problem iri found"+ fields[2]);
                   // }

                    SnomedMeta m = idMap.get(fields[4]);
                    if (FULLY_SPECIFIED.equals(fields[6])
                        && ACTIVE.equals(fields[2])
                        && m != null) {

                        Concept c = m.getConcept();

                        if (fields[7].endsWith("(attribute)") && (c instanceof Clazz)) {
                            // Switch to ObjectProperty
                            ObjectProperty op = new ObjectProperty();
                            op.setIri(c.getIri())
                                .setId(c.getId())
                                .setCode(c.getCode())
                                .setScheme(c.getScheme())
                                .setStatus(c.getStatus());
                            snomed.addObjectProperty(op);
                            snomed.deleteClazz((Clazz)c);
                            c = op;
                            m.setConcept(c);
                        }

                        c.setName(fields[7]);
                        i++;
                    }

                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " descriptions");
    }

    static void importRelationshipFiles(String path, EntailmentType entailmentType) throws IOException {
        Map<String,ClassExpression> groupMap= new HashMap<>();
        int i = 0;
        for (String relationshipFile : relationships) {
            Path file = findFilesForId(path, relationshipFile).get(0);
            System.out.println("Processing relationships in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine(); // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");
                    SnomedMeta m = idMap.get(fields[4]);
                    if (m != null && ACTIVE.equals(fields[2])) {
                        if (entailmentType==EntailmentType.INFERRED){
                            if (fields[7].equals(IS_A)){
                                m.getConcept().addIsa(new Concept().setIri(IRI_PREFIX+ fields[5]));
                            }
                        }
                        else {
                        if (m.getConcept() instanceof ObjectProperty) {
                            ((ObjectProperty) m.getConcept()).addSubObjectPropertyOf(
                                    new PropertyAxiom().setProperty(IRI_PREFIX + fields[5])
                            );
                        } else {
                            addToClass(m, groupMap, Integer.parseInt(fields[6]),
                                    fields[7], fields[5]);
                        }
                    }

                    }
                    i++;
                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " relationships");
    }

    private static void addToClass(SnomedMeta m,Map<String,ClassExpression> groupMap,
                                   Integer group,String relationship,String target) {
        ClassAxiom axiom;
        Clazz c = (Clazz) m.getConcept();
        if (m.isSubclass()) {
            if (c.getSubClassOf() == null) {
                axiom = new ClassAxiom();
                c.addSubClassOf(axiom);
            } else
                axiom = c.getSubClassOf().get(0);
        } else {
            if (c.getEquivalentTo() == null) {
                axiom = new ClassAxiom();
                c.addEquivalentTo(axiom);
            } else
                axiom = c.getEquivalentTo().get(0);
        }
        //Declares the class expression we are adding this relationship to
        ClassExpression cex = axiom;
        //which root class expession are we looking for, base axiom or a group
        if (group > 0)
            cex = findRoleGroup(c.getCode(),axiom, groupMap,group);

        //Do we now need to replace with an intersection ?
        cex=checkIntersection(cex);

        if (IS_A.equals(relationship))
            cex.setClazz(IRI_PREFIX + target);
        else {
            OPECardinalityRestriction ope = new OPECardinalityRestriction();
            cex.setPropertyObject(ope);
            ope.setProperty(IRI_PREFIX + relationship);
            ope.setQuantification("some");
            ope.setClazz(IRI_PREFIX + target);
        }
    }


    private static ClassExpression checkIntersection(ClassExpression cex) {
        if (cex.getClazz() != null) {
            String oldClazz = cex.getClazz();
            cex.setClazz(null);
            ClassExpression olex = new ClassExpression();
            olex.setClazz(oldClazz);
            cex.addIntersection(olex);
            ClassExpression inter = new ClassExpression();
            cex.addIntersection(inter);
            return inter;
        } else {
            if (cex.getPropertyObject() != null) {
                OPECardinalityRestriction ope = cex.getPropertyObject();
                cex.setPropertyObject(null);
                ClassExpression olEx = new ClassExpression();
                olEx.setPropertyObject(ope);
                cex.addIntersection(olEx);
                ClassExpression inter= new ClassExpression();
                cex.addIntersection(inter);
                return inter;
            } else {
                if (cex.getIntersection() != null) {
                    ClassExpression inter = new ClassExpression();
                    cex.addIntersection(inter);
                    return inter;
                }
            }
        }
        return cex;
    }

    private static ClassExpression findRoleGroup(String code,ClassAxiom axiom, Map<String,ClassExpression> groupMap,
                                                 Integer group) {
        if (groupMap.get(code+"/"+group.toString())!=null)
            return groupMap.get(code+"/"+ group.toString());
        else {
            ClassExpression roleGroup = checkIntersection(axiom);
            OPECardinalityRestriction ope=new OPECardinalityRestriction();
            roleGroup.setPropertyObject(ope);
            ope.setProperty(ROLE_GROUP);
            ope.setQuantification("some");
            groupMap.put(code+"/"+ group.toString(),ope);
            return ope;
        }
    }


    private static List<Path> findFilesForId(String path, String regex) throws IOException {
        return Files.find(Paths.get(path), 16,
                (file, attr) -> file.toString().matches(regex))
                .collect(Collectors.toList());
    }

    static void importMRCMDomainFiles(String path) throws IOException {
        int i = 0;

        //gets attribute domain files (usually only 1)
        for (String domainFile : attributeDomains) {
            Path file = findFilesForId(path, domainFile).get(0);
            System.out.println("Processing property domains in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    //Only process axioms relating to all snomed authoring
                    if (fields[11].equals(ALL_CONTENT)) {
                        SnomedMeta m = idMap.get(fields[5]);
                        if (m != null) {
                            //First time for domain class?
                            checkHasClazz(fields[6]);
                            //Update the axiom
                            ObjectProperty op = (ObjectProperty) m.getConcept();
                            op = addSnomedPropertyDomain(op, fields[6], Integer.parseInt(fields[7])
                                    , fields[8], fields[9], ConceptStatus.byValue(Byte.parseByte(fields[2])));

                        }
                    }

                    i++;
                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " axiom builder rows");
    }

    static void importMRCMRangeFiles(String path) throws IOException {
        int i = 0;
        //gets attribute range files (usually only 1)
        for (String rangeFile : attributeRanges) {
            Path file = findFilesForId(path, rangeFile).get(0);
            System.out.println("Processing property ranges in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");
                    if (fields[2].equals("1")){
                        SnomedMeta m = idMap.get(fields[5]);
                        if (m != null) {
                            //Update the axiom
                            ObjectProperty op = (ObjectProperty) m.getConcept();
                            op = addSnomedPropertyRange(op, fields[6]);
                        }
                    }

                    i++;
                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " axiom builder rows");
    }

    private static ObjectProperty addSnomedPropertyRange(ObjectProperty op, String ecl) {

        ClassAxiom rangeAx;
        if (op.getObjectPropertyRange() == null) {
            rangeAx = new ClassAxiom();
            op.addObjectPropertyRange(rangeAx);
        }

        //Only one range axiom per property
        rangeAx = op.getObjectPropertyRange().get(0);

        try {
            ClassExpression newEx = eclConverter.getClassExpression(ecl);
            if (newEx.getUnion() != null) {
                for (ClassExpression subEx : newEx.getUnion()) {
                    addToRangeAxiom(rangeAx, subEx);
                }
            } else
                addToRangeAxiom(rangeAx, newEx);
        } catch (Exception e) {
            System.err.println("File contains invalid or unsupported ECL language");
            return op;
        }
        return op;
    }

    private static void addToRangeAxiom(ClassAxiom rangeAx, ClassExpression ce){
        if (ce.getClazz()!=null)
            checkHasClazz(ce.getClazz().split(":")[1]);
        else
        if (ce.getObjectOneOf()!=null)
            checkHasClazz(ce.getObjectOneOf().get(0).split(":")[1]);
        else
            checkHasClazz(ce.getIntersection().get(0).getClazz().split(":")[1]);

        if (rangeAx.getUnion()!=null) {
            if (!duplicateRange(rangeAx,ce))
                rangeAx.addUnion(ce);
        }
        else {
            if (rangeAx.getClazz() == null) {
                if (ce.getClazz() != null) {
                    rangeAx.setClazz(ce.getClazz());
                }
                else if (ce.getObjectOneOf()!=null){
                    rangeAx.setObjectOneOf(ce.getObjectOneOf());
                }
                else {
                    for (ClassExpression inter : ce.getIntersection())
                        rangeAx.addIntersection(inter);
                }
            } else {
                if (ce.getClazz() != rangeAx.getClazz()) {
                    String clazz = rangeAx.getClazz();
                    rangeAx.setClazz(null);
                    ClassExpression union = new ClassExpression();
                    union.setClazz(clazz);
                    rangeAx.addUnion(union);
                    rangeAx.addUnion(ce);
                }
            }
        }

    }

    private static boolean duplicateRange(ClassAxiom rangeAx, ClassExpression ce) {
        boolean result = false;
        if (ce.getClazz()!=null)
            for (ClassExpression oldEx:rangeAx.getUnion())
                if (oldEx.getClazz()!=null)
                    if (oldEx.getClazz().equals(ce.getClazz()))
                        result= true;
        return result;
    }


    private static ObjectProperty addSnomedPropertyDomain(ObjectProperty op, String domain,
                                                          Integer inGroup, String card,
                                                          String cardInGroup, ConceptStatus status){


        ClassAxiom ca= createClassAxiom(domain,inGroup);

        //Default status is active but if deprecated add status
        if (status==ConceptStatus.INACTIVE){
            Annotation annot = DOWLManager.createAnnotation(":status",Byte.toString(status.getValue()));
            ca.addAnnotation(annot);
        }

        //First axiom for this object property
        if (op.getPropertyDomain()==null){
            op.addPropertyDomain(ca);
        }
        else {
            ClassAxiom pdomain= op.getPropertyDomain().get(0);
            //Is it already a union?
            if (pdomain.getUnion()!=null){
                pdomain.addUnion(ca);
            }
            else {
                //Remove the old axiom, add a union in and add the old expression in
                op.getPropertyDomain().remove(pdomain);
                ClassAxiom newPDomain = new ClassAxiom();
                pdomain.setId(null);
                newPDomain.addUnion(pdomain);
                ca.setId(null);
                newPDomain.addUnion(ca);
                op.addPropertyDomain(newPDomain);
            }
        }
        if (cardInGroup.charAt(cardInGroup.length()-1)=='1')
            if (op.getIsFunctional()==null){
                Axiom isFunctional = new Axiom();
                op.setIsFunctional(isFunctional);
            }
        return op;
    }

    private static ClassAxiom createClassAxiom(String domain, Integer inGroup) {
        ClassAxiom ca= new ClassAxiom();
        if (inGroup==1){
            OPECardinalityRestriction ope= new OPECardinalityRestriction();
            ope.setInverseOf(ROLE_GROUP);
            ope.setQuantification("some");
            ope.setClazz(IRI_PREFIX + domain);
            ca.setPropertyObject(ope);
        }
        else {
            ca.setClazz(IRI_PREFIX+ domain);

        }
        return ca;

    }




    public static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, descriptions,
                relationships, refsets,
                substitutions, attributeRanges,attributeDomains ).flatMap(Stream::of)
                .toArray(String[]::new);

        for(String file: files) {
            List<Path> matches = findFilesForId(path, file);
            if (matches.size() != 1) {
                System.err.println("Could not find " + file);
                throw new IOException("No RF2 files in inout directory");

            } else {
                System.out.println("Found: " + matches.get(0).toString());
            }
        }
    }
    protected static void outputDocument(Document document,String filename) throws IOException {
        System.out.println("Generating JSON");
        ObjectMapper mapper = new ObjectMapper(); // can use static singleton, inject: just make sure to reuse!
        // ... and configure
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        try (FileOutputStream fos = new FileOutputStream(new File(filename))) {
            mapper.writeValue(fos, document);
        }

    }

    protected static void outputDocuments(Document document,String outFolder,
                                          String filename,
                                          EntailmentType entailmentType) throws IOException {
        System.out.println("Generating multuple JSON documents");
        Integer increment=1;
        outputConcepts(document.getInformationModel(),outFolder+ filename.split(".json")[0]+ "-Common"+".json");
        outputObjectProperties(document.getInformationModel(),
                getIncremental(outFolder+ filename,
                        increment));
        Integer classCount=0;
        if (document.getInformationModel().getClazz()!=null)
        while (classCount<document.getInformationModel().getClazz().size()) {
            increment++;
            classCount = outputClasses(document.getInformationModel(),
                    getIncremental(outFolder + filename,increment),
                    increment,
                    classCount,
                    25000,
                    entailmentType);
        }
    }

    private static Integer outputClasses(Ontology full,
                                         String fileName, Integer increment,
                                         Integer from,
                                         Integer batchSize,
                                         EntailmentType entailmentType) throws IOException {
        Integer to=from;
        Ontology ontology = DOWLManager.createOntology(
            OntologyIri.DISCOVERY.getValue(),
            OntologyModuleIri.SNOMED.getValue() + increment.toString()
        );
        Document document = new Document();
        ontology.addImport(OntologyModuleIri.COMMON_CONCEPTS.getValue());
        document.setInformationModel(ontology);
        conceptList= new HashMap<>();
        for (int i=from; i<= (from+batchSize);i++) {
            if (i ==(full.getClazz().size()-1))
                return 100000000;
            Clazz c = full.getClazz().get(i);
            ontology.addClazz(c);
            conceptList.put(c.getIri(), c);
            to = i + 1;
        }
        //Adds names to make ui easier
        if (entailmentType== EntailmentType.ASSERTED)
            addNames(ontology);
        //outputs one document
        outputDocument(document,fileName);
        return to;
    }
    private static void outputConcepts(Ontology full, String fileName) throws IOException {

        Ontology ontology = DOWLManager.createOntology(
                OntologyIri.DISCOVERY.getValue(),
                OntologyModuleIri.COMMON_CONCEPTS.getValue() + "_1"
        );
        Document document = new Document();
        document.setInformationModel(ontology);
        if (full.getClazz()!=null)
          full.getClazz().forEach(cl -> {
            Clazz newCl= (Clazz) getBaseConcept(cl,new Clazz());
            ontology.addClazz(newCl);

        });
        if (full.getObjectProperty()!=null)
         full.getObjectProperty().forEach(op-> {
            ObjectProperty newOp= (ObjectProperty) getBaseConcept(op, new ObjectProperty());
            ontology.addObjectProperty(newOp);

        });
        if (full.getDataProperty()!=null)
            full.getDataProperty().forEach(dp-> {
                DataProperty newDp= (DataProperty) getBaseConcept(dp, new DataProperty());
            ontology.addDataProperty(newDp);

        });
        if (full.getAnnotationProperty()!=null)
         full.getAnnotationProperty().forEach(ap-> {
            AnnotationProperty newAp= (AnnotationProperty) getBaseConcept(ap, new AnnotationProperty());
            ontology.addAnnotationProperty(newAp);

        });
        if (full.getDataType()!=null)
         full.getDataType().forEach(dt-> {
            DataType newDt= (DataType) getBaseConcept(dt, new DataType());
            ontology.addDataType(newDt);

        });

        outputDocument(document,fileName);
    }
    private static Concept getBaseConcept(Concept fullConcept,Concept concept){

        Concept result= concept
                .setId(fullConcept.getId())
                .setStatus(fullConcept.getStatus())
                .setVersion(fullConcept.getVersion())
                .setIri(fullConcept.getIri())
                .setName(fullConcept.getName())
                .setDescription(fullConcept.getDescription())
                .setCode(fullConcept.getCode())
                .setScheme(fullConcept.getScheme());
        if (fullConcept.getAnnotations()!=null)
            fullConcept.getAnnotations().forEach(an ->{
                Annotation annot= new Annotation();
                annot.setProperty(an.getProperty());
                annot.setValue(an.getValue());
                result.addAnnotation(annot);
            });
        return result;
    }


    private static String getIncremental(String filename, Integer increment){
        return filename.split(".json")[0]+ "-"+ increment.toString()+".json";
    }

    private static void outputObjectProperties(Ontology full, String fileName) throws IOException {

        Ontology ontology = DOWLManager.createOntology(
            OntologyIri.DISCOVERY.getValue(),
            OntologyModuleIri.SNOMED.getValue() + "_1"
        );
        Document document = new Document();
        document.setInformationModel(ontology);
        ontology.addImport(OntologyModuleIri.COMMON_CONCEPTS.getValue());
        conceptList = new HashMap<>();
        full.getObjectProperty().forEach(op-> {
            ontology.addObjectProperty(op);
            conceptList.put(op.getIri(),op);
        });
        //Adds names to make ui easier
        addNames(ontology);
        //outputs one document
        outputDocument(document,fileName);
    }

    private static void addNames(Ontology ontology) {
        missingNames = new ArrayList<>();
        if (ontology.getObjectProperty() != null)
            ontology.getObjectProperty().forEach(op -> {
                if (op.getSubObjectPropertyOf() != null)
                    op.getSubObjectPropertyOf()
                            .forEach(sp -> checkName(ontology, ConceptType.OBJECTPROPERTY,
                                    sp.getProperty()));
                if (op.getPropertyDomain() != null)
                    op.getPropertyDomain()
                            .forEach(opd -> checkExpressionNames(ontology, opd));
                if (op.getObjectPropertyRange() != null)
                    op.getObjectPropertyRange().forEach(opr ->
                            checkExpressionNames(ontology, opr));

            });
        if (ontology.getClazz() != null)
            ontology.getClazz().forEach(cl -> {
                if (cl.getIri().equals("sn:34459009")){
                    System.out.println(cl.getIri());
                }
                if (cl.getSubClassOf() != null)
                    cl.getSubClassOf().forEach(sc -> checkExpressionNames(ontology, sc));
                if (cl.getEquivalentTo() != null)
                    cl.getEquivalentTo().forEach(sc -> checkExpressionNames(ontology, sc));
            });

        missingNames.forEach(mn->{
            if (mn instanceof ObjectProperty)
                ontology.addObjectProperty((ObjectProperty) mn);
            else
                ontology.addClazz((Clazz) mn);
        });
    }

    private static void checkExpressionNames(Ontology ontology,ClassExpression exp){
        if (exp.getClazz()!=null)
            checkName(ontology,ConceptType.CLASS,exp.getClazz());
        else
            if (exp.getPropertyObject()!=null) {
            if (exp.getPropertyObject().getProperty()!=null)
                checkName(ontology,ConceptType.OBJECTPROPERTY,exp.getPropertyObject().getProperty());
            else
                checkName(ontology,ConceptType.OBJECTPROPERTY,exp.getPropertyObject().getInverseOf());
            checkExpressionNames(ontology, exp.getPropertyObject());
        }

             else
                if (exp.getComplementOf()!=null)
                    checkExpressionNames(ontology,exp.getComplementOf());
                else
                     if (exp.getUnion()!=null)
                     exp.getUnion().forEach(u-> checkExpressionNames(ontology,u));
                else
                     if (exp.getIntersection()!=null)
                         exp.getIntersection().forEach(i-> checkExpressionNames(ontology,i));
    }

    private static void checkName(Ontology ontology, ConceptType conceptType,String iri){
        if (conceptList.get(iri)==null) {
            try {
                Concept refCon = idMap
                        .get(iri.substring(iri.lastIndexOf(":") + 1))
                        .getConcept();


                if (conceptType == ConceptType.OBJECTPROPERTY) {
                    ObjectProperty newOp = new ObjectProperty();
                    newOp.setId(refCon.getId());
                    newOp.setIri(refCon.getIri());
                    newOp.setName(refCon.getName());
                    newOp.setIsRef(true);
                    missingNames.add(newOp);
                    conceptList.put(iri, newOp);
                } else {
                    Clazz newC = new Clazz();
                    newC.setId(refCon.getId());
                    newC.setIri(refCon.getIri());
                    newC.setName(refCon.getName());
                    newC.setIsRef(true);
                    missingNames.add(newC);
                    conceptList.put(iri, newC);

                }
            }
            catch (Exception e) {
                System.out.println("problem with iri - "+ iri);
            }
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

    //Checks the class is in the class list
    private static void checkHasClazz(String conceptId) {
        if (!mrcmClasses.containsKey(conceptId)) {
            //find the concept and create a class
            try {
                SnomedMeta sn = idMap.get(conceptId);
                Concept c = sn.getConcept();
                Clazz cl = (Clazz) sn.getConcept();
                mrcmClasses.put(conceptId, cl);

            } catch (Exception e) {
                System.err.println("Concept in MRCM not in concept file");
            }
        }
    }

    public static void filterToMRCM(Ontology ontology){
        //Removes all classes
        ontology.setClazz(null);
        //adds in the class map
        mrcmClasses.forEach((str,clazz)-> {
            clazz.setEquivalentTo(null);
            clazz.setSubClassOf(null);
            clazz.setDisjointWithClass(null);
            ontology.addClazz(clazz);
        });
        List<ObjectProperty> removeList= new ArrayList<>();
        ontology.getObjectProperty().forEach(op->{
            if (op.getObjectPropertyRange()==null
            &&op.getPropertyDomain()==null
            && op.getIsFunctional()==null){
                removeList.add(op);
            }
        });
        removeList.forEach(op-> ontology.getObjectProperty().remove(op));
    }


}
