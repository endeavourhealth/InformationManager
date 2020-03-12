DROP TABLE IF EXISTS legacy_concept_id_map;
CREATE TABLE legacy_concept_id_map (
    legacyId VARCHAR(150) NOT NULL COLLATE utf8_bin,
    coreId VARCHAR(150) COLLATE utf8_bin,

    PRIMARY KEY legacy_concept_id_map_pk(legacyId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS supplier_concept_id_map;
CREATE TABLE supplier_concept_id_map (
    supplierId VARCHAR(150) NOT NULL COLLATE utf8_bin,
    coreId VARCHAR(150) COLLATE utf8_bin,

    PRIMARY KEY supplier_concept_id_map_pk(supplierId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- #################### READ 2 ####################
DROP TABLE IF EXISTS read_v2_snomed_map;
CREATE TABLE read_v2_snomed_map (
                                    readCode VARCHAR(6) NOT NULL,
                                    termCode VARCHAR(2) NOT NULL,
                                    conceptId BIGINT NOT NULL,
                                    PRIMARY KEY read_v2_snomed_map_pk (readCode, termCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

REPLACE INTO read_v2_snomed_map
(readCode, termCode, conceptId)
SELECT m.readCode, m.termCode, IFNULL(a.conceptId, m.conceptId)
FROM read_v2_map m
         LEFT JOIN read_v2_alt_map a ON a.readCode = m.readCode AND a.termCode = m.termCode AND a.useAlt = 'Y'
WHERE m.status = 1
ORDER BY m.readCode, m.termCode, m.assured DESC, m.effectiveDate -- Ensure latest for each row
;

INSERT INTO legacy_concept_id_map
(legacyId, coreId)
SELECT CONCAT('R2_',
              CASE termCode
                  WHEN '00' THEN
                      readCode
                  ELSE
                      IF (LENGTH(REPLACE(readCode, '.', '')) > 2,
                          CONCAT(
                                  REPLACE(readCode, '.', ''),
                                  '-',
                                  IF (SUBSTRING(termCode, 1, 1) ='1', SUBSTRING(termCode, 2, 1), termCode)
                              ),
                          CONCAT(
                                  REPLACE(readCode, '.', ''),
                                  '-',
                                  IF (SUBSTRING(termCode, 1, 1) = '1', SUBSTRING(termCode, 2, 1), termCode),
                                  REPEAT('.', 3-LENGTH(REPLACE(readCode, '.', '')))
                              )
                          )
                  END) as legacy,
       CONCAT('SN_', conceptId) as core
FROM read_v2_snomed_map;

-- #################### CTV3 ####################
DROP TABLE IF EXISTS read_v3_snomed_map;
CREATE TABLE read_v3_snomed_map (
                                    ctv3Concept VARCHAR(6) NOT NULL,
                                    conceptId BIGINT,
                                    PRIMARY KEY read_v3_snomed_map_pk (ctv3Concept)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

REPLACE INTO read_v3_snomed_map
(ctv3Concept, conceptId)
SELECT m.ctv3Concept, IFNULL(a.conceptId, m.conceptId)
FROM read_v3_map m
         LEFT JOIN read_v3_alt_map a ON a.ctv3Concept = m.ctv3Concept AND a.useAlt = 'Y'
WHERE m.status = 1
  AND (m.ctv3Type = 'P' OR m.ctv3Type IS NULL)
ORDER BY m.ctv3Concept, m.effectiveDate, m.assured DESC -- Ensure latest for each row
;

INSERT INTO legacy_concept_id_map
(legacyId, coreId)
SELECT CONCAT('R3_', ctv3Concept), CONCAT('SN_', conceptId)
FROM read_v3_snomed_map
WHERE conceptId IS NOT NULL;


-- #################### OPCS4 ####################
INSERT INTO legacy_concept_id_map
(legacyId, coreId)
SELECT CONCAT('O4_', code), target
FROM opcs4_map
WHERE target IS NOT NULL;


-- #################### Barts Cerner ####################
INSERT INTO legacy_concept_id_map
(legacyId, coreId)
SELECT CONCAT('BC_', code), IF(e.dbid IS NULL, CONCAT('SN_', snomed_expression), CONCAT('DS_BC_', e.dbid))
FROM barts_cerner b
         LEFT JOIN barts_cerner_snomed_expressions e ON e.expression = b.snomed_expression
WHERE snomed_expression IS NOT NULL;

-- #################### EMIS Maps ####################

-- Local read codes (no trud map)
INSERT INTO legacy_concept_id_map
(legacyId, coreId)
SELECT CONCAT('EMLOC_', ers.read_code), CONCAT('SN_', ers.snomed_concept_id)
FROM emis_read_snomed ers
WHERE ers.read_code IS NOT NULL
AND ers.snomed_concept_id IS NOT NULL;

-- Custom READ maps (where different to TRUD map)
INSERT INTO supplier_concept_id_map
(supplierId, coreId)
SELECT CONCAT('R2_', ers.read_code), CONCAT('SN_', ers.snomed_concept_id)
FROM emis_read_snomed ers
LEFT JOIN read_v2 r ON r.code = ers.read_code
LEFT JOIN legacy_concept_id_map m ON m.legacyId = CONCAT('R2_', ers.read_code)
WHERE CONCAT('SN_', ers.snomed_concept_id) <> m.coreId
AND ers.read_code IS NOT NULL;
