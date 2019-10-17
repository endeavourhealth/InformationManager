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
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', JSON_ARRAY('CodeScheme')))
    ));

-- CORE CONCEPTS (TermCode = 00)
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
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept'),
                JSON_OBJECT(
                        'operator', 'AND',
                        'attribute',
                        JSON_OBJECT(
                                'property', 'childOf',
                                'valueConcept', JSON_OBJECT('concept', concat('R2_', INSERT(v.code, IF(INSTR(v.code, '.') = 0, 5, INSTR(v.code, '.') - 1), 1, '.')))
                            )
                    )
            )
    )
FROM read_v2 v
JOIN concept c ON c.id = concat('R2_', v.code);

-- VARIANTS (TermCode != 00)
INSERT INTO concept (model, data)
SELECT DISTINCT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('R2_', readCode, termCode),
        'description', @desc:=IFNULL(term198, IFNULL(term60, term30)),
        'name', if(length(@desc) > 255, concat(left(@desc, 252), '...'), @desc),
        'codeScheme', 'READ2',
        'code', concat(readCode, termCode)
    )
FROM read_v2_key
WHERE termCode <> '00';

INSERT INTO concept_definition (concept, data)
SELECT DISTINCT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept'),
                JSON_OBJECT('operator', 'AND', 'attribute', JSON_OBJECT('property', 'termCodeOf', 'valueConcept', JSON_OBJECT('concept', concat('R2_', v.readCode))))
            )
    )
FROM read_v2_key v
JOIN concept c ON c.id = concat('R2_', v.readcode, v.termCode);
