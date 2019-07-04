-- ********************* OPTIMISED ACTIVE PREFERRED/SPECIFIED TABLES *********************
DROP TABLE IF EXISTS snomed_description_filtered;
CREATE TABLE snomed_description_filtered
SELECT c.id, d.term
FROM snomed_concept c
         JOIN snomed_description d
              ON d.conceptId = c.id
                  AND d.active = 1
                  AND d.typeId = 900000000000003001 	-- Fully specified name
                  AND d.moduleId = c.moduleId
         JOIN snomed_refset r
              ON r.referencedComponentId = d.id
                  AND r.active = 1
                  AND r.refsetId IN (999001261000000100, 999000691000001104) -- Clinical part & pharmacy part
WHERE c.active = 1;
ALTER TABLE snomed_description_filtered ADD PRIMARY KEY snomed_description_filtered_pk (id);

DROP TABLE IF EXISTS snomed_relationship_active;
CREATE TABLE snomed_relationship_active
SELECT *
FROM snomed_relationship
WHERE active = 1;

-- Create DOCUMENT
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/Snomed', '1.0.0');

SET @doc = LAST_INSERT_ID();

INSERT INTO concept (document, data)
VALUES (@doc, JSON_OBJECT(
                    'id', 'SNOMED',
                    'name', 'SNOMED',
                    'description', 'The SNOMED code scheme',
                    'is_subtype_of', JSON_OBJECT('id', 'CodeScheme'),
                    'code_prefix', 'SN_'
    )
    );

-- INSERT CORE CONCEPTS
INSERT INTO concept (document, data)
SELECT @doc, JSON_OBJECT(
               'id', concat('SN_', id),
               'name', IF(LENGTH(term) > 255, CONCAT(LEFT(term, 252), '...'), term),
               'description', term,
               'code_scheme', JSON_OBJECT('id','SNOMED'),
               'code', id,
               'is_subtype_of', JSON_OBJECT('id','CodeableConcept')
           )
FROM snomed_description_filtered;

-- Relationships
UPDATE concept c
    INNER JOIN (
        SELECT id, JSON_OBJECTAGG(prop, val) as rel
        FROM
            (SELECT concat('SN_', sourceId) as id, concat('SN_', rel.typeId) as prop,
                    JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', concat('SN_', rel.destinationId)
                            )
                        ) as val
             FROM snomed_relationship_active rel
             JOIN snomed_description_filtered fil ON fil.id = rel.destinationId
             GROUP BY rel.sourceId, rel.typeId) t1
        GROUP BY id) t2
    ON t2.id = c.id
SET data=JSON_MERGE(c.data, t2.rel);

