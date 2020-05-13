



-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/FHIR', '1.0.0');

SET @model = LAST_INSERT_ID();

-- Create the code schemes
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', id,
        'name', name,
        'description', name)
FROM fhir_scheme;

INSERT INTO concept_definition (concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeScheme')
            )
    )
FROM fhir_scheme f
JOIN concept c ON c.id = f.id;

-- Create the core concept equivalents
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('DS_', scheme, '_', code),
        'name', term,
        'description', term)
FROM fhir_scheme_value
WHERE map IS NULL;

INSERT INTO concept_definition (concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept')
            )
    )
FROM fhir_scheme_value v
JOIN concept c ON c.id = concat('DS_', v.scheme, '_', v.code)
WHERE v.map IS NULL;

-- Create the (mapped) fhir entries
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat(scheme, '_', code),
        'name', term,
        'description', term,
        'codeScheme', scheme,
        'code', code)
FROM fhir_scheme_value v;

INSERT INTO concept_definition (concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept')
            )
    )
FROM fhir_scheme_value v
JOIN concept c ON c.id = concat(v.scheme, '_', v.code);

UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN fhir_scheme_value v ON c.id = concat(v.scheme, '_', v.code)
    JOIN concept e ON e.id = IFNULL(v.map, concat('DS_', v.scheme, '_', v.code))
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'concept', e.id
                                              )
                                      )
                                      )
    );
