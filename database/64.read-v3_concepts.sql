-- Build work table
DROP TABLE IF EXISTS read_v3_current;
CREATE TABLE read_v3_current
SELECT
    c.code, ifnull(term_62, term_31) as name, term as description
FROM read_v3_concept c
         JOIN read_v3_desc d ON d.code = c.code AND d.type = 'P'
         JOIN read_v3_terms t ON t.termId = d.termId AND t.status = 'C'
WHERE c.status IN ('C', 'O');   -- Import (C)urrent and (O)ptional by default

-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/CTV3', '1.0.0');

SET @model = LAST_INSERT_ID();

-- code scheme
INSERT INTO concept(model, data)
VALUES
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'CTV3', 'name', 'READ 3', 'description', 'The READ (CTV) 3 code scheme'));

SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_definition (concept, data)
VALUES (@scheme, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', JSON_ARRAY('CodeScheme')))
    ));

INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('R3_', code),
        'name', if(length(name) > 60, concat(left(name, 57), '...'), name),
        'description', ifnull(description, name),
        'codeScheme', 'CTV3',
        'code', code
    )
FROM read_v3_current;

-- Relationships
INSERT INTO concept_definition (concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_MERGE_PRESERVE(
                JSON_ARRAY(JSON_OBJECT('concept', 'CodeableConcept')),
                JSON_ARRAYAGG(
                        JSON_OBJECT(
                                'operator', 'AND',
                                'attribute', JSON_ARRAY(
                                        JSON_OBJECT(
                                                'property', 'childOf',
                                                'valueConcept', concat('R3_', rel.parent)
                                            )
                                    )
                            )
                    )
            )
    )
FROM read_v3_hier rel
JOIN read_v3_concept p ON p.code = rel.parent
JOIN concept c ON c.id = concat('R3_', rel.code)
JOIN concept v ON v.id = concat('R3_', rel.parent)
GROUP BY rel.code;;

