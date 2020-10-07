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

public class MRCMImportTask extends Task {
    private final String inputFolder;
    private String outputFolder;


    public MRCMImportTask(String inputFolder, String outputFolder){
        this.inputFolder= inputFolder;
        this.outputFolder=outputFolder;
    }



    /**
     * Imports the Snomed MRCM files and creates Discovery ontology
     *
     * @param inputFolder  folder containing the International and UK RF2 releases
     * @param outputFolder Folder to contain the MRCM ontology. The output folder should also contain the UUI mapping file
     * @return Returns an ontology for further use
     * @throws IOException
     */
    public void loadAndSave() throws IOException {

        SnomedMRCM mrcm= new SnomedMRCM();
        //Validates input files
        mrcm.validatefiles(inputFolder);
        this.updateProgress(1,10);

        //Obtains the Snomed ID-> Discovery UUI map if present
        mrcm.importUUIDMap(outputFolder);
        this.updateProgress(2,10);

        //Gets concepts for reference
        mrcm.importConceptRefs(inputFolder);

        //Imports refsets for internal integrity
        mrcm.importRefsetFiles(inputFolder);
        this.updateProgress(4,10);

        //Imports descriptions just for names
        mrcm.importNames(inputFolder);
        this.updateProgress(6,10);

        //Property Domains
        mrcm.importMRCMDomainFiles(inputFolder);
        this.updateProgress(7,10);

        //Property Ranges
        mrcm.importMRCMRangeFiles(inputFolder);
        this.updateProgress(8,10);

        //Saves the UUID Map
        mrcm.saveUUIDMap(outputFolder);
        this.updateProgress(9,10);

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


