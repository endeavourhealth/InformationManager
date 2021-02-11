package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.imapi.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import org.apache.commons.io.input.BOMInputStream;
public class RF2ToDiscovery {
    private String country;
    private Map<String, Concept> conceptMap;
    private OWLOntologyManager owlManager;
    private OWLOntology owlOntology;
    private OWLToDiscovery owlTransform;
    private List<String> owlDocument;



    private Map<String, SnomedMeta> idMap = new HashMap<>(1000000);

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
    public static final String[] statedAxioms = {
        ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_sRefset_OWLExpressionSnapshot_INT_.*\\.txt"
    };


    public static final String FULLY_SPECIFIED = "900000000000003001";
    public static final String CLINICAL_REFSET = "999001261000000100";
    public static final String PHARMACY_REFSET = "999000691000001104";
    public static final String NECESSARY_INSUFFICIENT = "900000000000074008";
    public static final String IS_A = "116680003";
    public static final String SNOMED_ROOT = "sn:138875005";
    public static final String HIERARCHY_POSITION = ":CM_ValueTerminology";
    public static final String CODE_SCHEME = ":891101000252101";
    public static final String SN = "sn:";
    public static final String ROLE_GROUP = "sn:609096000";
    public static final String MEMBER_OF = "sn:394852005";
    public static final String ALL_CONTENT = "723596005";
    public static final String ACTIVE = "1";
    public static final String REPLACED_BY = "370124000";
    public static final String IN_ROLE_GROUP_OF = ":inRoleGroupOf";
    private Integer axiomCount;


    private Set<String> clinicalPharmacyRefsetIds = new HashSet<>();
    private ECLToDiscovery eclConverter = new ECLToDiscovery();
    private Ontology ontology;


    //======================PUBLIC METHODS============================

    /**
     * A per country method of loading e.g. INT or UK, used mostly for utility. It is better to use the country neutral method
     *
     * @param inFolder input route folder containining the RF2 elease folders
     * @param country  a string that differentiates one county or import from another e.g. uk INT UKClinical
     * @return Discovery Ontology object containing the Snomed ontology
     * @throws Exception
     */
    public Ontology importRF2ToDiscovery(String inFolder, String country) throws Exception {
        this.country = country.toLowerCase();
        return importRF2ToDiscovery(inFolder);
    }

    /**
     * Loads a multi country RF2 release package into a Discovery ontology will process international followed by uk clinical
     * followed by uk drug. Loads MRCM models also. Does not load reference sets.
     *
     * @param inFolder root folder containing the RF2 folders
     * @return Discovery ontology
     * @throws Exception
     */

    public Ontology importRF2ToDiscovery(String inFolder) throws Exception {
        validateFiles(inFolder);
        conceptMap = new HashMap<>();
        DOWLManager manager = new DOWLManager();
        ontology = manager.createOntology(
            OntologyIri.DISCOVERY.getValue());

        importConceptFiles(inFolder);

        //Imports owl axioms from stated files
        owlDocument = new ArrayList<>();
        owlDocument.add(getHeader());
        importStatedFiles(inFolder);
        importRefsetFiles(inFolder);
        importDescriptionFiles(inFolder);
        importRelationshipFiles(inFolder);
        importMRCMDomainFiles(inFolder);
        importMRCMRangeFiles(inFolder);
        owlDocument.add(")");


        String outFile="c:\\temp\\SnomedOwl2.owl";
        BufferedWriter writer= Files.newBufferedWriter(Paths.get(outFile));
        for (String line : owlDocument) {
            writer.write(line);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        System.out.println("Loading OWL axioms into OWL Ontology");

        owlManager = OWLManager.createOWLOntologyManager();
        OWLOntologyLoaderConfiguration loader = new OWLOntologyLoaderConfiguration();
        owlManager.setOntologyLoaderConfiguration(loader.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
        owlOntology= owlManager.loadOntology(IRI.create(new File(outFile)));
        System.out.println("Converting OWL Axioms to Discovery and adding to concept definitions");

        OWLDocumentFormat format= owlManager.getOntologyFormat(owlOntology);
        owlTransform= new OWLToDiscovery();
        owlTransform.initializePrefixManager(owlOntology,format);
        int i=0;
        owlOntology.getAxioms().forEach(ax-> convertOWLAxiom(ax,i));
        return ontology;
    }

    private void convertOWLAxiom(OWLAxiom ax,int i) {
        owlTransform.processAxiom(ax,ontology,conceptMap);
        i++;
        if (i % 1000 == 0)
            System.out.println("Converted " + i + " axioms");
    }




    /**
     * Validates the presence of the various RF2 files from a root folder path
     * Note to include the history substitution file
     *
     * @param path
     * @throws IOException if the files are not all present
     */
    public void validateFiles(String path) throws IOException {
        String[] files = Stream.of(concepts, descriptions,
            relationships, refsets,
            substitutions, attributeRanges, attributeDomains)
            .flatMap(Stream::of)
            .toArray(String[]::new);

        for (String file : files) {
            List<Path> matches = findFilesForId(path, file);
            if (matches.size() != 1) {
                System.err.println("Could not find " + file + " in " + path);
                throw new IOException("No RF2 files in inout directory");
            } else {
                System.out.println("Found: " + matches.get(0).toString());
            }

        }
    }


    //=================private methods========================

    private void importConceptFiles(String path) throws IOException {
        int i = 0;
        for (String conceptFile : concepts) {
            Path file = findFilesForId(path, conceptFile).get(0);
            if (isCountry(file)) {
            System.out.println("Processing concepts in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");
                    if (!idMap.containsKey(fields[0])) {
                        Concept c = new Concept();
                        SnomedMeta m = new SnomedMeta();
                        c.setIri(SN + fields[0]);
                        c.setCode(fields[0]);
                        c.setConceptType(ConceptType.CLASSONLY);
                        c.setScheme(CODE_SCHEME);
                        c.setStatus(ACTIVE.equals(fields[2]) ? ConceptStatus.ACTIVE : ConceptStatus.INACTIVE);
                        if (NECESSARY_INSUFFICIENT.equals(fields[4]))
                            m.setSubclass(true);
                        m.setConcept(c);
                        ontology.addConcept(c);
                        idMap.put(fields[0], m);
                        conceptMap.put(SN+ fields[0],c);

                    }
                    i++;
                    line = reader.readLine();
                }
            }
            }
        }
        System.out.println("Imported " + i + " concepts");
    }

    private void importRefsetFiles(String path) throws IOException {
        int i = 0;
        for (String refsetFile : refsets) {
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

                        }
                    }
                    i++;
                    line = reader.readLine();
                }

            }

        }
        System.out.println("Imported " + i + " refset");
    }

    private void importDescriptionFiles(String path) throws IOException, DataFormatException {
        int i = 0;
        for (String descriptionFile : descriptions) {

            Path file = findFilesForId(path, descriptionFile).get(0);
            if (isCountry(file)) {
            System.out.println("Processing  descriptions in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine(); // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");
                    SnomedMeta m = idMap.get(fields[4]);
                    if (m==null)
                        m= createNewMeta(fields[4]);
                    Concept c = m.getConcept();
                    if (c == null)
                        throw new DataFormatException(fields[4] + " not recognised as concept");
                    if (FULLY_SPECIFIED.equals(fields[6])
                        && ACTIVE.equals(fields[2])
                        && c != null) {

                        if (fields[7].endsWith("(attribute)") && (c instanceof Concept)) {
                            ontology.getConcept().remove(c);
                            // Switch to ObjectProperty
                            ObjectProperty op = new ObjectProperty();
                            op.setIri(c.getIri())
                                .setConceptType(ConceptType.OBJECTPROPERTY)
                                .setDbid(c.getDbid())
                                .setCode(c.getCode())
                                .setScheme(c.getScheme())
                                .setStatus(c.getStatus());
                            ontology.addConcept(op);
                            m.setConcept(op);
                            c = op;
                            conceptMap.put(c.getIri(),op);
                        }
                        c.setName(fields[7]);
                    }
                    if (!FULLY_SPECIFIED.equals(fields[6]))
                        if (ACTIVE.equals(fields[2]))
                            c.addSynonym(new TermCode(fields[7], fields[0]));
                    i++;
                    line = reader.readLine();
                }
            }

            }
        }
        System.out.println("Imported " + i + " descriptions");
    }

    private boolean isCountry(Path file) {
        if (country == null)
            return true;
        if (file.toString().toLowerCase().contains(country))
            return true;
        else return false;
    }


    private void importStatedFiles(String path) throws IOException {
        int i = 0;
        for (String relationshipFile : statedAxioms) {
            Path file = findFilesForId(path, relationshipFile).get(0);
            if (isCountry(file)) {
                System.out.println("Processing owl expressions in " + file.getFileName().toString());
                try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                    String line = reader.readLine(); // Skip header
                    line = reader.readLine();
                    axiomCount = 0;
                    while (line != null && !line.isEmpty()) {
                        String[] fields = line.split("\t");
                        SnomedMeta m = idMap.get(fields[5]);
                        String axiom = fields[6];
                        if (!axiom.startsWith("Prefix"))
                            if (ACTIVE.equals(fields[2]))
                                if (!axiom.startsWith("Ontology"))
                                 owlDocument.add(axiom);
                        i++;
                        line = reader.readLine();
                    }
                }

            }
        }
        System.out.println("Imported " + i + " relationships");
    }


    private void importRelationshipFiles(String path) throws IOException {
        int i = 0;
        for (String relationshipFile : relationships) {
            Path file = findFilesForId(path, relationshipFile).get(0);
            if (isCountry(file)) {
                System.out.println("Processing relationships in " + file.getFileName().toString());
                try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                    String line = reader.readLine(); // Skip header
                    line = reader.readLine();
                    axiomCount = 0;
                    while (line != null && !line.isEmpty()) {
                        String[] fields = line.split("\t");
                        //if (fields[4].equals("73211009")) {
                            SnomedMeta m = idMap.get(fields[4]);
                            if (m == null)
                                m = createNewMeta(fields[4]);
                            int group = Integer.parseInt(fields[6]);
                            String relationship = fields[7];
                            String target = fields[5];
                            if (ACTIVE.equals(fields[2]) | (relationship.equals(REPLACED_BY))) {
                                addRelationship(m, group, relationship, target);
                          //  }
                        }
                        i++;
                        line = reader.readLine();
                    }
                }

            }
        }
        System.out.println("Imported " + i + " relationships");
    }

    private void addRelationship(SnomedMeta m, int group, String relationship, String target) {
        Concept c = m.getConcept();
        if (c.getStatus() != ConceptStatus.ACTIVE)
            if (!relationship.equals(REPLACED_BY))
                return;
        if (relationship.equals(IS_A)||relationship.equals(REPLACED_BY)) {
            c.addIsa(new ConceptReference(SN + target));
            if (relationship.equals(REPLACED_BY)) {
                Concept replacedBy = idMap.get(target).getConcept();
                replacedBy.addIsa(new ConceptReference(SN + c.getIri()));
            }
        } else {
            Relationship roleGroup= getRoleGroup(c,group);
            Relationship subRole= new Relationship();
            roleGroup.addRole(subRole);
            subRole.setProperty(new ConceptReference(SN+ relationship));
            subRole.setValue(new ClassExpression().setClazz(new ConceptReference(SN+ target)));
        }
    }

    private Relationship getRoleGroup(Concept c, int group) {
        if (c.getRole()!=null){
            for (Relationship r:c.getRole())
                if (r.getGroup()==group)
                    return r;

        }
        Relationship newGroup= new Relationship();
        newGroup.setGroup(group);
        newGroup.setProperty(new ConceptReference(ROLE_GROUP));
        c.addRole(newGroup);
        return newGroup;

    }

    private SnomedMeta createNewMeta(String snomed) {
        SnomedMeta meta= new SnomedMeta();
        Concept concept = new Concept();
        concept.setIri(SN+ snomed);
        concept.setRef(true);
        meta.setConcept(concept);
        idMap.put(snomed,meta);
        return meta;
    }



    private static String getHeader(){
        String header="Prefix(bc:=<http://www.DiscoveryDataService.org/InformationModel/Legacy/Barts_Cerner#>)\n"
            +"Prefix(:=<http://snomed.info/sct#>)\n"
            +"Prefix(owl:=<http://www.w3.org/2002/07/owl#>)\n"
            +"Prefix(rdf:=<http://www.w3.org/1999/02/22-rdf-syntax-ns#>)\n"
            +"Prefix(xml:=<http://www.w3.org/XML/1998/namespace#>)\n"
            +"Prefix(xsd:=<http://www.w3.org/2001/XMLSchema#>)\n"
            +"Prefix(rdfs:=<http://www.w3.org/2000/01/rdf-schema#>)\n"
            +"Ontology(<http://snomed.info/sct>\n";
        return header;
    }

    //Adds an expression to a subclass of or equivalent to axiom
    private ClassExpression setAxiomExpression(Concept c, ClassExpression ax, Integer group, String relationship,
                               String target) {
        // Expression is not yet created, create and populate
        if (ax == null) {
            ax = new ClassExpression();
            ax.setGroup(group);
            if (relationship.equals(IS_A)) {
                ax.setClazz(SN + target);
                return ax;
            } else {
                if (group > 0)
                    createRoleGroup(ax, group, relationship, target);
                else
                    createRole(ax, relationship, target);
                return ax;
            }
        } else {
            addToAxiom(ax, group, relationship, target);
            return ax;
        }
    }

    private void addToAxiom(ClassExpression ax, Integer group, String relationship,
                            String target){
        if (ax.getClazz()!=null) {
            ClassExpression replace= new ClassExpression();
            replace.setGroup(ax.getGroup());
            replace.setClazz(ax.getClazz());
            ax.setClazz((ConceptReference) null);
            ax.addIntersection(replace);
        }
        if (ax.getObjectPropertyValue()!=null) {
            ClassExpression replace= new ClassExpression();
            replace.setGroup(ax.getGroup());
            replace.setObjectPropertyValue(ax.getObjectPropertyValue());
            ax.setObjectPropertyValue(null);
            ax.addIntersection(replace);
        }
        if (relationship.equals(IS_A)){
            ClassExpression superClass= new ClassExpression();
            superClass.setGroup(0);
            superClass.setClazz(new ConceptReference(SN+ target));
            ax.addIntersection(superClass);
        } else if (group==0){
            ClassExpression role = new ClassExpression();
            role.setGroup(0);
            ax.addIntersection(role);
            createRole(role,relationship,target);
        } else {
            ClassExpression roleGroup = null;
            for (ClassExpression ex : ax.getIntersection()) {
                if (ex.getGroup() == group)
                    roleGroup = ex;
            }
            if (roleGroup == null) {
                roleGroup = new ClassExpression();
                ax.addIntersection(roleGroup);
                createRoleGroup(roleGroup, group, relationship, target);
            } else {
                addToRoleGroup(roleGroup, relationship, target);
            }
        }
    }


    private void addToRoleGroup(ClassExpression roleGroup, String relationship, String target) {
        ClassExpression roleExpressionGroup = roleGroup
            .getObjectPropertyValue()
            .getExpression();
        if (roleExpressionGroup.getObjectPropertyValue()!=null){
            ClassExpression replace= new ClassExpression();
            replace.setObjectPropertyValue(roleExpressionGroup.getObjectPropertyValue());
            roleExpressionGroup.setObjectPropertyValue(null);
            roleExpressionGroup.addIntersection(replace);
        }
        ClassExpression roleExpression= new ClassExpression();
        roleExpressionGroup.addIntersection(roleExpression);
        createRole(roleExpression,relationship,target);
    }

    private void createRole(ClassExpression exp, String relationship, String target){
        if (relationship.equals(IS_A)){
            exp.setClazz(new ConceptReference(SN+target));
        } else {
            ObjectPropertyValue role = new ObjectPropertyValue();
            role.setProperty(new ConceptReference(SN + relationship));
            role.setQuantification(QuantificationType.SOME);
            role.setValueType(new ConceptReference(SN + target));
            exp.setObjectPropertyValue(role);
        }
    }
    private void createRoleGroup(ClassExpression exp, Integer group,String relationship,
                                 String target){
        exp.setGroup(group);
        ObjectPropertyValue roleGroup = new ObjectPropertyValue();
        exp.setObjectPropertyValue(roleGroup);
        roleGroup.setProperty(new ConceptReference(ROLE_GROUP));
        roleGroup.setQuantification(QuantificationType.SOME);
        ClassExpression roleExpression = new ClassExpression();
        roleGroup.setExpression(roleExpression);
        ObjectPropertyValue role = new ObjectPropertyValue();
        role.setProperty(new ConceptReference(SN + relationship));
        role.setQuantification(QuantificationType.SOME);
        role.setValueType(new ConceptReference(SN + target));
        roleExpression.setObjectPropertyValue(role);
    }

    private ClassExpression createExpression(String relationship,String target){
        ClassExpression exp= new ClassExpression();
        if (relationship.equals(IS_A)) {
            exp.setClazz(SN + target);
            return exp;
        }
        else {
            ObjectPropertyValue role = new ObjectPropertyValue();
            role.setProperty(new ConceptReference(SN + relationship));
            role.setQuantification(QuantificationType.SOME);
            role.setValueType(new ConceptReference(SN + target));
            exp.setObjectPropertyValue(role);
            return exp;
        }
    }




    private List<Path> findFilesForId(String path, String regex) throws IOException {
        return Files.find(Paths.get(path), 16,
                (file, attr) -> file.toString()
                    .matches(regex))
                .collect(Collectors.toList());
    }

    private void importMRCMDomainFiles(String path) throws IOException {
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
                            if (m!=null) {
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

    private void importMRCMRangeFiles(String path) throws IOException {
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
                        if (fields[2].equals("1")) {
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

    private ObjectProperty addSnomedPropertyRange(ObjectProperty op, String ecl) {

        ClassExpression rangeAx;
        if (op.getObjectPropertyRange() == null) {
            rangeAx = new ClassExpression();
            op.addObjectPropertyRange(rangeAx);
        }

        //Only one range axiom per property
        rangeAx = op.getObjectPropertyRange().stream().findFirst().get();

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

    private void addToRangeAxiom(ClassExpression rangeAx, ClassExpression ce){
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
                    String Concept = rangeAx.getClazz().getIri();
                    rangeAx.setClazz((ConceptReference) null);
                    ClassExpression union = new ClassExpression();
                    union.setClazz(new ConceptReference(Concept));
                    rangeAx.addUnion(union);
                    rangeAx.addUnion(ce);
                }
            }
        }

    }

    private boolean duplicateRange(ClassExpression rangeAx, ClassExpression ce) {
        boolean result = false;
        if (ce.getClazz()!=null)
            for (ClassExpression oldEx:rangeAx.getUnion())
                if (oldEx.getClazz()!=null)
                    if (oldEx.getClazz().equals(ce.getClazz()))
                        result= true;
        return result;
    }


    private ObjectProperty addSnomedPropertyDomain(ObjectProperty op, String domain,
                                                          Integer inGroup, String card,
                                                          String cardInGroup, ConceptStatus status){


        ClassExpression ca= createClassExpression(domain,inGroup);

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
            ClassExpression pdomain= op.getPropertyDomain().stream().findFirst().get();
            //Is it already a union?
            if (pdomain.getUnion()!=null){
                pdomain.addUnion(ca);
            }
            else {
                //Remove the old axiom, add a union in and add the old expression in
                op.getPropertyDomain().remove(pdomain);
                ClassExpression newPDomain = new ClassExpression();
                pdomain.setDbid(null);
                newPDomain.addUnion(pdomain);
                ca.setDbid(null);
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

    private ClassExpression createClassExpression(String domain, Integer inGroup) {
        ClassExpression ca= new ClassExpression();
        if (inGroup==1){
            ObjectPropertyValue ope= new ObjectPropertyValue();
            ope.setProperty(new ConceptReference(IN_ROLE_GROUP_OF));
            ope.setQuantification(QuantificationType.SOME);
            ope.setValueType(new ConceptReference(SN + domain));
            ca.setObjectPropertyValue(ope);
        }
        else {
            ca.setClazz(new ConceptReference(SN+ domain));

        }
        return ca;

    }





    private static boolean hasMRCM(Concept concept){
        if (concept instanceof ObjectProperty){
            ObjectProperty op = (ObjectProperty) concept;
            if (op.getPropertyDomain()!=null|op.getObjectPropertyRange()!=null)
                return true;
        }
        return false;
    }




}
