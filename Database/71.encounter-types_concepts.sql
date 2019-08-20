-- Useful/common concepts
SELECT @subtype := dbid FROM concept WHERE id = 'is_subtype_of';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @equiv := dbid FROM concept WHERE id = 'is_equivalent_to';
SELECT @parent := dbid FROM concept WHERE id = 'has_parent';        -- TODO: Migrate to "Is_a"?

-- Create document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/Discovery', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- Create core concepts
INSERT INTO concept (document, id, name, description)
SELECT @doc, CONCAT('DCE_', LOWER(REPLACE(term, ' ', '_'))), term, term
FROM encounter_types;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM encounter_types e
JOIN concept c ON c.id = CONCAT('DCE_', LOWER(REPLACE(e.term, ' ', '_')));

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @parent, v.dbid
FROM encounter_subtypes s
JOIN concept c ON c.id = CONCAT('LENC_', LOWER(REPLACE(s.term, ' ', '_')))
JOIN concept v ON v.id = CONCAT('LENC_', LOWER(REPLACE(s.targetTerm, ' ', '_')));


-- Create local encounter concepts
INSERT INTO concept (document, id, name, description)
SELECT @doc, CONCAT('LENC_', LOWER(REPLACE(term, ' ', '_'))), term, term
FROM encounter_types;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM encounter_types e
JOIN concept c ON c.id = CONCAT('LENC_', LOWER(REPLACE(e.term, ' ', '_')));

-- Create equivalence
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @equiv, v.dbid
FROM encounter_types e
JOIN concept c ON c.id = CONCAT('LENC_', LOWER(REPLACE(term, ' ', '_')))
JOIN concept v ON v.id = CONCAT('DCE_', LOWER(REPLACE(term, ' ', '_')));

-- Create term maps
INSERT INTO concept_term_map
(term, type, target)
SELECT sourceTerm, typ.dbid, tgt.dbid
FROM encounter_maps m
JOIN concept typ ON typ.id = 'DCE_type_of_encounter'
JOIN concept tgt ON tgt.id = CONCAT('LENC_', LOWER(REPLACE(m.typeTerm, ' ', '_')))
;
