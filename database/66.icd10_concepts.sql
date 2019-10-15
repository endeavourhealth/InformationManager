-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/ICD10', '1.0.0');

SET @model = LAST_INSERT_ID();

-- Code scheme
INSERT INTO concept(model, data)
VALUES
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'ICD10', 'name', 'ICD10', 'description', 'The ICD10 code scheme'));

SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_definition (concept, data)
VALUES (@scheme, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', JSON_ARRAY('CodeScheme')))
    ));

-- Core concepts
DROP TABLE IF EXISTS icd10_additional;
CREATE TABLE icd10_additional
(
    id VARCHAR(140) NOT NULL,
    name VARCHAR(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO icd10_additional (id, name)
VALUES
('I10_modifier4', 'IDC10 4th character modifier suffix'),
('I10_modifier5', 'IDC10 5th character modifier suffix'),
('I10_qualifiers', 'IDC10 dual classification (asterisk codes)'),
('I10_gender_mask', 'IDC10 gender mask'),
('I10_min_age', 'IDC10 minimum age'),
('I10_max_age', 'IDC10 maximum age'),
('I10_tree_description', 'IDC10 tree description');

INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', id,
        'name', name,
        'description', name
    )
FROM icd10_additional;

-- CONCEPTS
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('I10_', code),
        'name', if(length(description) > 255, concat(left(description, 252), '...'), description),
        'description', description,
        'codeScheme', 'ICD10',
        'code', code
    )
FROM icd10;

INSERT INTO concept_definition (concept, data)
SELECT DISTINCT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'CodeableConcept'))
    )
FROM icd10 i
JOIN concept c ON c.id = concat('I10_', i.code);

UPDATE concept_definition cd
JOIN concept c ON c.dbid = cd.concept
JOIN icd10 i ON c.id = concat('I10_', i.code)
SET cd.data = JSON_MERGE_PRESERVE(
        cd.data,
        JSON_OBJECT('subtypeOf', JSON_ARRAY(
                JSON_OBJECT('operator', 'AND',
                            'attribute',
                            JSON_OBJECT('property', 'I10_modifier4', 'value', modifier_4)
                    )
            )
            )
    )
WHERE modifier_4 IS NOT NULL;

UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN icd10 i ON c.id = concat('I10_', i.code)
SET cd.data = JSON_MERGE_PRESERVE(
        cd.data,
        JSON_OBJECT('subtypeOf', JSON_ARRAY(
                JSON_OBJECT('operator', 'AND',
                            'attribute',
                            JSON_OBJECT('property', 'I10_modifier5', 'value', modifier_5)
                    )
            )
            )
    )
WHERE modifier_5 IS NOT NULL;

UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN icd10 i ON c.id = concat('I10_', i.code)
SET cd.data = JSON_MERGE_PRESERVE(
        cd.data,
        JSON_OBJECT('subtypeOf', JSON_ARRAY(
                JSON_OBJECT('operator', 'AND',
                            'attribute',
                            JSON_OBJECT('property', 'I10_gender_mask', 'value', gender_mask)
                    )
            )
            )
    )
WHERE gender_mask IS NOT NULL;

UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN icd10 i ON c.id = concat('I10_', i.code)
SET cd.data = JSON_MERGE_PRESERVE(
        cd.data,
        JSON_OBJECT('subtypeOf',
                    JSON_OBJECT('operator', 'AND',
                                'attribute', JSON_ARRAY(
                                        JSON_OBJECT('property', 'I10_min_age', 'value', min_age)
                                    )
                        )
            )
    )
WHERE min_age IS NOT NULL;

UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN icd10 i ON c.id = concat('I10_', i.code)
SET cd.data = JSON_MERGE_PRESERVE(
        cd.data,
        JSON_OBJECT('subtypeOf', JSON_ARRAY(
                JSON_OBJECT('operator', 'AND',
                            'attribute',
                            JSON_OBJECT('property', 'I10_max_age', 'value', max_age)
                    )
            )
            )
    )
WHERE max_age IS NOT NULL;

UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN icd10 i ON c.id = concat('I10_', i.code)
SET cd.data = JSON_MERGE_PRESERVE(
        cd.data,
        JSON_OBJECT('subtypeOf', JSON_ARRAY(
                JSON_OBJECT('operator', 'AND',
                            'attribute',
                            JSON_OBJECT('property', 'I10_tree_description', 'value', tree_description)
                    )
            )
            )
    )
WHERE tree_description IS NOT NULL;

UPDATE concept_definition cd
    JOIN (
        SELECT c.dbid, JSON_OBJECT('subtypeOf', JSON_ARRAYAGG(
                JSON_OBJECT('operator', 'AND',
                            'attribute',
                            JSON_OBJECT('property', 'I10_qualifiers', 'valueConcept', JSON_OBJECT('concept', concat('I10_', q.code)))
                    )
            )
            ) AS def
        FROM icd10 i
                 JOIN icd10 q ON (i.qualifiers = q.code OR i.qualifiers LIKE CONCAT(q.code, '^%') OR i.qualifiers LIKE CONCAT('%^', q.code) OR INSTR(i.qualifiers, CONCAT('^', q.code, '^')) > 0)
                 JOIN concept c ON c.id = CONCAT('I10_', i.code)
        WHERE i.qualifiers IS NOT NULL
        GROUP BY i.code
    ) t ON cd.concept = t.dbid
SET cd.data = JSON_MERGE_PRESERVE(cd.data, t.def);
