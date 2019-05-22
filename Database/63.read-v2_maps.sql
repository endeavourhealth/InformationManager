DROP TABLE IF EXISTS read_v2_map_tmp;
CREATE TABLE read_v2_map_tmp (
    readCode VARCHAR(6) COLLATE utf8_bin NOT NULL,
    conceptId BIGINT NOT NULL,
    INDEX read_v2_map_tmp_idx (readCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO read_v2_map_tmp
(readCode, conceptId)
SELECT m.readCode, m.conceptId
FROM read_v2_map m
         JOIN concept c ON c.id = CONCAT('SN_', m.conceptId)
WHERE m.assured = 1
  AND m.status = 1
  AND m.termCode = '00';

-- Populate summary
DROP TABLE IF EXISTS read_v2_map_summary;
CREATE TABLE read_v2_map_summary (
                                     readCode VARCHAR(6) COLLATE utf8_bin NOT NULL,
                                     multi BOOLEAN DEFAULT FALSE,
                                     altConceptId BIGINT DEFAULT NULL,
                                     PRIMARY KEY read_v2_map_summary_pk (readCode),
                                     INDEX read_v2_map_summary_multi_idx (multi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO read_v2_map_summary
(readCode, multi, altConceptId)
SELECT t.readCode, COUNT(DISTINCT c.dbid) > 1 as multi, IF(c.dbid is null, null, a.conceptId) as conceptId
FROM read_v2_map_tmp t
LEFT JOIN read_v2_alt_map a ON a.readCode = t.readCode AND a.termCode = '00' AND a.conceptId IS NOT NULL AND a.useAlt = 'Y'
LEFT JOIN concept c ON c.id = CONCAT('SN_', a.conceptId)
GROUP BY t.readCode;

-- Add 1:1 maps
UPDATE concept c
    INNER JOIN (
        SELECT s.readCode, JSON_OBJECT('is_equivalent_to', JSON_OBJECT('id', CONCAT('SN_', t.conceptId))) as equiv
        FROM read_v2_map_summary s
        JOIN read_v2_map_tmp t ON t.readCode = s.readCode
        WHERE s.multi = FALSE
    ) t2 ON c.id = CONCAT('R2_', t2.readCode)
SET data = JSON_MERGE(data, equiv);

-- Add 1:n maps with 1:1 (alternative) overrides
UPDATE concept c
    INNER JOIN (
        SELECT s.readCode, JSON_OBJECT('is_equivalent_to', JSON_OBJECT('id', CONCAT('SN_', s.altConceptId))) as equiv
        FROM read_v2_map_summary s
        WHERE s.multi = TRUE
          AND s.altConceptId IS NOT NULL
    ) t2 ON c.id = CONCAT('R2_', t2.readCode)
SET data = JSON_MERGE(data, t2.equiv);

-- Add 1:n maps with no alternative overrides (proxy concepts)
UPDATE concept c
    INNER JOIN (
        SELECT s.readCode, JSON_OBJECT('is_equivalent_to', JSON_OBJECT('id', CONCAT('DS_R2_', s.readCode))) as equiv
        FROM read_v2_map_summary s
        WHERE s.multi = TRUE
          AND s.altConceptId IS NULL
    ) t2 ON c.id = CONCAT('R2_', t2.readCode)
SET data = JSON_MERGE(data, t2.equiv);

-- Create PROXY document
# INSERT INTO document (path, version)
# VALUES ('InformationModel/dm/R2-proxy', '1.0.0');

SET @doc = 6; -- LAST_INSERT_ID();

-- Create proxy concepts
INSERT INTO concept
(document, data)
SELECT @doc, JSON_OBJECT(
        'id', CONCAT('DS_R2_', s.readCode),
        'name', c.name,
        'description', c.description,
        'is_subtype_of', JSON_OBJECT('id', 'CodeableConcept'),
        'is_related_to', JSON_ARRAYAGG(JSON_OBJECT('id', CONCAT('SN_', t.conceptId)))
    )
FROM read_v2_map_summary s
         JOIN read_v2_map_tmp t ON t.readCode = s.readCode
         JOIN concept c ON c.id = CONCAT('R2_', s.readCode)
WHERE s.multi = TRUE
  AND s.altConceptId IS NULL
GROUP BY s.readCode;
