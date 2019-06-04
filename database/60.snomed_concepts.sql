-- Filter the entries we're interested in
DROP TABLE IF EXISTS snomed_description_filtered;
CREATE TABLE snomed_description_filtered
SELECT d.conceptId, d.term
FROM snomed_description d
         JOIN snomed_concept c ON c.id = d.conceptId AND c.active = 1
WHERE d.moduleId = 900000000000207008
  AND d.typeId = 900000000000003001
  AND d.active = 1;

INSERT INTO snomed_description_filtered
SELECT d.conceptId, d.term
FROM snomed_refset_clinical_active_preferred_component r
         JOIN snomed_description_active_fully_specified d ON d.id = r.referencedComponentId
WHERE d.moduleId <> 900000000000207008;

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
                        'is_subtype_of', JSON_OBJECT('id', 'CodeScheme')
    )
    );

-- INSERT CORE CONCEPTS
INSERT INTO concept (document, data)
SELECT @doc, JSON_OBJECT(
               'id', concat('SN_', conceptId),
               'name', IF(LENGTH(term) > 60, CONCAT(LEFT(term, 57), '...'), term),
               'description', term,
               'code_scheme', JSON_OBJECT('id','SNOMED'),
               'code', conceptId,
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
             GROUP BY rel.sourceId, rel.typeId) t1
        GROUP BY id) t2
    ON t2.id = c.id
SET data=JSON_MERGE(c.data, t2.rel);

