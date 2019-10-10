-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/TPPLocal', '1.0.0');

SET @model = LAST_INSERT_ID();


-- Code scheme
INSERT INTO concept (model, data)
VALUES (@model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', 'TPP_LOCAL',
        'name', 'TPP Local',
        'description', 'TPP local codes scheme'));

SET @scheme := LAST_INSERT_ID();

INSERT INTO concept_definition (concept, data)
VALUES (@scheme, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeScheme')
            )
    ));

-- Concepts
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', CONCAT('TPPLOC_', ctv3_code),
        'name', ctv3_text,
        'codeScheme', 'TPP_LOCAL',
        'code', ctv3_code)
FROM tpp_local_codes;

INSERT INTO concept_definition (concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept')
            )
    )
FROM tpp_local_codes e
JOIN concept c ON c.id = CONCAT('TPPLOC_', ctv3_code);
