USE im_next2;

ALTER TABLE concept
ADD FOREIGN KEY concept_namespace_fk (namespace) REFERENCES namespace(dbid),
ADD FOREIGN KEY concept_scheme_fk (scheme) REFERENCES concept(dbid),
ADD FOREIGN KEY concept_status_fk (status) REFERENCES concept_status(dbid);

ALTER TABLE concept_property_object
ADD FOREIGN KEY concept_property_object_concept_fk (concept) REFERENCES concept(dbid),
ADD FOREIGN KEY concept_property_object_property_fk (property) REFERENCES concept(dbid),
ADD FOREIGN KEY concept_property_object_object_fk (object) REFERENCES concept(dbid);

ALTER TABLE concept_property_data
ADD FOREIGN KEY concept_property_data_concept_fk (concept) REFERENCES concept(dbid),
ADD FOREIGN KEY concept_property_data_property_fk (property) REFERENCES concept(dbid);

ALTER TABLE concept_tct
ADD FOREIGN KEY concept_tct_source_fk (source) REFERENCES concept(dbid),
ADD FOREIGN KEY concept_tct_property_fk (property) REFERENCES concept(dbid),
ADD FOREIGN KEY concept_tct_target_fk (target) REFERENCES concept(dbid);

ALTER TABLE concept_term
ADD FOREIGN KEY concept_term_concept_fk (concept) REFERENCES concept(dbid);

ALTER TABLE concept_term_map
ADD FOREIGN KEY concept_term_map_target_fk (target) REFERENCES concept(dbid);
