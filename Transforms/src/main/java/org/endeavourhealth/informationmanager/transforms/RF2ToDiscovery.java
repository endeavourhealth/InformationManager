package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.imapi.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

public class RF2ToDiscovery {
    private String country;


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
        DOWLManager manager = new DOWLManager();
        ontology = manager.createOntology(
            OntologyIri.DISCOVERY.getValue());

        importConceptFiles(inFolder);
        importRefsetFiles(inFolder);
        importDescriptionFiles(inFolder);
        importRelationshipFiles(inFolder);
        importMRCMDomainFiles(inFolder);
        //importMRCMRangeFiles(inFolder);
        return ontology;

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
                        if (fields[7].contains(("(attribute)")))
                            c.setConceptType(ConceptType.OBJECTPROPERTY);
                        if (FULLY_SPECIFIED.equals(fields[6])
                            && ACTIVE.equals(fields[2])
                            && c != null) {
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
                            setAxiom(m, group, relationship, target);
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

    private SnomedMeta createNewMeta(String snomed) {
        SnomedMeta meta= new SnomedMeta();
        Concept concept = new Concept();
        concept.setIri(SN+ snomed);
        concept.setRef(true);
        meta.setConcept(concept);
        idMap.put(snomed,meta);
        return meta;
    }

    private void setAxiom(SnomedMeta m,
                          int group, String relationship, String target) {
        if (m.getConcept().getStatus() != ConceptStatus.ACTIVE)
            if (!relationship.equals(REPLACED_BY))
                return;
        Concept c = m.getConcept();
        if (relationship.equals(IS_A)||relationship.equals(REPLACED_BY)){
            c.addIsa(new ConceptReference(SN+target));
            if (relationship.equals(REPLACED_BY)) {
                Concept replacedBy = idMap.get(target).getConcept();
                replacedBy.addIsa(new ConceptReference(SN + c.getIri()));
            }

        }
        //Simple sub object property of axiom
        if (c.getConceptType() == ConceptType.OBJECTPROPERTY) {
            if (relationship.equals(IS_A)) {
                c.addSubObjectPropertyOf(new PropertyAxiom().setProperty(new ConceptReference(SN + target)));
                return;
            } else
                throw new UnknownFormatConversionException("Unknown object property axiom "
                    + c.getName() + "->" + relationship + "->" + target);
        } else {
            //Subclass or equivalent?
            if (m.isSubclass()) {
                if (c.getSubClassOf() == null) {
                    c.addSubClassOf(setAxiomExpression(c,null, group, relationship, target));
                }
                else {
                    setAxiomExpression(c,
                        c.getSubClassOf().stream().findFirst().get(),
                        group,
                        relationship,
                        target);

                }
            } else {
                if (c.getEquivalentTo() == null){
                    c.addEquivalentTo(setAxiomExpression(c,null,group,relationship,target));
                } else {
                    setAxiomExpression(c,
                        c.getEquivalentTo().stream().findFirst().get(),
                        group,
                        relationship,
                        target);
                }
            }
        }
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
        if (ax.getPropertyValue()!=null) {
            ClassExpression replace= new ClassExpression();
            replace.setGroup(ax.getGroup());
            replace.setPropertyValue(ax.getPropertyValue());
            ax.setPropertyValue(null);
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
            .getPropertyValue()
            .getExpression();
        if (roleExpressionGroup.getPropertyValue()!=null){
            ClassExpression replace= new ClassExpression();
            replace.setPropertyValue(roleExpressionGroup.getPropertyValue());
            roleExpressionGroup.setPropertyValue(null);
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
            PropertyValue role = new PropertyValue();
            role.setProperty(new ConceptReference(SN + relationship));
            role.setQuantification(QuantificationType.SOME);
            role.setValueType(new ConceptReference(SN + target));
            exp.setPropertyValue(role);
        }
    }
    private void createRoleGroup(ClassExpression exp, Integer group,String relationship,
                                 String target){
        exp.setGroup(group);
        PropertyValue roleGroup = new PropertyValue();
        exp.setPropertyValue(roleGroup);
        roleGroup.setProperty(new ConceptReference(ROLE_GROUP));
        roleGroup.setQuantification(QuantificationType.SOME);
        ClassExpression roleExpression = new ClassExpression();
        roleGroup.setExpression(roleExpression);
        PropertyValue role = new PropertyValue();
        role.setProperty(new ConceptReference(SN + relationship));
        role.setQuantification(QuantificationType.SOME);
        role.setValueType(new ConceptReference(SN + target));
        roleExpression.setPropertyValue(role);
    }

    private ClassExpression createExpression(String relationship,String target){
        ClassExpression exp= new ClassExpression();
        if (relationship.equals(IS_A)) {
            exp.setClazz(SN + target);
            return exp;
        }
        else {
            PropertyValue role = new PropertyValue();
            role.setProperty(new ConceptReference(SN + relationship));
            role.setQuantification(QuantificationType.SOME);
            role.setValueType(new ConceptReference(SN + target));
            exp.setPropertyValue(role);
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
                            Concept op = m.getConcept();
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
        for (String rangeFile : attributeRanges)
        System.out.println("Imported " + i + " axiom builder rows");
    }

    private Concept addSnomedPropertyRange(Concept op, String ecl) {

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


    private Concept addSnomedPropertyDomain(Concept op, String domain,
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
            PropertyValue ope= new PropertyValue();
            ope.setInverseOf(new ConceptReference(ROLE_GROUP));
            ope.setQuantification(QuantificationType.SOME);
            ope.setValueType(new ConceptReference(SN + domain));
            ca.setPropertyValue(ope);
        }
        else {
            ca.setClazz(new ConceptReference(SN+ domain));

        }
        return ca;

    }





    private static boolean hasMRCM(Concept op){
            if (op.getPropertyDomain()!=null|op.getObjectPropertyRange()!=null)
                return true;
        return false;
    }




}