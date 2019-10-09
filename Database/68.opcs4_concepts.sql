-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/OPCS4', '1.0.0');
SET @model = LAST_INSERT_ID();

-- Code scheme
INSERT INTO concept(model, data)
VALUES
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'OPCS4', 'name', 'OPCS4', 'description', 'The OPCS4 code scheme'));
SET @scheme = LAST_INSERT_ID();


INSERT INTO concept_definition (concept, data)
VALUES (@scheme, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', JSON_ARRAY('CodeScheme')))
    ));


-- CONCEPTS
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('O4_', code),
        'name', if(length(description) > 255, concat(left(description, 252), '...'), description),
        'description', description,
        'codeScheme', 'OPCS4',
        'code', code
    )
FROM opcs4;

INSERT INTO concept_definition (concept, data)
SELECT DISTINCT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'CodeableConcept'))
    )
FROM opcs4 o
JOIN concept c ON c.id = concat('O4_',o.code);
