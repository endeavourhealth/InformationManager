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

-- Common/usefule ids
SELECT @coreActive := dbid FROM concept WHERE id = 'CoreActive';
SELECT @coreInactive := dbid FROM concept WHERE id = 'CoreInactive';
SELECT @codeScheme := dbid FROM concept WHERE id = 'codeScheme';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';

SET @operatorAnd = 0;

SET @subtypeOf = 0;
SET @replacedBy = 4;

-- Add code scheme
INSERT INTO concept (model, status, id, name, description)
VALUES (@model, @coreActive, 'SNOMED', 'SNOMED','The SNOMED code scheme');
SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_definition_concept (concept, type, concept_value)
VALUES (@scheme, @subtypeOf, @codeScheme);

-- INSERT CORE CONCEPTS
INSERT INTO concept (model, status, id, name, description, scheme, code)
SELECT @model,
       IF(active = 1, @coreActive, @coreInactive),
       concat('SN_', id),
       IF(LENGTH(term) > 255, CONCAT(LEFT(term, 252), '...'), term),
        term,
        @scheme,
        id
FROM snomed_description_filtered;

INSERT INTO concept_definition_concept (concept, type, concept_value)
SELECT c.dbid, @subtypeOf, @codeable
FROM snomed_description_filtered f
JOIN concept c ON c.id = CONCAT('SN_', f.id);

INSERT INTO concept_definition_concept (concept, operator, type, concept_value)
SELECT c.dbid, @operatorAnd, @subtypeOf, t.dbid
FROM snomed_relationship_active r
         JOIN concept c ON c.id = CONCAT('SN_', r.sourceId)
         JOIN concept t ON t.id = CONCAT('SN_', r.destinationId)
WHERE r.typeId = 116680003
  AND r.relationshipGroup = 0;

INSERT INTO concept_definition_role_group (concept, type, role_group, property, value_concept)
SELECT c.dbid, @subtypeOf, 0, t.dbid, v.dbid
FROM snomed_relationship_active r
JOIN concept c ON c.id = CONCAT('SN_', r.sourceId)
JOIN concept v ON v.id = CONCAT('SN_', r.destinationId)
JOIN concept t ON t.id = CONCAT('SN_', r.typeId)
WHERE r.typeId <> 116680003
  AND r.relationshipGroup = 0;

INSERT INTO concept_definition_role_group (concept, type, role_group, operator, property, value_concept)
SELECT c.dbid, @subtypeOf, r.relationshipGroup, @operatorAnd, t.dbid, v.dbid
FROM snomed_relationship_active r
         JOIN concept c ON c.id = CONCAT('SN_', r.sourceId)
         JOIN concept v ON v.id = CONCAT('SN_', r.destinationId)
         JOIN concept t ON t.id = CONCAT('SN_', r.typeId)
WHERE r.typeId <> 116680003
  AND r.relationshipGroup > 0;

-- Replacements
INSERT INTO concept_definition_concept (concept, type, operator, concept_value)
SELECT c.dbid, @replacedBy, @operatorAnd, r.dbid
FROM snomed_history h
JOIN concept c ON c.id = concat('SN_', h.oldConceptId)
JOIN concept r ON r.id = concat('SN_', h.newConceptId)
GROUP BY h.oldConceptId;
