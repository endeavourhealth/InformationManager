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
