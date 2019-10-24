-- ********************* OPTIMISED ACTIVE PREFERRED/SPECIFIED TABLES *********************
DROP TABLE IF EXISTS snomed_description_filtered;
CREATE TABLE snomed_description_filtered
SELECT c.id, d.term, c.active
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
-- WHERE c.active = 1               -- ********** NOTE : NOW ACTIVE AND INACTIVE CONCEPTS TO BE IMPORTED
;
ALTER TABLE snomed_description_filtered ADD PRIMARY KEY snomed_description_filtered_pk (id);

DROP TABLE IF EXISTS snomed_relationship_active;
CREATE TABLE snomed_relationship_active
SELECT sr.*
FROM snomed_relationship sr
WHERE sr.active = 1;

ALTER TABLE snomed_relationship_active
ADD INDEX snomed_relationship_active_grp (relationshipGroup);

-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/Snomed', '1.0.0');

SET @model = LAST_INSERT_ID();

-- Add code scheme
INSERT INTO concept (model, data)
VALUES (@model, JSON_OBJECT(
    'status', 'CoreActive',
    'id','SNOMED',
    'name', 'SNOMED',
    'description', 'The SNOMED code scheme'));
SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_definition (concept, data)
VALUES (@scheme, JSON_OBJECT(
    'status', 'CoreActive',
    'subtypeOf', JSON_ARRAY(
            JSON_OBJECT('concept', 'CodeScheme')
        )
    ));

-- INSERT CORE CONCEPTS
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'id', concat('SN_', id),
        'name', IF(LENGTH(term) > 255, CONCAT(LEFT(term, 252), '...'), term),
        'description', term,
        'codeScheme', 'SNOMED',
        'code', id,
        'status', IF(active = 1, 'CoreActive', 'CoreInactive')
    )
FROM snomed_description_filtered;

-- Definitions
DROP TABLE IF EXISTS snomed_json;
CREATE TABLE snomed_json
(
    id BIGINT NOT NULL,
    def JSON NOT NULL,
    INDEX snomed_json_idx (id)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

INSERT INTO snomed_json (id, def)
SELECT  f.id, JSON_OBJECT('concept', 'CodeableConcept')
FROM snomed_description_filtered f
LEFT JOIN snomed_history h ON h.oldConceptId = f.id
WHERE h.oldConceptId IS NULL;
;

INSERT INTO snomed_json
(id, def)
SELECT rg.sourceId,
       CASE WHEN rg.typeId = 116680003 THEN
                JSON_OBJECT('concept', CONCAT('SN_', rg.destinationId))
            ELSE
                JSON_OBJECT('attribute', JSON_OBJECT('property', CONCAT('SN_', rg.typeId), 'valueConcept', JSON_OBJECT('concept', CONCAT('SN_', rg.destinationId))))
           END AS def
FROM snomed_relationship_active rg
WHERE rg.relationshipGroup = 0;

SELECT @idx:=0;
SELECT @sourceId:=null;

INSERT INTO snomed_json
(id, def)
SELECT t2.sourceId, JSON_OBJECT('roleGroup', JSON_OBJECT('attribute', JSON_ARRAYAGG(t2.def)))
FROM (
         SELECT rg.relationshipGroup,
                @idx:=CASE WHEN @sourceId=sourceId AND @grp=relationshipGroup THEN @idx+1 ELSE 1 END,
                CASE WHEN @idx = 1 THEN
                         JSON_OBJECT('property', CONCAT('SN_', rg.typeId), 'valueConcept', JSON_OBJECT('concept', CONCAT('SN_', rg.destinationId)))
                     ELSE
                         JSON_OBJECT('operator', 'AND', 'property', CONCAT('SN_', rg.typeId), 'valueConcept', JSON_OBJECT('concept', CONCAT('SN_', rg.destinationId)))
                    END AS def,
                @sourceId:=sourceId as sourceId,
                @grp:=relationshipGroup
         FROM snomed_relationship_active rg
         WHERE rg.relationshipGroup > 0
         ORDER BY rg.sourceId, rg.relationshipGroup
     ) AS t2
GROUP BY t2.sourceId, t2.relationshipGroup
;

SELECT @idx:=0;
SELECT @id:=null;

INSERT INTO concept_definition
(concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAYAGG(def)
    )
FROM
    (
        SELECT @idx:=CASE WHEN @id=id THEN @idx+1 ELSE 1 END,
               @id:=id AS id,
               CASE WHEN @idx=1 THEN def ELSE JSON_MERGE(JSON_OBJECT('operator','AND'), def) END AS def
        FROM snomed_json
        ORDER BY id
    ) t
        JOIN concept c ON c.id = CONCAT('SN_', t.id)
  GROUP BY t.id
;

-- Replacements
INSERT INTO concept_definition (concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept'),
                JSON_OBJECT('operator', 'AND', 'attribute', JSON_OBJECT('property', 'replacedBy', 'valueConcept', JSON_OBJECT('concept', concat('SN_', h.newConceptId))))
            )
    )
FROM snomed_history h
JOIN concept c ON c.id = concat('SN_', h.oldConceptId)
GROUP BY h.oldConceptId;
