DROP TABLE IF EXISTS read_v3_map_tmp;
CREATE TABLE read_v3_map_tmp (
                                 ctv3Concept VARCHAR(6) COLLATE utf8_bin NOT NULL,
                                 conceptId BIGINT NOT NULL,
                                 INDEX read_v3_map_tmp (ctv3Concept)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO read_v3_map_tmp
(ctv3Concept, conceptId)
SELECT DISTINCT m.ctv3Concept, m.conceptId
FROM read_v3_map m
         JOIN concept c ON c.id = CONCAT('SN_', m.conceptId)
WHERE m.status = 1
  AND m.assured = 1
  AND (m.ctv3Type = 'P' OR m.ctv3Type IS NULL);

-- Populate summary
DROP TABLE IF EXISTS read_v3_map_summary;
CREATE TABLE read_v3_map_summary (
                                     ctv3Concept VARCHAR(6) COLLATE utf8_bin NOT NULL,
                                     multi BOOLEAN DEFAULT FALSE,
                                     altConceptId BIGINT DEFAULT NULL,
                                     PRIMARY KEY read_v3_map_summary_pk (ctv3Concept),
                                     INDEX read_v3_map_summary_idx (multi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO read_v3_map_summary
(ctv3Concept, multi, altConceptId)
SELECT t.ctv3Concept, COUNT(DISTINCT t.conceptId) > 1 as multi, a.conceptId
FROM read_v3_map_tmp t
         LEFT JOIN read_v3_alt_map a ON a.ctv3Concept = t.ctv3Concept AND a.conceptId IS NOT NULL AND a.useAlt = 'Y'
GROUP BY t.ctv3Concept;

-- Add 1:1 maps
UPDATE concept c
    INNER JOIN (
            SELECT s.ctv3Concept, JSON_OBJECT('is_equivalent_to', JSON_OBJECT('id', CONCAT('SN_', t.conceptId))) as equiv
            FROM read_v3_map_summary s
            JOIN read_v3_map_tmp t ON t.ctv3Concept = s.ctv3Concept
            WHERE s.multi = FALSE
    ) t ON c.id = CONCAT('R3_', t.ctv3Concept)
SET data = JSON_MERGE(data, equiv);

-- Add 1:n maps with 1:1 (alternative) overrides
UPDATE concept c
    INNER JOIN (
        SELECT s.ctv3Concept, JSON_OBJECT('is_equivalent_to', JSON_OBJECT('id', CONCAT('SN_', s.altConceptId))) as equiv
        FROM read_v3_map_summary s
        WHERE s.multi = TRUE
          AND s.altConceptId IS NOT NULL
    ) t2 ON c.id = CONCAT('R3_', t2.ctv3Concept)
SET data = JSON_MERGE(data, t2.equiv);

-- Add 1:n maps with no alternative overrides (proxy concepts)
UPDATE concept c
    INNER JOIN (
        SELECT s.ctv3Concept, JSON_OBJECT('is_equivalent_to', JSON_OBJECT('id', CONCAT('DS_R3_', s.ctv3Concept))) as equiv
        FROM read_v3_map_summary s
        WHERE s.multi = TRUE
          AND s.altConceptId IS NULL
    ) t2 ON c.id = CONCAT('R3_', t2.ctv3Concept)
SET data = JSON_MERGE(data, t2.equiv);



-- Create PROXY document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/R3-proxy', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- Create proxy concepts
INSERT INTO concept
(document, data)
SELECT @doc, JSON_OBJECT(
        'id', CONCAT('DS_R3_', s.ctv3Concept),
        'name', c.name,
        'description', c.description,
        'is_subtype_of', JSON_OBJECT('id', 'CodeableConcept'),
        'is_related_to', JSON_ARRAYAGG(JSON_OBJECT('id', CONCAT('SN_', t.conceptId)))
    )
FROM read_v3_map_summary s
JOIN read_v3_map_tmp t ON t.ctv3Concept = s.ctv3Concept
JOIN concept c ON c.id = CONCAT('R3_', s.ctv3Concept)
WHERE s.multi = TRUE
  AND s.altConceptId IS NULL
GROUP BY s.ctv3Concept;
