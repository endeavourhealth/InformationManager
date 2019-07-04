-- Create document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/Discovery', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- Create core concepts
INSERT INTO concept
(document, data)
SELECT @doc,
       JSON_OBJECT(
           'id', CONCAT('DCE_', LOWER(REPLACE(term, ' ', '_'))),
           'name', term,
           'description', term,
           'is_subtype_of', JSON_OBJECT('id', 'CodeableConcept')
           ) as data
FROM encounter_types;

-- Create local encounter concepts
INSERT INTO concept
(document, data)
SELECT @doc,
       JSON_OBJECT(
               'id', CONCAT('LENC_', LOWER(REPLACE(term, ' ', '_'))),
               'name', term,
               'description', term,
               'is_subtype_of', JSON_OBJECT('id', 'CodeableConcept'),
               'is_equivalent_to', JSON_OBJECT('id', CONCAT('DCE_', LOWER(REPLACE(term, ' ', '_'))))
           ) as data
FROM encounter_types;

INSERT INTO concept_term_map
(term, type, target)
SELECT sourceTerm, typ.dbid, tgt.dbid
FROM encounter_maps m
JOIN concept typ ON typ.id = 'DCE_type_of_encounter'
JOIN concept tgt ON tgt.id = CONCAT('LENC_', LOWER(REPLACE(m.typeTerm, ' ', '_')))
;
