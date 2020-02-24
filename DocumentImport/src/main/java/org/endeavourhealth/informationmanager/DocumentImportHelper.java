package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.informationmanager.models.Document;
import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;
import org.json.JSONArray;

public class DocumentImportHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentImport.class);

    /**
     *
     * @param informationModel
     */
    public void populateConcepts(JSONObject informationModel) throws Exception {
        try (InformationManagerDAL informationManagerDAL = new InformationManagerJDBCDAL()) {
            //Saving to concept table
            JSONArray concepts = informationModel.has("Concept") ? (JSONArray) informationModel.get("Concept") : null;
            concepts.forEach(item -> {
                JSONObject concept = (JSONObject) item;
                String status = concept.has("status") ? (String) concept.get("status") : "";
                String iri = concept.has("iri") ? (String) concept.get("iri") : "";
                String name = concept.has("name") ? (String) concept.get("name") : "";
                String description = concept.has("description") ? (String) concept.get("description") : "";
                String code = concept.has("code") ? (String) concept.get("code") : "";
                String version = concept.has("version") ? (String) concept.get("version") : "";

                Concept conceptObj = new Concept();
                conceptObj.setName(name);
                conceptObj.setCode(code);
                conceptObj.setDescription(description);
                conceptObj.setIri(iri);
                conceptObj.setStatus(status);

                try {
                    informationManagerDAL.insertConcept(conceptObj);
                } catch (Exception e) {
                    LOG.error("Error while inserting concept into database");
                }
            });
            //Saving to concept table
        }
    }

    /**
     *
     * @param informationModel
     */
    public void populateConceptAxioms(JSONObject informationModel) throws Exception {
        try (InformationManagerDAL informationManagerDAL = new InformationManagerJDBCDAL()) {
            JSONArray conceptAxioms = informationModel.has("ConceptAxioms") ? (JSONArray) informationModel.get("ConceptAxioms") : null;
            conceptAxioms.forEach(item -> {
                JSONObject conceptAxiom = (JSONObject) item;
                String concept = conceptAxiom.has("Concept") ? (String) conceptAxiom.get("Concept") : null;
                JSONObject subClassJSON = conceptAxiom.has("SubClassOf") ? (JSONObject) conceptAxiom.get("SubClassOf") : null;

                if(subClassJSON != null) {
                    //Saving to subtype table
                    String operator = subClassJSON.has("Operator") ? (String) subClassJSON.get("Operator") : null;
                    JSONArray superConcepts = subClassJSON.has("Concept") ? (JSONArray) subClassJSON.get("Concept") : null;
                    if(superConcepts != null) {
                        for (Object superConcept : superConcepts) {
                            SubType subType = new SubType();
                            try {
                                subType.setConcept(getConceptId(concept));
                                subType.setAxiom(getAxiomId("SubClassOf"));
                                subType.setSuperType(getConceptId((String) superConcept));
                                subType.setOperator(getOperatorId(operator));

                                informationManagerDAL.createSubType(subType);

                            } catch (Exception e) {
                                LOG.error("Unable to fetch Concept Id/Axiom Id for the given Concept Iri/Token OR Error in saving SubType to DB");
                            }
                        }
                    }
                    //Saving to subtype table

                    //Saving to property_class & property_data table
                    JSONArray roleGroups = subClassJSON.has("RoleGroup") ? (JSONArray) subClassJSON.get("RoleGroup") : null;
                    if(roleGroups != null) {
                        for (Object roleGroup : roleGroups) {
                            JSONObject roleGroupJSON = new JSONObject(roleGroup.toString());

                            JSONArray roles = roleGroupJSON.has("Role") ? (JSONArray) roleGroupJSON.get("Role") : null;
                            if(roles != null) {
                                for (Object role : roles) {
                                    JSONObject roleJSON = new JSONObject(role.toString());

                                    //Saving to property_class table
                                    JSONObject valueClassJSON = roleJSON.has("ValueClass") ? (JSONObject) roleJSON.get("ValueClass") : null;
                                    if(valueClassJSON != null) {
                                        PropertyClass propertyClass = new PropertyClass();
                                        try {
                                            propertyClass.setConcept(getConceptId(concept));
                                            if(roleGroupJSON.has("Operator"))
                                                propertyClass.setOperator(getOperatorId(roleGroupJSON.getString("Operator")));

                                            if(roleJSON.has("groupNumber"))
                                                propertyClass.setGroup((Integer) roleJSON.get("groupNumber"));

                                            if(roleJSON.has("Property"))
                                                propertyClass.setProperty(getConceptId((String) roleJSON.get("Property")));

                                            propertyClass.setMinCardinality(roleJSON.has("MinCardinality") ? (Integer) roleJSON.get("MinCardinality") : null);
                                            propertyClass.setMaxCardinality(roleJSON.has("MaxCardinality") ? (Integer) roleJSON.get("MaxCardinality") : null);
                                            propertyClass.setAxiom(getAxiomId("SubClassOf"));

                                            JSONArray objects = valueClassJSON.has("Concept") ? (JSONArray) valueClassJSON.get("Concept") : null;
                                            if(objects != null) {
                                                for (Object object : objects) {
                                                    propertyClass.setObject(getConceptId((String) object));

                                                    informationManagerDAL.insertPropertyClass(propertyClass);
                                                }
                                            }

                                        } catch (Exception e) {
                                            LOG.error("Unable to fetch Concept Id/Axiom Id for the given Concept Iri/Token OR Error in saving property_class to DB");
                                        }
                                    }
                                    //Saving to property_class table

                                    //Saving to property_data table
                                    Integer valueData = roleJSON.has("ValueData") ? (Integer) roleJSON.get("ValueData") : null;
                                    if(valueData != null) {
                                        PropertyData propertyData = new PropertyData();
                                        try {
                                            propertyData.setConcept(getConceptId(concept));
                                            if(roleGroupJSON.has("Operator"))
                                                propertyData.setOperator(getOperatorId(roleGroupJSON.getString("Operator")));

                                            if(roleJSON.has("groupNumber"))
                                                propertyData.setGroup((Integer) roleJSON.get("groupNumber"));

                                            if(roleJSON.has("Property"))
                                                propertyData.setProperty(getConceptId((String) roleJSON.get("Property")));

                                            propertyData.setMinCardinality(roleJSON.has("MinCardinality") ? (Integer) roleJSON.get("MinCardinality") : null);
                                            propertyData.setMaxCardinality(roleJSON.has("MaxCardinality") ? (Integer) roleJSON.get("MaxCardinality") : null);
                                            propertyData.setAxiom(getAxiomId("SubClassOf"));
                                            propertyData.setData(String.valueOf(valueData));

                                            informationManagerDAL.insertPropertyData(propertyData);

                                        } catch (Exception e) {
                                            LOG.error("Unable to fetch Concept Id/Axiom Id for the given Concept Iri/Token OR Error in saving property_class to DB");
                                        }
                                    }
                                    //Saving to property_data table
                                }
                            }
                        }
                    }
                    //Saving to property_class & property_data table
                }
            });
        }
    }

    /**
     *
     * @param concept
     * @return
     * @throws Exception
     */
    private Integer getConceptId(String concept) throws Exception {
        try (InformationManagerDAL informationManagerDAL = new InformationManagerJDBCDAL()) {
            return informationManagerDAL.getConceptId(concept);
        }
    }

    /**
     *
     * @param token
     * @return
     * @throws Exception
     */
    private Integer getAxiomId(String token) throws Exception {
        try (InformationManagerDAL informationManagerDAL = new InformationManagerJDBCDAL()) {
            return informationManagerDAL.getAxiomId(token);
        }
    }

    /**
     *
     * @param operator
     * @return
     * @throws Exception
     */
    private Integer getOperatorId(String operator) throws Exception {
        try (InformationManagerDAL informationManagerDAL = new InformationManagerJDBCDAL()) {
            return informationManagerDAL.getOperatorId(operator);
        }
    }

}
