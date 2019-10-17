-- Build work tables
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
SELECT t.ctv3Concept, COUNT(DISTINCT c.dbid) > 1 as multi, IF(c.dbid is null, null, a.conceptId) as conceptId
FROM read_v3_map_tmp t
         LEFT JOIN read_v3_alt_map a ON a.ctv3Concept = t.ctv3Concept AND a.conceptId IS NOT NULL AND a.useAlt = 'Y'
         LEFT JOIN concept c ON c.id = CONCAT('SN_', a.conceptId)
GROUP BY t.ctv3Concept;


-- Add 1:1 maps
UPDATE concept_definition cd
JOIN concept c ON c.dbid = cd.concept
JOIN read_v3_map_summary s ON CONCAT('R3_', s.ctv3Concept) = c.id
JOIN read_v3_map_tmp t ON t.ctv3Concept = s.ctv3Concept
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'attribute',
                                                      JSON_OBJECT('property', 'mappedTo', 'valueConcept', JSON_OBJECT('concept', concat('SN_', t.conceptId)))
                                              )
                                      )
                                      )
    )
WHERE s.multi = FALSE
AND s.altConceptId IS NULL;



-- Add 1:n maps with 1:1 (alternative) overrides
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN read_v3_map_summary s ON CONCAT('R3_', s.ctv3Concept) = c.id
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'attribute',
                                                      JSON_OBJECT('property', 'mappedTo', 'valueConcept', JSON_OBJECT('concept', concat('SN_', s.altConceptId)))
                                              )
                                      )
                                      )
    )
WHERE s.multi = TRUE
  AND s.altConceptId IS NOT NULL;

