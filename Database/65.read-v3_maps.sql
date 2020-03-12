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
