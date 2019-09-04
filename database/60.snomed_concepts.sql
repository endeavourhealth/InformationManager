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
-- WHERE c.active = 1               -- ********** NOTE : NOW ACTIVE AND INACTIVE CONCEPTS TO BE IMPORTED
;
ALTER TABLE snomed_description_filtered ADD PRIMARY KEY snomed_description_filtered_pk (id);

DROP TABLE IF EXISTS snomed_relationship_active;
CREATE TABLE snomed_relationship_active
SELECT *
FROM snomed_relationship
WHERE active = 1;

-- Common/useful IDs
SELECT @subtype := dbid FROM concept WHERE id = 'is_subtype_of';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';

-- Create DOCUMENT
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/Snomed', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- Add code scheme
INSERT INTO concept (document, id, name, description)
VALUES (@doc, 'SNOMED', 'SNOMED', 'The SNOMED code scheme');
SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_property_object (dbid, property, value)
SELECT @scheme, @subtype, dbid FROM concept WHERE id = 'CodeScheme';

-- INSERT CORE CONCEPTS
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('SN_', id), IF(LENGTH(term) > 255, CONCAT(LEFT(term, 252), '...'), term), term, @scheme, id
FROM snomed_description_filtered;

-- Relationships
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, p.dbid, v.dbid
FROM snomed_relationship_active r
JOIN snomed_description_filtered s ON s.id = r.destinationId
JOIN concept c ON c.id = concat('SN_', r.sourceId)
JOIN concept p ON p.id = concat('SN_', r.typeId)
JOIN concept v ON v.id = concat('SN_', r.destinationId);

-- Replacements
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, p.dbid, v.dbid
FROM snomed_history h
JOIN concept c ON c.id = concat('SN_', h.oldConceptId)
JOIN concept v ON v.id = concat('SN_', h.newConceptId)
JOIN concept p ON p.id IN ('SN_116680003', 'SN_370124000');     -- Is a / Replaced by
