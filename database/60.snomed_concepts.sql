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
SELECT *
FROM snomed_relationship
WHERE active = 1;

-- Common/useful IDs
SELECT @subtype := dbid FROM concept WHERE id = 'isA';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @equiv := dbid FROM concept WHERE id = 'isEquivalentTo';
SELECT @codeScheme := dbid FROM concept WHERE id = 'CodeScheme';

-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/Snomed', '1.0.0');

SET @model = LAST_INSERT_ID();

-- Add code scheme
INSERT INTO concept (model, data)
VALUES (@model, JSON_OBJECT(
    'status', 'CoreActive'
    'id','SNOMED',
    'name', 'SNOMED',
    'description', 'The SNOMED code scheme'));
SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_definition (concept, data)
VALUES (@scheme, JSON_OBJECT(
    'status', 'CoreActive',
    'definitionOf', 'SNOMED',
    'subTypeOf', JSON_OBJECT('concept', JSON_ARRAY('CodeableConcept'))
    ));

-- INSERT CORE CONCEPTS
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'id', concat('SN_', id),
        'name', IF(LENGTH(term) > 255, CONCAT(LEFT(term, 252), '...'), term),
        'description', term,
        'codeScheme', @scheme,
        'code', id,
        'status', IF(active = 1, 'CoreActive', 'CoreInactive')
    )
FROM snomed_description_filtered;

-- Relationships
INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, p.dbid, v.dbid
FROM snomed_relationship_active r
JOIN snomed_description_filtered s ON s.id = r.destinationId
JOIN concept c ON c.id = concat('SN_', r.sourceId)
JOIN concept p ON p.id = concat('SN_', r.typeId)
JOIN concept v ON v.id = concat('SN_', r.destinationId);

-- Replacements
INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, p.dbid, v.dbid
FROM snomed_history h
JOIN concept c ON c.id = concat('SN_', h.oldConceptId)
JOIN concept v ON v.id = concat('SN_', h.newConceptId)
JOIN concept p ON p.id IN ('SN_116680003', 'SN_370124000');     -- Is a / Replaced by


-- Process core equivalents
DROP TABLE IF EXISTS snomed_core_map;
CREATE TABLE snomed_core_map (
    snomed_id VARCHAR(140) NOT NULL COLLATE utf8_bin,
    core_id VARCHAR(140) NOT NULL COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO snomed_core_map
(snomed_id, core_id)
VALUES
('SN_116680003', 'isA')    -- Is a
-- ('', 'is_equivalent_to')
-- ('', 'is_related_to')
-- ('SN_149016008', '')     -- MAY BE A
;

-- Add equivalence mapping SNOMED -> Core
INSERT INTO concept_property
(dbid, property, concept)
SELECT s.dbid, @equiv, c.dbid
FROM snomed_core_map m
JOIN concept s ON s.id = m.snomed_id
JOIN concept c ON c.id = m.core_id;

-- Duplicate the SNOMED property values into their core equivalents
INSERT INTO concept_property
(dbid, property, concept)
SELECT cpo.dbid, c.dbid, cpo.concept
FROM snomed_core_map m
JOIN concept s ON s.id = m.snomed_id
JOIN concept c ON c.id = m.core_id
JOIN concept_property cpo ON cpo.property = s.dbid;

