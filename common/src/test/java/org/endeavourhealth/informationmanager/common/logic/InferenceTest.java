package org.endeavourhealth.informationmanager.common.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.model.ConceptNode;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;


class InferenceTest {

    @Test
    void execute() throws IOException, NoSuchAlgorithmException {
        ObjectMapper objectMapper = new ObjectMapper();
        Ontology discovery = objectMapper.readValue(new File("IMCoreFunc.json"), Ontology.class);
        Collection<ConceptNode> result = new Inference().execute(discovery);

        System.out.println("Final tree");
        System.out.println("--------------------------------------------------");
        printNodes(result);
        System.out.println("--------------------------------------------------");
        System.out.println("Done");
    }

    private void printNodes(Collection<ConceptNode> nodes) {
        printNodes(nodes, 0);
    }

    private void printNodes(Collection<ConceptNode> nodes, int level) {
        for(ConceptNode n: nodes) {
            System.out.print(String.join("", Collections.nCopies(level, "\t")));
            System.out.println(n.getId());
            printNodes(n.getChildren(), level + 1);
        }
    }
}
