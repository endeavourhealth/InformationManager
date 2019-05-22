-- Create DOCUMENT
# INSERT INTO document (path, version)
# VALUES ('InformationModel/dm/READ2', '1.0.0');

SET @doc = 5; -- LAST_INSERT_ID();

INSERT INTO concept(document, data)
VALUES (@doc, JSON_OBJECT(
                    'id', 'READ2',
                    'name', 'READ 2',
                    'description', 'The READ2 code scheme',
                        'is_subtype_of', JSON_OBJECT(
                        'id', 'CodeScheme'
                        )));

-- CONCEPTS
INSERT INTO concept (document, data)
SELECT @doc, JSON_OBJECT(
           'id', concat('R2_',code),
           'name', if(length(term) > 60, concat(left(term, 57), '...'), term),
           'description', term,
           'code_scheme', 'READ2',
           'code', code,
           'is_subtype_of', JSON_OBJECT(
               'id','CodeableConcept'
               )
           )
FROM read_v2;

-- ATTRIBUTES
DROP TABLE IF EXISTS read_v2_hierarchy;
CREATE TABLE read_v2_hierarchy
SELECT r.code AS code, @code:=REPLACE(r.code,'.','') AS code_trimmed, @parent:=RPAD(SUBSTRING(@code, 1, LENGTH(@code)-1), 5, '.') AS parent
FROM read_v2 r;

UPDATE concept c
    INNER JOIN (
        SELECT id, JSON_OBJECTAGG(prop, val) as rel
        FROM
            (SELECT concat('R2_', rel.code) as id, 'has_parent' as prop,
                    JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', concat('R2_', rel.parent)
                            )
                        ) as val
             FROM read_v2_hierarchy rel
             GROUP BY rel.code) t1
        GROUP BY id) t2
    ON t2.id = c.id
SET data=JSON_MERGE(c.data, t2.rel);
