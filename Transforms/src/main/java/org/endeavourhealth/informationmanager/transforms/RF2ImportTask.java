package org.endeavourhealth.informationmanager.transforms;

import javafx.concurrent.Task;
import org.endeavourhealth.informationmanager.common.transform.ConversionType;
import org.endeavourhealth.informationmanager.common.transform.DOWLManager;
import org.endeavourhealth.informationmanager.common.transform.EntailmentType;
import org.endeavourhealth.informationmanager.common.transform.model.Document;
import org.endeavourhealth.informationmanager.common.transform.model.DocumentInfo;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;

import java.io.IOException;

/**
 * Task object to manage some conversion types such as import of the MRCM files fron Snomed into Discovery ontology
 * <p> bind the progressProperty to a progress monitory of some kind. If used in a thread May be cancelled
 * by the Task.cancel() method which is checked for periodically</p>
 * @author David Stables Endeavour
 */
public class RF2ImportTask extends Task {
    private String inputFolder;
    private ConversionType importType;
    private String outputFolder;
    private String messageLines= "";
    private RF2ToDiscovery snomed;
    private String uuidFolder;



    private void updateMessageLine(String line){
        messageLines=messageLines+ line+ "\n";
        updateMessage(messageLines);
    }

    private void importSnomed(EntailmentType entailmentType) throws Exception {

        DOWLManager dmanager = new DOWLManager();
        snomed= new RF2ToDiscovery();
        Ontology ontology = dmanager.createOntology(
                "http://www.DiscoveryDataService.org/InformationModel/Snomed");


        ontology.setDocumentInfo(
                new DocumentInfo().setDocumentIri("http://www.DiscoveryDataService.org/InformationModel/Snomed")
        );

        updateMessageLine("Validating source RF2 files...");
        snomed.validateFiles(inputFolder);

        this.updateMessageLine("Importing UUID map");
        snomed.importUUIDMap(uuidFolder);
        this.updateProgress(1,10);

        if (isCancelled()) return;

        this.updateProgress(2,10);
        if (isCancelled()) return;

        this.updateMessageLine("Importing concept file...");
        snomed.importConceptFiles(inputFolder, ontology);
        this.updateProgress(3,10);

        if (isCancelled()) return;

        this.updateMessageLine("Importing refsets...");
        snomed.importRefsetFiles(inputFolder);
        this.updateProgress(4,10);
        if (isCancelled()) return;

        this.updateMessageLine("Importing description files...");
        snomed.importDescriptionFiles(inputFolder, ontology);
        this.updateProgress(6,10);
        if (isCancelled()) return;

        this.updateMessageLine("Importing relationships files...");
        snomed.importRelationshipFiles(inputFolder, entailmentType);
        this.updateProgress(8,10);
        if (isCancelled()) return;

        if (entailmentType!=null&entailmentType== EntailmentType.ASSERTED) {
            this.updateMessageLine("Importing MRCM Domain files...");
            snomed.importMRCMDomainFiles(inputFolder);
            this.updateProgress(8,10);
            if (isCancelled()) return;

            this.updateMessageLine("Importing MRCM range files...");
            snomed.importMRCMRangeFiles(inputFolder);
            this.updateProgress(9, 10);
            if (isCancelled()) return;
        }
        this.updateMessageLine("Saving UUID map...");
        snomed.saveUUIDMap(uuidFolder);


        Document document = new Document();
        document.setInformationModel(ontology);
        this.updateMessageLine("Exporting Discovery ontology files...");
        snomed.outputDocuments(document,outputFolder,
                RF2ToDiscovery.snomedDocument,
                entailmentType);
        this.updateProgress(10,10);
        if (isCancelled()) return;
    }

    private void importMCRM() throws Exception {
        DOWLManager dmanager = new DOWLManager();
        snomed= new RF2ToDiscovery();
        Ontology ontology = dmanager.createOntology(
                "http://www.DiscoveryDataService.org/InformationModel/SnomedMRCM");


        ontology.setDocumentInfo(
                new DocumentInfo().setDocumentIri("http://www.DiscoveryDataService.org/InformationModel/SnomedMRCM")
        );
        updateMessageLine("Validating source RF2 files...");
        snomed.validateFiles(inputFolder);

        this.updateMessageLine("Importing UUID map");
        snomed.importUUIDMap(uuidFolder);
        this.updateProgress(1,10);
        if (isCancelled()) return;


        this.updateMessageLine("Importing concept file...");
        snomed.importConceptFiles(inputFolder, ontology);
        this.updateProgress(3,10);
        if (isCancelled()) return;

        this.updateMessageLine("Importing refsets...");
        snomed.importRefsetFiles(inputFolder);
        this.updateProgress(4,10);
        if (isCancelled()) return;

        this.updateMessageLine("Importing description files...");
        snomed.importDescriptionFiles(inputFolder, ontology);
        this.updateProgress(6,10);
        if (isCancelled()) return;


        this.updateMessageLine("Importing MRCM Domain files...");
        snomed.importMRCMDomainFiles(inputFolder);
        this.updateProgress(8,10);
        if (isCancelled()) return;

        this.updateMessageLine("Importing MRCM range files...");
        snomed.importMRCMRangeFiles(inputFolder);

        this.updateProgress(9,10);
        if (isCancelled()) return;
        this.updateMessageLine("Saving UUID map...");
        snomed.saveUUIDMap(uuidFolder);

        Document document = new Document();
        document.setInformationModel(ontology);
       //Filters out everything but MRCM
        snomed.filterToMRCM(ontology);


        this.updateMessageLine("Exporting Discovery MRCM ontology file...");
        snomed.outputDocuments(document,outputFolder,
                RF2ToDiscovery.MRCMDocument,
                EntailmentType.ASSERTED);
        this.updateProgress(10,10);
        if (isCancelled()) return;
    }

    @Override
    protected Integer call() throws Exception {
        try {
            switch (importType) {
                case RF2_TO_DISCOVERY_FOLDER: {
                    importSnomed(EntailmentType.ASSERTED);
                    return 1;
                }

                case SNOMED_MRCM_TO_DISCOVERY: {
                    importMCRM();
                    return 1;
                }
                case RF2_TO_ISA_FOLDER: {
                    importSnomed(EntailmentType.INFERRED);
                    return 1;
                }
                default:
                    throw new Exception("Non supported import task type");

            }


            } catch(Exception e) {
            throw new Exception (e.getMessage());
        }


    }

    public String getInputFolder() {
        return inputFolder;
    }

    public ConversionType getImportType() {
        return importType;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public RF2ImportTask setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
        return this;
    }

    public RF2ImportTask setinputFolder(String inputFolder) {
        this.inputFolder = inputFolder;
        return this;
    }
    public RF2ImportTask setImportType(ConversionType importType) {
        this.importType = importType;
        return this;
    }


    public String getUuidFolder() {
        return uuidFolder;
    }

    public RF2ImportTask setUuidFolder(String uuidFolder) {
        this.uuidFolder = uuidFolder;
        return this;
    }
}


