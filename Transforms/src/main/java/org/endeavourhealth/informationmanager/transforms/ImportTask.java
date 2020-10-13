package org.endeavourhealth.informationmanager.transforms;

import javafx.concurrent.Task;
import org.endeavourhealth.informationmanager.common.transform.DOWLManager;
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
public class ImportTask extends Task {
    private final String inputFolder;
    private final ImportType importType;
    private String outputFolder;
    private String messageLines= "";
    private Snomed snomed;
    private String uuidFolder;

    /**
     * Initialize with input, output and type
     * @param inputFolder  root folder containing the source files e.g. RF2
     * @param outputFolder folder for the output ontology
     * @param importType  classication type e.g. SNOMED or MRCM
     */
    public ImportTask(String inputFolder, String outputFolder,
                      String uuidFolder,
                      ImportType importType){
        this.inputFolder= inputFolder;
        this.outputFolder=outputFolder;
        this.importType= importType;
        this.uuidFolder= uuidFolder;
    }



    /**
     * Imports the Snomed or Snomed MRCM files and creates and savesDiscovery ontology
     *
     * @throws IOException
     */
    public void loadAndSave() throws IOException {
        if (importType == ImportType.MRCM) {
            importMCRM();
        }
        else {
            importSnomed();
        }
    }

    private void updateMessageLine(String line){
        messageLines=messageLines+ line+ "\n";
        updateMessage(messageLines);
    }

    private void importSnomed() throws IOException {
        DOWLManager dmanager = new DOWLManager();
        snomed= new Snomed();
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
        snomed.importRelationshipFiles(inputFolder);
        this.updateProgress(8,10);
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
        this.updateMessageLine("Exporting Discovery ontology file...");
        snomed.outputDocuments(document,outputFolder, Snomed.snomedDocument);
        this.updateProgress(10,10);
        if (isCancelled()) return;
    }

    private void importMCRM() throws IOException{
        DOWLManager dmanager = new DOWLManager();
        snomed= new Snomed();
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
        snomed.outputDocuments(document,outputFolder, Snomed.MRCMDocument);
        this.updateProgress(10,10);
        if (isCancelled()) return;
    }

    @Override
    protected Integer call() throws Exception {

        loadAndSave();
        return 1;
    }

}


