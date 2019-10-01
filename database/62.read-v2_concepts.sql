-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/READ2', '1.0.0');

SET @model = LAST_INSERT_ID();

INSERT INTO concept(model, data)
VALUES
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'READ2', 'name', 'READ 2', 'description', 'The READ2 code scheme'));

SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_definition (concept, data)
VALUES (@scheme, JSON_OBJECT(
        'status', 'CoreActive',
        'definitionOf', 'READ2',
        'subTypeOf', JSON_OBJECT('concept', JSON_ARRAY('CodeScheme'))
    ));

-- CONCEPTS
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('R2_', code),
        'name', if(length(term) > 255, concat(left(term, 252), '...'), term),
        'description', term,
        'codeScheme', 'READ2',
        'code', code
    )
FROM read_v2;

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'definitionOf', id,
        'subTypeOf', JSON_ARRAY(
            JSON_OBJECT('concept', 'CodeableConcept'),
            JSON_OBJECT('operator', 'AND', 'concept', concat('R2_', INSERT(v.code, IF(INSTR(v.code, '.') = 0, 5, INSTR(v.code, '.')-1), 1, '.')))
            )
    )
FROM read_v2 v
JOIN concept c ON c.id = concat('R2_', v.code);
