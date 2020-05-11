USE im_v1_meta;

-- ######################## NOTE: ##########################
-- ## USE INSERT IGNORE TO ALLOW DELTAS VIA UPDATED FIELD ##
-- #########################################################

-- #################### TABLE ####################
CREATE TABLE IF NOT EXISTS concept_meta (
    id varchar(150) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Unique human-readable concept id',
    draft tinyint(1) NOT NULL DEFAULT '0',
    name varchar(255) DEFAULT NULL,
    status varchar(150) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    description varchar(400) DEFAULT NULL,
    scheme VARCHAR(150) DEFAULT NULL,
    code varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    updated datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY concept_scheme_code_idx (scheme,code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- #################### SNOMED ####################
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT CONCAT('SN_', id),
       IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term),
       term,
       'SNOMED',
       id,
       IF (active = 1, 'CoreActive', 'CoreInactive')
FROM im_source.snomed_description_filtered;

-- #################### READ 2 ####################
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT CONCAT('R2_', code),
       IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term),
       term,
       'READ2',
       REPLACE(code, '.', ''),
       'LegacyActive'
FROM im_source.read_v2;

-- #################### CTV3 ####################
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT CONCAT('R3_', code),
       IF(LENGTH(name) > 250, CONCAT(LEFT(name, 247), '...'), name),
       ifnull(description, name),
       'CTV3',
       code,
        IF (status IN ('C', 'O'), 'LegacyActive', 'LegacyInactive')
FROM im_source.read_v3_summary;

-- #################### ICD10 ####################
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
VALUES
('I10_Modifier4', 'ICD10 Modifier 4', 'IDC10 4th character modifier suffix', 'ICD10', 'Modifier4', 'LegacyActive'),
('I10_Modifier5', 'ICD10 Modifier 5', 'IDC10 5th character modifier suffix', 'ICD10', 'Modifier5', 'LegacyActive'),
('I10_Qualifiers', 'ICD10 Modifier qualifiers', 'IDC10 dual classification (asterisk codes)', 'ICD10', 'Qualifiers', 'LegacyActive'),
('I10_GenderMask', 'ICD10 Gender mask', 'IDC10 gender mask', 'ICD10', 'GenderMask', 'LegacyActive'),
('I10_MinAge', 'IDC10 minimum age', 'IDC10 minimum age', 'ICD10', 'MinAge', 'LegacyActive'),
('I10_MaxAge', 'IDC10 maximum age', 'IDC10 maximum age', 'ICD10', 'MaxAge', 'LegacyActive'),
('I10_TreeDescription', 'IDC10 tree description', 'IDC10 tree description', 'ICD10', 'TreeDescription', 'LegacyActive');

INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT CONCAT('I10_', code),
       IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description),
       description,
       'ICD10',
       code,
       'LegacyActive'
FROM im_source.icd10;

-- #################### OPCS4 ####################
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT CONCAT('O4_', code),
       IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description),
       description,
       'OPCS4',
       code,
       'LegacyActive'
FROM im_source.opcs4;

-- #################### BARTS/CERNER ####################
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT CONCAT('BC_', code),
       IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term),
       term,
       'BartsCerner',
       code,
       'LegacyActive'
FROM im_source.barts_cerner;

-- #################### ENCOUNTER TYPES ####################
-- Code schemes
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
VALUES
('LENC', 'Legacy encounter', 'Legacy encounter code type', 'LENC', 'TYPE', 'CoreActive'),
('COT', 'Core type', 'Core code type', 'COT', 'TYPE', 'CoreActive');

-- Legacy codes
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT DISTINCT (code),
                IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term),
                term,
                'LENC',
                REPLACE(code, 'LE_', ''),
                'LegacyActive'
FROM im_source.encounter_types;

-- Core codes
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT DISTINCT REPLACE(core_iri, 'cm:', 'COT_'),
                IF(LENGTH(core_term) > 250, CONCAT(LEFT(core_term, 247), '...'), core_term),
                core_term,
                'COT',
                REPLACE(core_iri, 'cm:', ''),
                'CoreActive'
FROM im_source.encounter_types;

-- #################### FHIR VALUE SETS ####################
-- Code schemes
INSERT IGNORE INTO concept_meta
(id, name, description, status)
SELECT id, name, name, 'CoreActive'
FROM im_source.fhir_scheme;

-- Core codes
INSERT IGNORE INTO concept_meta
(id, name, description, status)
SELECT CONCAT('DS_', scheme, '_', code),
       term,
       term,
       'CoreActive'
FROM im_source.fhir_scheme_value
WHERE map IS NULL;

-- Mapped FHIR
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT concat(scheme, '_', code),
        term,
        term,
        scheme,
        code,
        'LegacyActive'
FROM im_source.fhir_scheme_value;

-- #################### EMIS LOCAL - NOTE: Duplicate where hyphenated ####################
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT DISTINCT CONCAT('EMLOC_', l.readTermId),
       IF(LENGTH(l.codeTerm) > 250, CONCAT(LEFT(l.codeTerm, 247), '...'), l.codeTerm),
       l.codeTerm,
       'EMIS_LOCAL',
       l.readTermId,
       'LegacyActive'
FROM im_source.emis_read_snomed l
LEFT JOIN im_source.read_v2 r ON REPLACE(r.code, '.', '') = l.readTermId
AND r.code IS NULL;

-- #################### TPP LOCAL ####################
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT CONCAT('TPPLOC_', l.ctv3Code),
       IF(LENGTH(l.ctv3Text) > 250, CONCAT(LEFT(l.ctv3Text, 247), '...'), l.ctv3Text),
       l.ctv3Text,
       'TPP_LOCAL',
       l.ctv3Code,
       'LegacyActive'
FROM im_source.tpp_local_codes l
LEFT JOIN im_source.read_v3_concept r ON r.code = l.ctv3Code
WHERE r.code IS NULL;

-- #################### VISION LOCAL ####################
INSERT IGNORE INTO concept_meta
(id, name, description, scheme, code, status)
SELECT CONCAT('VISLOC_', l.readCode),
       IF(LENGTH(l.readTerm) > 250, CONCAT(LEFT(l.readTerm, 247), '...'), l.readTerm),
       l.readTerm,
       'VISION_LOCAL',
       l.readCode,
       'LegacyActive'
FROM im_source.vision_local_codes l
LEFT JOIN im_source.read_v2 r ON r.code = l.readCode
WHERE r.code IS NULL;
