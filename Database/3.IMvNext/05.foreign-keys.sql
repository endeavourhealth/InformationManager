USE im_next;

ALTER TABLE concept
ADD FOREIGN KEY concept_namespace_fk (namespace) REFERENCES namespace(id),
ADD FOREIGN KEY concept_scheme_fk (scheme) REFERENCES concept(id),
ADD FOREIGN KEY concept_status_fk (status) REFERENCES concept_status(id);

ALTER TABLE concept_property_object
ADD FOREIGN KEY concept_property_object_concept_fk (concept) REFERENCES concept(id),
ADD FOREIGN KEY concept_property_object_property_fk (property) REFERENCES concept(id),
ADD FOREIGN KEY concept_property_object_object_fk (object) REFERENCES concept(id);

ALTER TABLE concept_property_data
ADD FOREIGN KEY concept_property_data_concept_fk (concept) REFERENCES concept(id),
ADD FOREIGN KEY concept_property_data_property_fk (property) REFERENCES concept(id);

ALTER TABLE concept_tct
ADD FOREIGN KEY concept_tct_source_fk (source) REFERENCES concept(id),
ADD FOREIGN KEY concept_tct_property_fk (property) REFERENCES concept(id),
ADD FOREIGN KEY concept_tct_target_fk (target) REFERENCES concept(id);

ALTER TABLE concept_term
ADD FOREIGN KEY concept_term_concept_fk (concept) REFERENCES concept(id);

ALTER TABLE concept_term_map
ADD FOREIGN KEY concept_term_map_target_fk (target) REFERENCES concept(id);

ALTER TABLE value_set
ADD FOREIGN KEY value_set_concept_fk (concept) REFERENCES concept(id);

ALTER TABLE data_model_attribute
ADD FOREIGN KEY data_model_attribute_id_fk (id) REFERENCES concept(id),
ADD FOREIGN KEY data_model_attribute_attribute_fk (attribute) REFERENCES concept(id);
