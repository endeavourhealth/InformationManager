package org.endeavourhealth.informationmanager.transforms;

import com.codahale.metrics.EWMA;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.models.Property;
import org.endeavourhealth.informationmanager.common.transform.DOWLManager;
import org.endeavourhealth.informationmanager.common.transform.model.*;
//import org.snomed.langauges.ecl.ECLQueryBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SnomedMRCM {

    private List<ObjectProperty> opList = new ArrayList<>();
    private List<Clazz> cList = new ArrayList<>();
    private Map<String, ObjectProperty> opMap = new HashMap<>();
    private Map<String, Clazz> cMap = new HashMap<>();
    private ECLConverter eclConverter = new ECLConverter();
    private Set<String> clinicalPharmacyRefsetIds = new HashSet<>();
    private String IRI_PREFIX = "sn:";
    private String CODE_SCHEME = ":891101000252101";

    /**
     * Imports the Snomed MRCM files and creates Discovery ontology
     *
     * @param inputFolder  folder containing the International and UK RF2 releases
     * @param outputFolder Folder to contain the MRCM ontology. The output folder should also contain the UUI mapping file
     * @return Returns an ontology for further use
     * @throws IOException
     */
    public Ontology saveMRCMAsDiscovery(String inputFolder, String outputFolder) throws IOException {
        RF2.validateFiles(inputFolder);
        Ontology ontology = DOWLManager
                .createOntology("http://www.DiscoveryDataService.org/InformationModel/SnomedMRCM");
        //Obtains the Snomed ID-> Discovery UUI map
        RF2.importIMUUIDMap(outputFolder);
        //Gets concepts for reference
        importConceptRefs(inputFolder);
        //Imports refsets for internal integrity
        importRefsetFiles(inputFolder);
        //Imports descriptions just for names
        importNames(inputFolder);
        //Property Domains
        importMRCMDomainFiles(inputFolder);
        //Property Ranges
        importMRCMRangeFiles(inputFolder);
        //Saves the UUID Map
        RF2.saveIMUUIDMap(outputFolder);
        RF2.clearMaps();

        //Adds classes and object properties to ontology
        cList.forEach(a -> ontology.addClazz(a));
        opList.forEach(a -> ontology.addObjectProperty(a));
        Document document = new Document();
        document.setInformationModel(ontology);
        RF2.outputDocument(document, outputFolder, RF2.MRCMDocument);
        return ontology;

    }


    //Imports description file just to get the preferred name of the concept
    private void importNames(String inputFolder) throws IOException {
        int i = 0;
        for (String descriptionFile : RF2.descriptions) {
            Path file = findFilesForId(inputFolder, descriptionFile).get(0);
            System.out.println("Imports full names in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine(); // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");
                    SnomedMeta m = RF2.idMap.get(fields[4]);
                    if (RF2.FULLY_SPECIFIED.equals(fields[6])
                            && RF2.ACTIVE.equals(fields[2])
                            && m != null
                            && m.getModuleId().equals(fields[3])) {

                        Concept c = m.getConcept();
                        c.setName(fields[7]);
                        i++;
                    }

                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " descriptions");
    }

    private void importConceptRefs(String path) throws IOException {
        int i = 0;
        for (String conceptFile : RF2.concepts) {
            Path file = findFilesForId(path, conceptFile).get(0);
            System.out.println("Processing concepts in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");

                    if (!RF2.idMap.containsKey(fields[0])) {
                        Clazz c = new Clazz();
                        String id = RF2.getUUIDMap("SnomedCT/Concept/" + fields[0]);
                        c.setId(id);
                        c.setIri(IRI_PREFIX + fields[0]);
                        c.setCode(fields[0]);
                        c.setScheme(CODE_SCHEME);
                        c.setStatus(RF2.ACTIVE.equals(fields[2]) ? ConceptStatus.ACTIVE : ConceptStatus.INACTIVE);

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

    private List<Path> findFilesForId(String path, String regex) throws IOException {
        return Files.find(Paths.get(path), 16,
                (file, attr) -> file.toString().matches(regex))
                .collect(Collectors.toList());
    }

    private void importRefsetFiles(String path) throws IOException {
        int i = 0;
        for (String refsetFile : RF2.refsets) {
            Path file = findFilesForId(path, refsetFile).get(0);
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

    private void importMRCMDomainFiles(String path) throws IOException {
        int i = 0;


        //Creates role group entry
        SnomedMeta sn1 = RF2.idMap.get("609096000");
        ObjectProperty p1 = DOWLManager.conceptAsObjectProperty(sn1.getConcept());
        opMap.put("609096000", p1);
        opList.add(p1);
        //gets attribute domain files (usually only 1)
        for (String domainFile : RF2.attributeDomains) {
            Path file = findFilesForId(path, domainFile).get(0);
            System.out.println("Processing property domains in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");
                    if (fields[11].equals(RF2.ALL_CONTENT)) {
                        //First time for domain class?
                        checkHasClazz(fields[6]);

                        //First axiom for this property ? create object property
                        if (!opMap.containsKey(fields[5])) {
                            //find the concept and create an objectProperty
                            try {
                                SnomedMeta sn = RF2.idMap.get(fields[5]);
                                Concept c = sn.getConcept();
                                ObjectProperty p = DOWLManager.conceptAsObjectProperty(c);
                                opMap.put(fields[5], p);
                                opList.add(p);
                            } catch (Exception e) {
                                System.err.println("Concept in MRCM not in concept file");
                            }
                        }
                        //Update the axiom
                        ObjectProperty op = opMap.get(fields[5]);
                        op = addSnomedPropertyDomain(op, fields[6], Integer.parseInt(fields[7])
                                , fields[8], fields[9], ConceptStatus.byValue(Byte.parseByte(fields[2])));
                        opMap.put(fields[5], op);
                    }

                    i++;
                    line = reader.readLine();
                }
            }
        }
        System.out.println("Imported " + i + " axiom builder rows");
    }

    //Checks the class is in the class list
    private void checkHasClazz(String conceptId) {
        if (!cMap.containsKey(conceptId)) {
            //find the concept and create a class
            try {
                SnomedMeta sn = RF2.idMap.get(conceptId);
                Concept c = sn.getConcept();
                Clazz cl = (Clazz) sn.getConcept();
                cMap.put(conceptId, cl);
                cList.add(cl);
            } catch (Exception e) {
                System.err.println("Concept in MRCM not in concept file");
            }
        }
    }

    private void importMRCMRangeFiles(String path) throws IOException {
        int i = 0;
        //gets attribute range files (usually only 1)
        for (String rangeFile : RF2.attributeRanges) {
            Path file = findFilesForId(path, rangeFile).get(0);
            System.out.println("Processing property ranges in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line = reader.readLine();    // Skip header
                line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] fields = line.split("\t");
                    //only deal with active ranges
                    if (fields[2].equals("1")) {
                        //First axiom for this property ? create object property
                        if (!opMap.containsKey(fields[5])) {
                            //find the concept and create an objectProperty
                            try {
                                SnomedMeta sn = RF2.idMap.get(fields[5]);
                                Concept c = sn.getConcept();
                                ObjectProperty p = DOWLManager.conceptAsObjectProperty(c);
                                opMap.put(fields[5], p);
                                opList.add(p);
                            } catch (Exception e) {
                                System.err.println("Concept in MRCM not in concept file");
                            }
                        }
                        //Update the axiom
                        ObjectProperty op = opMap.get(fields[5]);
                        op = addSnomedPropertyRange(op, fields[6]);
                        opMap.put(fields[5], op);
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
            String axiomid = RF2.getUUIDMap("SnomedCT/PropertyRange/" + op.getCode());
            rangeAx.setId(axiomid);
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

    private void addToRangeAxiom(ClassAxiom rangeAx, ClassExpression ce){
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

    private boolean duplicateRange(ClassAxiom rangeAx, ClassExpression ce) {
        boolean result = false;
        if (ce.getClazz()!=null)
            for (ClassExpression oldEx:rangeAx.getUnion())
                if (oldEx.getClazz()!=null)
                    if (oldEx.getClazz().equals(ce.getClazz()))
                        result= true;
        return result;
    }


    private ObjectProperty addSnomedPropertyDomain(ObjectProperty op,String domain,
                                                          Integer inGroup,String card,
                                                          String cardInGroup, ConceptStatus status){
        String axiomid= RF2.getUUIDMap("SnomedCT/PropertyDomain/"+op.getCode());

        ClassAxiom ca= createClassAxiom(domain,inGroup);

        //Default status is active but if deprecated add status
        if (status==ConceptStatus.INACTIVE){
            Annotation annot = DOWLManager.createAnnotation(":status",Byte.toString(status.getValue()));
            ca.addAnnotation(annot);
        }
        ca.setId(axiomid);
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
                newPDomain.setId(axiomid);
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
                isFunctional.setId(RF2.getUUIDMap("SnomedCT/FuntionalProperty/"+ op.getCode()));
                op.setIsFunctional(isFunctional);
            }
        return op;
    }

    private ClassAxiom createClassAxiom(String domain, Integer inGroup) {
        ClassAxiom ca= new ClassAxiom();
        if (inGroup==1){
            OPECardinalityRestriction ope= new OPECardinalityRestriction();
            ope.setInverseOf(RF2.ROLE_GROUP);
            ope.setQuantification("some");
            ope.setClazz(IRI_PREFIX + domain);
            ca.setPropertyObject(ope);
        }
        else {
            ca.setClazz(IRI_PREFIX+ domain);

        }
        return ca;

    }





}
