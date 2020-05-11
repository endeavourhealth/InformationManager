package org.endeavourhealth.informationmanager.common.dal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.common.config.ConfigManagerException;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

class IMOWLDALTest {
/*
    @Test
    void save() throws IOException, SQLException, ConfigManagerException {
        ConfigManager.Initialize("information-manager");
        ObjectMapper objectMapper = new ObjectMapper();
        Ontology discovery = objectMapper.readValue(new File("IMCoreFunc.json"), Ontology.class);

        new IMOWLDAL().save(discovery);
    }

    @Test
    void load() throws ConfigManagerException, IOException, SQLException {
        ConfigManager.Initialize("information-manager");
        Ontology ontology = new IMOWLDAL().load("http://www.DiscoveryDataService.org/InformationModel/DiscoveryCore#");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ontology);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("IMCoreFuncDB.json"))) {
            writer.write(json);
        }
    }*/
}
