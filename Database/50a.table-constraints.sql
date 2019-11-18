ALTER TABLE concept
ADD FOREIGN KEY concept_model_fk (model) REFERENCES model(dbid);

ALTER TABLE document
ADD FOREIGN KEY document_model_fk (model) REFERENCES model (dbid),
ADD FOREIGN KEY document_status_fk (status) REFERENCES concept (dbid),
ADD FOREIGN KEY document_publisher_fk (publisher) REFERENCES concept(dbid);

ALTER TABLE concept_synonym
ADD FOREIGN KEY concept_synonym_fk (dbid) REFERENCES concept(dbid);

ALTER TABLE concept_definition_concept
ADD FOREIGN KEY concept_definition_concept_fk (concept) REFERENCES concept(dbid),
ADD FOREIGN KEY concept_definition_type_fk (type) REFERENCES concept_definition_type (dbid),
ADD FOREIGN KEY concept_definition_operator_kf (operator) REFERENCES operator(dbid),
ADD FOREIGN KEY concept_definition_concept_value_fk(concept_value) REFERENCES concept(dbid);

ALTER TABLE concept_definition_role_group
ADD FOREIGN KEY concept_definition_role_group_concept_kf (concept) REFERENCES concept(dbid),
ADD FOREIGN KEY concept_definition_role_group_type_fk (type) REFERENCES concept_definition_type(dbid),
ADD FOREIGN KEY concept_definition_role_group_operator_fk (operator) REFERENCES operator(dbid),
ADD FOREIGN KEY concept_definition_role_group_property_fk (property) REFERENCES concept(dbid),
ADD FOREIGN KEY concept_definition_role_group_value_concept_fk (value_concept) REFERENCES concept(dbid);

ALTER TABLE property_domain
ADD FOREIGN KEY property_domain_class_fk (class) REFERENCES concept (dbid),
ADD FOREIGN KEY property_domain_property_fk (property) REFERENCES concept (dbid),
ADD FOREIGN KEY property_domain_status_fk (status) REFERENCES concept (dbid);

ALTER TABLE property_range
ADD FOREIGN KEY property_range_class_fk (class) REFERENCES concept (dbid),
ADD FOREIGN KEY property_range_property_fk (property) REFERENCES concept (dbid),
ADD FOREIGN KEY property_range_status_fk (status) REFERENCES concept (dbid),
ADD FOREIGN KEY property_range_range_operator_fk (operator) REFERENCES operator(dbid),
ADD FOREIGN KEY property_range_range_type_fk (type) REFERENCES constraint_type(dbid),
ADD FOREIGN KEY property_range_range_concept_fk (concept) REFERENCES concept(dbid);
