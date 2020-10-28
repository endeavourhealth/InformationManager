package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DOWLManagerTest {

    @Test
    void loadAndSaveSimpleInferred() throws OWLOntologyCreationException, FileFormatException, IOException {
        DOWLManager manager= new DOWLManager();
        manager.loadAndSaveSimpleInferred(new File("c:/msm/shared/family.json"),null);
    }
}