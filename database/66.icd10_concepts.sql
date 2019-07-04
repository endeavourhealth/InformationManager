-- Create document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/ICD10', '1.0.0');

SET @doc = LAST_INSERT_ID();


-- Core concepts
INSERT INTO concept(document, data)
VALUES (@doc, JSON_OBJECT(
                    'id', 'ICD10',
                    'name', 'ICD10',
                    'description', 'The ICD10 code scheme',
                    'is_subtype_of', JSON_OBJECT('id', 'CodeScheme'),
                    'code_prefix', 'I10_'
        )),
       (@doc, JSON_OBJECT(
                    'id', 'I10_modifier4',
                    'name', 'IDC10 4th character modifier suffix',
                    'is_subtype_of', JSON_OBJECT(
                        'id', 'CodeableConcept'
                        ))),
       (@doc, JSON_OBJECT(
                    'id', 'I10_modifier5',
                    'name', 'IDC10 5th character modifier suffix',
                    'is_subtype_of', JSON_OBJECT(
                        'id', 'CodeableConcept'
                        ))),
       (@doc, JSON_OBJECT(
                    'id', 'I10_qualifiers',
                    'name', 'IDC10 dual classification (asterisk codes)',
                    'is_subtype_of', JSON_OBJECT(
                        'id', 'CodeableConcept'
                        ))),
       (@doc, JSON_OBJECT(
                    'id', 'I10_gender_mask',
                    'name', 'IDC10 gender mask',
                    'is_subtype_of', JSON_OBJECT(
                        'id', 'CodeableConcept'
                        ))),
       (@doc, JSON_OBJECT(
                    'id', 'I10_min_age',
                    'name', 'IDC10 minimum age',
                    'is_subtype_of', JSON_OBJECT(
                        'id', 'CodeableConcept'
                        ))),
       (@doc, JSON_OBJECT(
                    'id', 'I10_max_age',
                    'name', 'IDC10 maximum age',
                    'is_subtype_of', JSON_OBJECT(
                        'id', 'CodeableConcept'
                        ))),
       (@doc, JSON_OBJECT(
                    'id', 'I10_tree_description',
                    'name', 'IDC10 tree description',
                    'is_subtype_of', JSON_OBJECT(
                        'id', 'CodeableConcept'
                        )));

-- CONCEPTS
INSERT INTO concept (document, data)
SELECT @doc, JSON_OBJECT(
           'id', concat('I10_', code),
           'name', if(length(description) > 255, concat(left(description, 252), '...'), description),
           'description', description,
           'code_scheme', JSON_OBJECT('id', 'ICD10'),
           'code', code,
           'is_subtype_of', JSON_OBJECT(
               'id', 'CodeableConcept'
               ),
           'I10_modifier4', modifier_4,
           'I10_modifier5', modifier_5,
           'I10_gender_mask', gender_mask,
           'I10_min_age', min_age,
           'I10_max_age', max_age,
           'I10_tree_description', tree_description
           )
FROM icd10;


UPDATE concept c
    INNER JOIN
    (
        SELECT CONCAT('I10_', code) AS qid,
               JSON_OBJECT(
                   'I10_qualifiers',
                   CAST(CONCAT('[{"id": "I10_', REPLACE(qualifiers, '^', '"},{"id": "I10_'), '"}]') AS JSON)
                   )                AS qual
        FROM icd10
        WHERE qualifiers <> ''
    ) t1
    ON t1.qid = c.id
SET data = JSON_MERGE(c.data, t1.qual);
