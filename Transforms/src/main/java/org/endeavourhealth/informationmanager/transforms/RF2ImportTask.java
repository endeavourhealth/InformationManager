package org.endeavourhealth.informationmanager.transforms;

import javafx.concurrent.Task;
import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.informationmanager.common.transform.model.Document;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;

import java.util.UUID;

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



    private void updateMessageLine(String line){
        messageLines=messageLines+ line+ "\n";
        updateMessage(messageLines);
    }

    private void importSnomedAsFolder(EntailmentType entailmentType) throws Exception {
        Ontology ontology= importSnomed(entailmentType);
        Document document = new Document();
        document.setInformationModel(ontology);
        this.updateMessageLine("Exporting Discovery ontology files...");
        RF2ToDiscovery.outputDocuments(document,outputFolder,
                RF2ToDiscovery.snomedDocument,
                entailmentType);
        this.updateProgress(10,10);
        if (isCancelled()) return;
    }

    private void importSnomedAsFile(EntailmentType entailmentType) throws Exception {
        Ontology ontology= importSnomed(entailmentType);
        Document document = new Document();
        document.setInformationModel(ontology);
        this.updateMessageLine("Exporting Discovery isa file...");
        RF2ToDiscovery.outputDocument(document,outputFolder+"\\Snomed-Inferred.json");
        this.updateProgress(10,10);
        if (isCancelled()) return;
    }
    private Ontology importSnomed(EntailmentType entailmentType) throws Exception {
        Ontology ontology = DOWLManager.createOntology(
            OntologyIri.DISCOVERY.getValue(),
            OntologyModuleIri.COMMON_CONCEPTS.getValue()
        );

        updateMessageLine("Validating source RF2 files...");
        RF2ToDiscovery.validateFiles(inputFolder);


        this.updateProgress(1,10);

        if (isCancelled()) return null;

        this.updateProgress(2,10);
        if (isCancelled()) return null;

        this.updateMessageLine("Importing concept file...");
        RF2ToDiscovery.importConceptFiles(inputFolder, ontology);
        this.updateProgress(3,10);

        if (isCancelled()) return null;

        this.updateMessageLine("Importing refsets...");
        RF2ToDiscovery.importRefsetFiles(inputFolder);
        this.updateProgress(4,10);
        if (isCancelled()) return null;

        this.updateMessageLine("Importing description files...");
        RF2ToDiscovery.importDescriptionFiles(inputFolder, ontology);
        this.updateProgress(6,10);
        if (isCancelled()) return null;

        this.updateMessageLine("Importing relationships files...");
        RF2ToDiscovery.importRelationshipFiles(inputFolder, entailmentType);
        this.updateProgress(8,10);
        if (isCancelled()) return null;

        if (entailmentType!=null&entailmentType== EntailmentType.ASSERTED) {
            this.updateMessageLine("Importing MRCM Domain files...");
            RF2ToDiscovery.importMRCMDomainFiles(inputFolder);
            this.updateProgress(8,10);
            if (isCancelled()) return null;

            this.updateMessageLine("Importing MRCM range files...");
            RF2ToDiscovery.importMRCMRangeFiles(inputFolder);
            this.updateProgress(9, 10);
            if (isCancelled()) return null;
        }
        this.updateMessageLine("Saving UUID map...");


        return ontology;
    }

    private void importMCRM() throws Exception {
        Ontology ontology = DOWLManager.createOntology(
            OntologyIri.DISCOVERY.getValue(),
            OntologyModuleIri.SNOMED.getValue()
        );
        updateMessageLine("Validating source RF2 files...");
        RF2ToDiscovery.validateFiles(inputFolder);

        this.updateMessageLine("Importing UUID map");

        this.updateProgress(1,10);
        if (isCancelled()) return;


        this.updateMessageLine("Importing concept file...");
        RF2ToDiscovery.importConceptFiles(inputFolder, ontology);
        this.updateProgress(3,10);
        if (isCancelled()) return;

        this.updateMessageLine("Importing refsets...");
        RF2ToDiscovery.importRefsetFiles(inputFolder);
        this.updateProgress(4,10);
        if (isCancelled()) return;

        this.updateMessageLine("Importing description files...");
        RF2ToDiscovery.importDescriptionFiles(inputFolder, ontology);
        this.updateProgress(6,10);
        if (isCancelled()) return;


        this.updateMessageLine("Importing MRCM Domain files...");
        RF2ToDiscovery.importMRCMDomainFiles(inputFolder);
        this.updateProgress(8,10);
        if (isCancelled()) return;

        this.updateMessageLine("Importing MRCM range files...");
        RF2ToDiscovery.importMRCMRangeFiles(inputFolder);

        this.updateProgress(9,10);
        if (isCancelled()) return;
        this.updateMessageLine("Filtering to MRCM...");
        ontology = RF2ToDiscovery.filterToMRCM(ontology);

        Document document = new Document();
        document.setInformationModel(ontology);


        this.updateMessageLine("Exporting Discovery MRCM ontology file...");
        RF2ToDiscovery.outputDocuments(document,outputFolder,
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
                    importSnomedAsFolder(EntailmentType.ASSERTED);
                    return 1;
                }
                case RF2_TO_DISCOVERY_FILE: {
                    importSnomedAsFile(EntailmentType.ASSERTED);
                    return 1;
                }
                case RF2_TO_ISA_FILE: {
                    importSnomedAsFile(EntailmentType.INFERRED);
                    return 1;
                }

                case SNOMED_MRCM_TO_DISCOVERY: {
                    importMCRM();
                    return 1;
                }
                case RF2_TO_ISA_FOLDER: {
                    importSnomedAsFolder(EntailmentType.INFERRED);
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


}


