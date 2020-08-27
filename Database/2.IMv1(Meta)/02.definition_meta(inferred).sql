USE im_v1_meta;

-- ######################## NOTE: ##########################
-- ## USE INSERT IGNORE TO ALLOW DELTAS VIA UPDATED FIELD ##
-- #########################################################
SET @isA := 'SN_116680003';
SET @isReplacedBy := 'SN_370124000';
SET @hasParent := 'has_parent';
SET @isEquivalentTo := 'is_equivalent_to';

-- #################### TABLE ####################

DROP TABLE concept_property_object_meta;
CREATE TABLE IF NOT EXISTS concept_property_object_meta (
                              concept VARCHAR(140) NOT NULL COLLATE utf8_bin,
                              property VARCHAR(140) NOT NULL COLLATE utf8_bin,
                              object VARCHAR(140) NOT NULL COLLATE utf8_bin,
                              `group` INT NOT NULL DEFAULT 0,

                              INDEX cpo_idx (concept, property, object)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- #################### SNOMED ####################
-- Relationships
INSERT INTO concept_property_object_meta
(concept, property, object, `group`)
SELECT CONCAT('SN_', sourceId), CONCAT('SN_', typeId), CONCAT('SN_', destinationId), relationshipGroup
FROM im_source.snomed_relationship r
WHERE r.active = true;

-- Replacements
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT CONCAT('SN_', oldConceptId), @isReplacedBy, CONCAT('SN_', newConceptId)
FROM im_source.snomed_history h;

-- #################### READ2 ####################
-- Relationships
SELECT CONCAT('R2_', code), @hasParent, CONCAT('R2_', LEFT(code, @l:=LENGTH(REPLACE(code, '.', ''))-1), REPEAT('.', 5-@l))
FROM im_source.read_v2
WHERE code <> '.....';

-- Forward maps (READ2 -- is equivalent to --> SNOMED) 1:1
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT IF(m.termCode = '00', CONCAT('R2_', m.readCode), CONCAT('R2_', m.readCode, m.termCode)), @isEquivalentTo, CONCAT('SN_', IFNULL(a.conceptId, m.conceptId))
FROM im_source.read_v2_map m
LEFT JOIN im_source.read_v2_alt_map a ON a.readCode = m.readCode AND a.termCode = m.termCode AND a.useAlt = 'Y'
WHERE m.status = 1 AND m.assured = 1;

-- Reverse maps (READ2 -- is a --> SNOMED) 1:n
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT IF(m.termCode = '00', CONCAT('R2_', m.readCode), CONCAT('R2_', m.readCode, m.termCode)), @isA, CONCAT('SN_', IFNULL(a.conceptId, m.conceptId))
FROM im_source.read_v2_map m
LEFT JOIN im_source.read_v2_alt_map a ON a.readCode = m.readCode AND a.termCode = m.termCode AND a.useAlt = 'Y'
WHERE m.status = 1 AND m.assured = 1;


-- #################### CTV3 ####################
-- Relationships
SELECT CONCAT('R3_', code), @hasParent, CONCAT('R3_', parent)
FROM im_source.read_v3_hier;

-- Forward maps (CTV3 -- is equivalent to --> SNOMED) 1:1
SET @i := 0;

INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT CONCAT('R3_', ctv3Concept), @isEquivalentTo, CONCAT('SN_', snomedId)
FROM (
         SELECT m.ctv3Concept, IFNULL(a.conceptId, m.conceptId) AS snomedId, IF(@id = m.ctv3Concept, @i := @i + 1, @i := 0) AS i, @id := m.ctv3Concept
         FROM im_source.read_v3_map m
         LEFT JOIN im_source.read_v3_alt_map a ON a.ctv3Concept = m.ctv3Concept AND a.useAlt = 'Y'
         WHERE m.status = 1 AND m.conceptId IS NOT NULL
         AND (m.ctv3Type = 'P' OR m.ctv3Type IS NULL)
         ORDER BY m.ctv3Concept, m.assured DESC, m.effectiveDate DESC
     ) x
WHERE i = 0;

-- Reverse maps (CTV3 -- is a --> SNOMED) 1:n
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT DISTINCT CONCAT('R3_', m.ctv3Concept), @isA, CONCAT('SN_', IFNULL(a.conceptId, m.conceptId))
FROM im_source.read_v3_map m
LEFT JOIN im_source.read_v3_alt_map a ON a.ctv3Concept = m.ctv3Concept AND a.useAlt = 'Y'
WHERE m.status = 1 AND m.conceptId IS NOT NULL
AND (m.ctv3Type = 'P' OR m.ctv3Type IS NULL);


-- #################### ICD10 ####################
-- Relationships
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT code, @hasParent, LEFT(code, INSTR(code, '.')-1)
FROM im_source.ICD10
WHERE code LIKE '%.%';

-- Forward maps (ICD10 -- is equivalent to --> SNOMED) 1:1
-- NONE

-- Reverse maps (ICD10 -- is a --> SNOMED) 1:n
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT DISTINCT i.code, @isA, m.referencedComponentId
FROM im_source.icd10_opcs4_maps m
JOIN im_source.icd10 i ON i.alt_code = m.mapTarget
WHERE m.refsetId = 999002271000000101
AND m.active = 1
ORDER BY i.code;

-- #################### OPCS4 ####################
-- Relationships
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT code, @hasParent, LEFT(code, INSTR(code, '.')-1)
FROM im_source.opcs4
WHERE code LIKE '%.%';

-- Forward maps (OPCS4 -- is equivalent to --> SNOMED) 1:1
-- NONE

-- Reverse maps (OPCS4 -- is a --> SNOMED) 1:n
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT DISTINCT o.code, @isA, m.referencedComponentId
FROM im_source.icd10_opcs4_maps m
JOIN im_source.opcs4 o ON o.altCode = m.mapTarget
WHERE m.refsetId = 999002741000000101
AND m.active = 1
ORDER BY o.code;

-- #################### BARTS CERNER ####################
-- Relationships
-- NONE

-- Forward maps (BARTSCERNER -- is equivalent to --> SNOMED) 1:1
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT CONCAT('BC_', code), @isEquivalentTo, IF(e.dbid IS NULL, CONCAT('SN_', b.snomed_expression), CONCAT('DS_BC_', e.dbid))
FROM im_source.barts_cerner b
LEFT JOIN im_source.barts_cerner_snomed_expressions e ON e.expression = b.snomed_expression
WHERE b.snomed_expression IS NOT NULL;

-- Reverse maps (BARTSCERNER -- is a --> SNOMED) 1:n
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT CONCAT('BC_', code), @isA, CONCAT('SN_', b.snomed_expression)
FROM im_source.barts_cerner b
LEFT JOIN im_source.barts_cerner_snomed_expressions e ON e.expression = b.snomed_expression
WHERE b.snomed_expression IS NOT NULL
AND e.expression IS NULL;

-- #################### ENCOUNTER TYPES ####################
-- Relationships
-- NONE

-- Forward maps (LENC -- is equivalent to --> COT) 1:1
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT code, @isEquivalentTo, REPLACE(core_iri, 'cm:', 'COT_')
FROM im_source.encounter_types;

-- Reverse maps (LENC -- is a --> COT) 1:n
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT code, @isA, REPLACE(core_iri, 'cm:', 'COT_')
FROM im_source.encounter_types;

-- #################### FHIR ####################
-- Relationships
-- NONE

-- Forward maps (FHIR -- is equivalent to --> CORE) 1:1
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT concat(scheme, '_', code), @isEquivalentTo, CONCAT('DS_', scheme, '_', code)
FROM im_source.fhir_scheme_value;

-- Reverse maps (BARTSCERNER -- is a --> SNOMED) 1:n
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT concat(scheme, '_', code), @isA, CONCAT('DS_', scheme, '_', code)
FROM im_source.fhir_scheme_value;


-- #################### EMIS ####################
-- Relationships
-- NONE

-- Forward maps (EMIS -- is equivalent to --> SNOMED) 1:1
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT DISTINCT readTermId, @isEquivalentTo, snomedCTConceptId
FROM im_source.emis_read_snomed e
LEFT JOIN im_source.read_v2 r ON REPLACE(r.code, '.', '') = e.readTermId
WHERE e.snomedCTConceptId NOT LIKE '%1000006___'        -- Ignore "False SNOMED" maps
AND r.code IS NULL;

-- Reverse maps (EMIS -- is a --> SNOMED) 1:n
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT DISTINCT readTermId, @isA, snomedCTConceptId
FROM im_source.emis_read_snomed e
LEFT JOIN im_source.read_v2 r ON REPLACE(r.code, '.', '') = e.readTermId
WHERE e.snomedCTConceptId NOT LIKE '%1000006___'        -- Ignore "False SNOMED" maps
AND r.code IS NULL;


-- #################### TPP ####################
-- Relationships
-- NONE

-- Forward maps (TPP -- is equivalent to --> SNOMED) 1:1
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT CONCAT('TPPLOCK_', ctv3Code), @isEquivalentTo, CONCAT('SN_', snomedCode)
FROM im_source.tpp_snomed_map;

-- Reverse maps (TPP -- is a --> SNOMED) 1:n
INSERT INTO concept_property_object_meta
(concept, property, object)
SELECT CONCAT('TPPLOCK_', ctv3Code), @isA, CONCAT('SN_', snomedCode)
FROM im_source.tpp_snomed_map;
