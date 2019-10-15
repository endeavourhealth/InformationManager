-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/Discovery', '1.0.0');

SET @model = LAST_INSERT_ID();

-- Create core concepts
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id',CONCAT('DCE_', LOWER(REPLACE(term, ' ', '_'))),
        'name', term,
        'description', term)
FROM encounter_types;

INSERT INTO concept_definition (concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept')
            )
    )
FROM encounter_types e
JOIN concept c ON c.id = CONCAT('DCE_', LOWER(REPLACE(e.term, ' ', '_')));

UPDATE concept_definition cd
JOIN concept c ON c.dbid = cd.concept
JOIN encounter_subtypes s ON c.id = CONCAT('DCE_', LOWER(REPLACE(s.term, ' ', '_')))
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'concept', CONCAT('DCE_', LOWER(REPLACE(s.targetTerm, ' ', '_')))
                                              )
                                      )
                                      )
    )
;


-- Create local encounter concepts
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id',CONCAT('LENC_', LOWER(REPLACE(term, ' ', '_'))),
        'name', term,
        'description', term)
FROM encounter_types;

INSERT INTO concept_definition (concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept'),
                JSON_OBJECT('operator', 'AND',
                            'attribute',
                            JSON_OBJECT('property', 'mappedTo', 'valueConcept', JSON_OBJECT('concept', CONCAT('DCE_', LOWER(REPLACE(term, ' ', '_'))))
                                )
                    )
            )
    )
FROM encounter_types e
JOIN concept c ON c.id = CONCAT('LENC_', LOWER(REPLACE(e.term, ' ', '_')));

-- Create term maps
INSERT INTO concept_term_map
(term, type, target)
SELECT sourceTerm, typ.dbid, tgt.dbid
FROM encounter_maps m
JOIN concept typ ON typ.id = 'DCE_type_of_encounter'
JOIN concept tgt ON tgt.id = CONCAT('LENC_', LOWER(REPLACE(m.typeTerm, ' ', '_')))
;
