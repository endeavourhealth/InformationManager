package org.endeavourhealth.informationmanager.transforms;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.DOWLManager;
import org.endeavourhealth.informationmanager.common.transform.model.*;

import java.beans.EventHandler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Task object to manage the import of the MRCM files fron Snomed into Discovery ontology
 * <p> bind the progressProperty to a progress monitory of some kind. If used in a thread May be cancelled
 * by the Task.cancel() method which is checked for periodically</p>
 * @author David Stables Endeavour
 */
public class MRCMImportTask extends Task {
    private final String inputFolder;
    private String outputFolder;


    public MRCMImportTask(String inputFolder, String outputFolder){
        this.inputFolder= inputFolder;
        this.outputFolder=outputFolder;
    }



    /**
     * Imports the Snomed MRCM files and creates and savesDiscovery ontology
     *
     * @throws IOException
     */
    public void loadAndSave() throws IOException {

        SnomedMRCM mrcm= new SnomedMRCM();
        //Validates input files
        mrcm.validatefiles(inputFolder);
        this.updateProgress(1,10);
        if (isCancelled()) return;

        //Obtains the Snomed ID-> Discovery UUI map if present
        mrcm.importUUIDMap(outputFolder);
        this.updateProgress(2,10);
        if (isCancelled()) return;

        //Gets concepts for reference
        mrcm.importConceptRefs(inputFolder);
        if (isCancelled()) return;

        //Imports refsets for internal integrity
        mrcm.importRefsetFiles(inputFolder);
        this.updateProgress(4,10);
        if (isCancelled()) return;

        //Imports descriptions just for names
        mrcm.importNames(inputFolder);
        this.updateProgress(6,10);
        if (isCancelled()) return;

        //Property Domains
        mrcm.importMRCMDomainFiles(inputFolder);
        this.updateProgress(7,10);
        if (isCancelled()) return;

        //Property Ranges
        mrcm.importMRCMRangeFiles(inputFolder);
        this.updateProgress(8,10);
        if (isCancelled()) return;

        //Saves the UUID Map
        mrcm.saveUUIDMap(outputFolder);
        this.updateProgress(9,10);
        if (isCancelled()) return;

        //Saves ontology
        mrcm.saveOntology(outputFolder);
        this.updateProgress(10,10);

    }




    @Override
    protected Integer call() throws Exception {
        loadAndSave();
        return 1;
    }

}


