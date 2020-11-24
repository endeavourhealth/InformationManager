package org.endeavourhealth.informationmanager.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.map.MultiValueMap;
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
    public static final String ALL_CONTENT="723596005";
    public static final String ACTIVE = "1";
    public static final String REPLACED_BY="370124000";
    private Integer axiomCount;



    private Set<String> clinicalPharmacyRefsetIds = new HashSet<>();
    private ECLToDiscovery eclConverter= new ECLToDiscovery();
    private Ontology ontology;


    //======================PUBLIC METHODS============================
    public Ontology importRF2ToDiscovery(String inFolder) throws Exception {
        validateFiles(inFolder);
        DOWLManager manager= new DOWLManager();
        ontology = manager.createOntology(
            OntologyIri.DISCOVERY.getValue(),
            OntologyModuleIri.SNOMED.getValue()
        );

        importConceptFiles(inFolder);
        importRefsetFiles(inFolder);
        importDescriptionFiles(inFolder);
        importRelationshipFiles(inFolder);
        importMRCMDomainFiles(inFolder);
        importMRCMRangeFiles(inFolder);
        return ontology;

    }

    /**
     * Validates the presence of the various RF2 files from a root folder path
     * Note to include the history substitution file
     * @param path
     * @throws IOException if the files are not all present
     */
    public void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, descriptions,
            relationships, refsets,
            substitutions, attributeRanges,attributeDomains )
            .flatMap(Stream::of)
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


    //=================private methods========================

    private void importConceptFiles(String path) throws IOException {
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
                            Concept c = new Concept();
                            SnomedMeta m=new SnomedMeta();
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
        System.out.println("Imported " + i + " concepts");
    }
    
    private void importRefsetFiles(String path) throws IOException {
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

                            }
                        }
                        i++;
                        line = reader.readLine();
                    }

            }

        }
        System.out.println("Imported " + i + " refset");
    }

    private void importDescriptionFiles(String path) throws IOException,DataFormatException {
        int i = 0;
        for(String descriptionFile: descriptions) {

                Path file = findFilesForId(path, descriptionFile).get(0);
                System.out.println("Processing  descriptions in " + file.getFileName().toString());
                try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                    String line = reader.readLine(); // Skip header
                    line = reader.readLine();
                    while (line != null && !line.isEmpty()) {
                        String[] fields = line.split("\t");
                        SnomedMeta m= idMap.get(fields[4]);
                        Concept c= m.getConcept();
                        if (c==null)
                            throw new DataFormatException(fields[4]+" not recognised as concept");
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
                                c=op;
                            }
                            c.setName(fields[7]);
                        }
                        if (ACTIVE.equals(fields[2]))
                            c.addSynonym(new Synonym(fields[7], fields[0]));
                        i++;
                        line = reader.readLine();
                    }

            }
        }
        System.out.println("Imported " + i + " descriptions");
    }

    private void importRelationshipFiles(String path) throws IOException {
        int i = 0;
        for (String relationshipFile : relationships) {
                Path file = findFilesForId(path, relationshipFile).get(0);
                System.out.println("Processing relationships in " + file.getFileName().toString());
                try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                    String line = reader.readLine(); // Skip header
                    line = reader.readLine();
                    axiomCount = 0;
                    while (line != null && !line.isEmpty()) {
                        String[] fields = line.split("\t");
                        SnomedMeta m = idMap.get(fields[4]);
                        int group = Integer.parseInt(fields[6]);
                        String relationship = fields[7];
                        String target = fields[5];
                        if (ACTIVE.equals(fields[2])) {
                            setRelationship(m, group, relationship, target);
                        }
                        i++;
                        line = reader.readLine();
                    }

            }
        }
        System.out.println("Imported " + i + " relationships");
    }
    private void setRelationship(SnomedMeta m,
                                int group,String relationship,String target){
        if (m.getConcept().getStatus()!=ConceptStatus.ACTIVE)
            if (!relationship.equals(REPLACED_BY))
                return;
        if (relationship.equals(IS_A)) {
            m.getConcept().addIsa(new ConceptReference(idMap
                .get(target)
                .getConcept()
                .getIri()));
            if (m.getConcept() instanceof ObjectProperty)
                ((ObjectProperty) m.getConcept()).addSubObjectPropertyOf(
                    new PropertyAxiom().setProperty(SN + target));
            else
                addToClass(m, group, relationship, target);
        } else
            addToClass(m, group, relationship,target);

    }
    private void addToClass(SnomedMeta m, int group,String relationship,String target) {
        ClassAxiom axiom;
        Concept c = m.getConcept();
        ClassExpression cex;
        // Finds or creates the class expression we are adding this relationship to
        //Do we now need to replace with an intersection ?
        //The logic is that if there is already an entry at this point it must
        //either be an intersection or one must be created but if there is no entry
        //then an intersection is not yet needed
        if (m.isSubclass()) {
            if (c.getSubClassOf() == null) {
                axiom = new ClassAxiom();
                c.addSubClassOf(axiom);
                axiom.setGroup(group);
                cex= axiom;
            } else
                cex= checkIntersection(c.getSubClassOf().stream().findFirst().get(),group);
        } else {
            if (c.getEquivalentTo() == null) {
                axiom = new ClassAxiom();
                c.addEquivalentTo(axiom);
                axiom.setGroup(0);
                cex=axiom;
            } else
                cex= checkIntersection(c.getEquivalentTo().stream().findFirst().get(),group);
        }

        
        if (IS_A.equals(relationship))
            cex.setClazz(new ConceptReference(SN + target));
        else {
            ObjectPropertyValue ope = new ObjectPropertyValue();
            cex.setObjectPropertyValue(ope);
            ope.setProperty(new ConceptReference(SN + relationship));
            ope.setQuantification(QuantificationType.SOME);
            ope.setValueType(new ConceptReference(SN + target));
        }
    }


    private ClassExpression checkIntersection(ClassAxiom cax,int group) {
        //Checks whether an intersection already exists, if not create one
        //assign the old expression to it and create a new one
        if (cax.getClazz() != null) {
            String oldConcept = cax.getClazz().getIri();
            cax.setClazz((ConceptReference) null);
            ClassExpression olex = new ClassExpression();
            olex.setClazz(new ConceptReference(oldConcept));
            cax.addIntersection(olex);
            ClassExpression inter = new ClassExpression();
            cax.addIntersection(inter);
            inter.setGroup(group);
            return inter;
        } else if (cax.getObjectPropertyValue() != null) {
                ObjectPropertyValue ope = cax.getObjectPropertyValue();
                cax.setObjectPropertyValue(null);
                ClassExpression olEx = new ClassExpression();
                olEx.setObjectPropertyValue(ope);
                cax.addIntersection(olEx);
                ClassExpression inter= new ClassExpression();
                cax.addIntersection(inter);
                inter.setGroup(group);
                return inter;
            } else   //Already exists find it or create one
                return getRoleGroup(cax,group);
    }

    private ClassExpression getRoleGroup(ClassAxiom axiom, int group) {
        for (ClassExpression ex:axiom.getIntersection())
            if (ex.getGroup()==group)
                return ex;
        //Not found so new group must be created
        ClassExpression ex= new ClassExpression();
        ex.setGroup(group);
        axiom.addIntersection(ex);
        return ex;
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
                            ObjectProperty op = (ObjectProperty) m.getConcept();
                            op = addSnomedPropertyDomain(op, fields[6], Integer.parseInt(fields[7])
                                , fields[8], fields[9], ConceptStatus.byValue(Byte.parseByte(fields[2])));

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

        ClassAxiom rangeAx;
        if (op.getObjectPropertyRange() == null) {
            rangeAx = new ClassAxiom();
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

    private void addToRangeAxiom(ClassAxiom rangeAx, ClassExpression ce){
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

    private boolean duplicateRange(ClassAxiom rangeAx, ClassExpression ce) {
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
            ClassAxiom pdomain= op.getPropertyDomain().stream().findFirst().get();
            //Is it already a union?
            if (pdomain.getUnion()!=null){
                pdomain.addUnion(ca);
            }
            else {
                //Remove the old axiom, add a union in and add the old expression in
                op.getPropertyDomain().remove(pdomain);
                ClassAxiom newPDomain = new ClassAxiom();
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

    private ClassAxiom createClassAxiom(String domain, Integer inGroup) {
        ClassAxiom ca= new ClassAxiom();
        if (inGroup==1){
            ObjectPropertyValue ope= new ObjectPropertyValue();
            ope.setInverseOf(new ConceptReference(ROLE_GROUP));
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
