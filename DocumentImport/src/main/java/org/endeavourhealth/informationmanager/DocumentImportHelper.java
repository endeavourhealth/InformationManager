package org.endeavourhealth.informationmanager;

import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.*;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;
import org.json.JSONArray;

public class DocumentImportHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentImport.class);

    /**
     *
     * @param informationModel
     * @throws Exception
     */
    public void populateNamespaces(JSONObject informationModel) throws Exception {
        try (InformationManagerDAL informationManagerDAL = new InformationManagerJDBCDAL()) {
            //Saving to namespace table
            JSONArray namespaces = informationModel.has("Namespace") ? (JSONArray) informationModel.get("Namespace") : null;
            if(namespaces != null) {
                namespaces.forEach(item -> {
                    JSONObject namespaceObj = (JSONObject) item;
                    String prefix = namespaceObj.has("prefix") ? (String) namespaceObj.get("prefix") : "";
                    String iri = namespaceObj.has("iri") ? (String) namespaceObj.get("iri") : "";

                    Namespace namespace = new Namespace();
                    namespace.setPrefix(prefix);
                    namespace.setIri(iri);

                    try {
                        informationManagerDAL.insertNamespace(namespace);
                    } catch (Exception e) {
                        LOG.error("Error while inserting namespace into database");
                    }
                });
            }
            //Saving to namespace table
        }
    }

    /**
     *
     * @param informationModel
     * @throws Exception
     */
    public void populateConcepts(JSONObject informationModel) throws Exception {
        try (InformationManagerDAL informationManagerDAL = new InformationManagerJDBCDAL()) {
            //Saving to concept table
            JSONArray concepts = informationModel.has("Concept") ? (JSONArray) informationModel.get("Concept") : null;
            if(concepts != null) {
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
            }
            //Saving to concept table
        }
    }

    /**
     *
     * @param informationModel
     * @throws Exception
     */
    public void populateConceptAxioms(JSONObject informationModel) throws Exception {
        try (InformationManagerDAL informationManagerDAL = new InformationManagerJDBCDAL()) {
            JSONArray conceptAxioms = informationModel.has("ConceptAxioms") ? (JSONArray) informationModel.get("ConceptAxioms") : null;
            if(conceptAxioms != null) {
                conceptAxioms.forEach(item -> {
                    JSONObject conceptAxiom = (JSONObject) item;
                    String concept = conceptAxiom.has("Concept") ? (String) conceptAxiom.get("Concept") : null;

                    //Condition to invoke method to set property_class and property_data values based on SubClass/Equivalent
                    if(conceptAxiom.has("SubClassOf"))
                        populatePropertyClassData(informationManagerDAL, concept, conceptAxiom, "SubClassOf");
                    else if(conceptAxiom.has("EquivalentTo"))
                        populatePropertyClassData(informationManagerDAL, concept, conceptAxiom, "EquivalentTo");

                    //Saving to inverse_property table
                    JSONObject inversePropertyJSON = conceptAxiom.has("InversePropertyOf") ? (JSONObject) conceptAxiom.get("InversePropertyOf") : null;
                    if (inversePropertyJSON != null) {
                        try {
                            InverseProperty inverseProperty = new InverseProperty();
                            inverseProperty.setConcept(getConceptId((String) conceptAxiom.get("Concept")));
                            inverseProperty.setAxiom(getAxiomId("InversePropertyOf"));
                            inverseProperty.setInverse(getConceptId((String) inversePropertyJSON.get("Concept")));

                            informationManagerDAL.insertInverseProperty(inverseProperty);
                        } catch (Exception e) {
                            LOG.error("Unable to fetch Concept Id/Axiom Id for the given Concept Iri/Token OR Error in saving inverse_property to DB");
                        }
                    }
                    //Saving to inverse_property table

                    //Saving to property_domain table
                    JSONObject propertyDomainJSON = conceptAxiom.has("PropertyDomain") ? (JSONObject) conceptAxiom.get("PropertyDomain") : null;
                    if (propertyDomainJSON != null) {
                        try {
                            PropertyDomain propertyDomain = new PropertyDomain();
                            propertyDomain.setConcept(getConceptId(conceptAxiom.getString("Concept")));
                            propertyDomain.setAxiom(getAxiomId("PropertyDomain"));
                            propertyDomain.setOperator(getOperatorId(propertyDomainJSON.getString("Operator")));

                            JSONArray domains = propertyDomainJSON.has("Domain") ? (JSONArray) propertyDomainJSON.get("Domain") : null;
                            if(domains != null) {
                                for (Object domain : domains) {
                                    try {
                                        JSONObject domainJSON = new JSONObject(domain.toString());
                                        propertyDomain.setDomain(String.valueOf(getConceptId(domainJSON.getString("Concept"))));
                                        propertyDomain.setInGroup((Integer) domainJSON.get("inGroup"));
                                        propertyDomain.setMinCardinality(domainJSON.has("MinCardinality") ? (Integer) domainJSON.get("MinCardinality") : null);
                                        propertyDomain.setMaxCardinality(domainJSON.has("MaxCardinality") ? (Integer) domainJSON.get("MaxCardinality") : null);
                                        propertyDomain.setMinInGroup(domainJSON.has("minInGroup") ? (Integer) domainJSON.get("minInGroup") : null);
                                        propertyDomain.setMaxInGroup(domainJSON.has("maxInGroup") ? (Integer) domainJSON.get("maxInGroup") : null);

                                        informationManagerDAL.insertPropertyDomain(propertyDomain);
                                    } catch (Exception e) {
                                        LOG.error("Unable to fetch Concept Id/Axiom Id for the given Concept Iri/Token OR Error in saving property_domain to DB");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            LOG.error("Unable to fetch Concept Id/Axiom Id for the given Concept Iri/Token");
                        }
                    }
                    //Saving to property_domain table

                    //Saving to subtype table for subPropertyOf Axiom
                    JSONObject subPropertyJSON = conceptAxiom.has("SubPropertyOf") ? (JSONObject) conceptAxiom.get("SubPropertyOf") : null;
                    if (subPropertyJSON != null) {
                        JSONArray superConcepts = subPropertyJSON.has("Concept") ? (JSONArray) subPropertyJSON.get("Concept") : null;
                        if (superConcepts != null) {
                            for (Object superConcept : superConcepts) {
                                SubType subType = new SubType();
                                try {
                                    subType.setConcept(getConceptId(concept));
                                    subType.setAxiom(getAxiomId("SubPropertyOf"));
                                    subType.setSuperType(getConceptId((String) superConcept));

                                    informationManagerDAL.createSubType(subType);
                                } catch (Exception e) {
                                    LOG.error("Unable to fetch Concept Id/Axiom Id for the given Concept Iri/Token OR Error in saving SubType to DB");
                                }
                            }
                        }
                    }
                    //Saving to subtype table for subPropertyOf Axiom

                    //Saving to property_range table
                    JSONObject propertyRangeJSON = conceptAxiom.has("PropertyRange") ? (JSONObject) conceptAxiom.get("PropertyRange") : null;
                    if (propertyRangeJSON != null) {
                        JSONArray proRangeConcepts = propertyRangeJSON.has("Concept") ? (JSONArray) propertyRangeJSON.get("Concept") : null;
                        if (proRangeConcepts != null) {
                            for (Object proRangeConcept : proRangeConcepts) {
                                JSONObject proRangeConceptJSON = new JSONObject(proRangeConcept.toString());
                                PropertyRange propertyRange = new PropertyRange();
                                try {
                                    propertyRange.setConcept(getConceptId(concept));
                                    propertyRange.setAxiom(getAxiomId("PropertyRange"));
                                    propertyRange.setRange(String.valueOf(getConceptId(proRangeConceptJSON.has("Concept") ? proRangeConceptJSON.getString("Concept") : null)));
                                    propertyRange.setSubsumption(proRangeConceptJSON.has("Subsumption") ? proRangeConceptJSON.getString("Subsumption") : null);
                                    informationManagerDAL.insertPropertyRange(propertyRange);

                                } catch (Exception e) {
                                    LOG.error("Unable to fetch Concept Id/Axiom Id for the given Concept Iri/Token OR Error in saving PropertyRange to DB");
                                }
                            }
                        }
                    }
                    //Saving to property_range table

                    //Saving to property_transitive table
                    boolean transitiveStatus = conceptAxiom.has("IsTransitive") ? (boolean) conceptAxiom.get("IsTransitive") : false;
                    if (transitiveStatus) {
                        PropertyTransitive propertyTransitive = new PropertyTransitive();
                        try {
                            propertyTransitive.setConcept(getConceptId(concept));
                            informationManagerDAL.insertPropertyTransitive(propertyTransitive);
                        } catch (Exception e) {
                            LOG.error("Unable to fetch Concept Id for the given Concept Iri OR Error in saving propertyTransitive to DB");
                        }
                    }
                    //Saving to property_transitive table
                });
            }
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

    /**
     * Method to set property_class and property_data values based on SubClass/Equivalent
     * @param axiomKey
     */
    private void populatePropertyClassData(InformationManagerDAL informationManagerDAL, String concept, JSONObject conceptAxiom, String axiomKey) {
        JSONObject subClassOEquivalentJSON = (JSONObject) conceptAxiom.get(axiomKey);
        if (subClassOEquivalentJSON != null) {
            //Saving to subtype table
            String operator = subClassOEquivalentJSON.has("Operator") ? (String) subClassOEquivalentJSON.get("Operator") : null;
            JSONArray superConcepts = subClassOEquivalentJSON.has("Concept") ? (JSONArray) subClassOEquivalentJSON.get("Concept") : null;
            if (superConcepts != null) {
                try {
                    for (Object superConcept : superConcepts) {
                        SubType subType = new SubType();
                        subType.setConcept(getConceptId(concept));
                        subType.setAxiom(getAxiomId(axiomKey));
                        subType.setSuperType(getConceptId((String) superConcept));
                        subType.setOperator(getOperatorId(operator));

                        informationManagerDAL.createSubType(subType);
                    }
                    informationManagerDAL.updateConceptStatus();
                } catch (Exception e) {
                    LOG.error("Unable to fetch Concept Id/Axiom Id for the given Concept Iri/Token OR Error in saving SubType to DB");
                }
            }
            //Saving to subtype table

            //Saving to property_class & property_data table
            JSONArray roleGroups = subClassOEquivalentJSON.has("RoleGroup") ? (JSONArray) subClassOEquivalentJSON.get("RoleGroup") : null;
            if (roleGroups != null) {
                for (Object roleGroup : roleGroups) {
                    JSONObject roleGroupJSON = new JSONObject(roleGroup.toString());

                    JSONArray roles = roleGroupJSON.has("Role") ? (JSONArray) roleGroupJSON.get("Role") : null;
                    if (roles != null) {
                        for (Object role : roles) {
                            JSONObject roleJSON = new JSONObject(role.toString());

                            //Saving to property_class table
                            JSONObject valueClassJSON = roleJSON.has("ValueClass") ? (JSONObject) roleJSON.get("ValueClass") : null;
                            if (valueClassJSON != null) {
                                PropertyClass propertyClass = new PropertyClass();
                                try {
                                    propertyClass.setConcept(getConceptId(concept));
                                    if (roleGroupJSON.has("Operator"))
                                        propertyClass.setOperator(getOperatorId(roleGroupJSON.getString("Operator")));

                                    if (roleJSON.has("groupNumber"))
                                        propertyClass.setGroup((Integer) roleJSON.get("groupNumber"));

                                    if (roleJSON.has("Property"))
                                        propertyClass.setProperty(getConceptId((String) roleJSON.get("Property")));

                                    propertyClass.setMinCardinality(roleJSON.has("MinCardinality") ? (Integer) roleJSON.get("MinCardinality") : null);
                                    propertyClass.setMaxCardinality(roleJSON.has("MaxCardinality") ? (Integer) roleJSON.get("MaxCardinality") : null);
                                    propertyClass.setAxiom(getAxiomId(axiomKey));

                                    JSONArray objects = valueClassJSON.has("Concept") ? (JSONArray) valueClassJSON.get("Concept") : null;
                                    if (objects != null) {
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
                            if (valueData != null) {
                                PropertyData propertyData = new PropertyData();
                                try {
                                    propertyData.setConcept(getConceptId(concept));
                                    if (roleGroupJSON.has("Operator"))
                                        propertyData.setOperator(getOperatorId(roleGroupJSON.getString("Operator")));

                                    if (roleJSON.has("groupNumber"))
                                        propertyData.setGroup((Integer) roleJSON.get("groupNumber"));

                                    if (roleJSON.has("Property"))
                                        propertyData.setProperty(getConceptId((String) roleJSON.get("Property")));

                                    propertyData.setMinCardinality(roleJSON.has("MinCardinality") ? (Integer) roleJSON.get("MinCardinality") : null);
                                    propertyData.setMaxCardinality(roleJSON.has("MaxCardinality") ? (Integer) roleJSON.get("MaxCardinality") : null);
                                    propertyData.setAxiom(getAxiomId(axiomKey));
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
    }

}
