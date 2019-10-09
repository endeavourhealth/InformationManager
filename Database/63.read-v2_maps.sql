-- Build working tables
DROP TABLE IF EXISTS read_v2_map_tmp;
CREATE TABLE read_v2_map_tmp (
    readCode VARCHAR(6) COLLATE utf8_bin NOT NULL,
    termCode VARCHAR(2) NOT NULL,
    conceptId BIGINT NOT NULL,
    INDEX read_v2_map_tmp_idx (readCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO read_v2_map_tmp
(readCode, termCode, conceptId)
SELECT m.readCode, m.termCode, m.conceptId
FROM read_v2_map m
         JOIN concept c ON c.id = CONCAT('SN_', m.conceptId)
WHERE m.assured = 1
  AND m.status = 1;

-- Populate summary
DROP TABLE IF EXISTS read_v2_map_summary;
CREATE TABLE read_v2_map_summary (
                                     readCode VARCHAR(6) COLLATE utf8_bin NOT NULL,
                                     termCode VARCHAR(2) NOT NULL,
                                     multi BOOLEAN DEFAULT FALSE,
                                     altConceptId BIGINT DEFAULT NULL,
                                     PRIMARY KEY read_v2_map_summary_pk (readCode, termCode),
                                     INDEX read_v2_map_summary_multi_idx (multi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO read_v2_map_summary
(readCode, termCode, multi, altConceptId)
SELECT t.readCode, t.termCode, COUNT(DISTINCT c.dbid) > 1 as multi, IF(c.dbid is null, null, a.conceptId) as conceptId
FROM read_v2_map_tmp t
LEFT JOIN read_v2_alt_map a ON a.readCode = t.readCode AND a.termCode = t.termCode AND a.conceptId IS NOT NULL AND a.useAlt = 'Y'
LEFT JOIN concept c ON c.id = CONCAT('SN_', a.conceptId)
GROUP BY t.readCode, t.termCode;

-- Add 1:1 maps
UPDATE concept_definition cd
JOIN concept c ON c.dbid = cd.concept
JOIN read_v2_map_summary s ON s.readCode = c.code AND c.scheme = 'READ2'
JOIN read_v2_map_tmp t ON t.readCode = s.readCode AND t.termCode = s.termCode
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'attribute', JSON_ARRAY(
                                                              JSON_OBJECT('property', 'mappedTo', 'valueConcept', concat('SN_', t.conceptId))
                                                          )
                                              )
                                      )
                                      )
    )
WHERE s.multi = false
  AND s.altConceptId IS NULL
  AND t.conceptId IS NOT NULL
  AND t.termCode = '00';

UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN read_v2_map_summary s ON CONCAT(s.readCode, s.termCode) = c.code AND c.scheme = 'READ2'
    JOIN read_v2_map_tmp t ON t.readCode = s.readCode AND t.termCode = s.termCode
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'attribute', JSON_ARRAY(
                                                              JSON_OBJECT('property', 'mappedTo', 'valueConcept', concat('SN_', t.conceptId))
                                                          )
                                              )
                                      )
                                      )
    )
WHERE s.multi = false
  AND s.altConceptId IS NULL
  AND t.conceptId IS NOT NULL
  AND t.termCode <> '00';

-- Add 1:n maps with 1:1 (alternative) overrides
UPDATE concept_definition cd
JOIN concept c ON c.dbid = cd.concept
JOIN read_v2_map_summary s ON s.readCode = c.code AND c.scheme = 'READ2'
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'attribute', JSON_ARRAY(
                                                              JSON_OBJECT('property', 'mappedTo', 'valueConcept', concat('SN_', s.altConceptId))
                                                          )
                                              )
                                      )
                                      )
    )

WHERE s.multi = false
  AND s.altConceptId IS NOT NULL
AND s.termCode = '00';

UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN read_v2_map_summary s ON CONCAT(s.readCode, s.termCode) = c.code AND c.scheme = 'READ2'
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'attribute', JSON_ARRAY(
                                                              JSON_OBJECT('property', 'mappedTo', 'valueConcept', concat('SN_', s.altConceptId))
                                                          )
                                              )
                                      )
                                      )
    )

WHERE s.multi = false
  AND s.altConceptId IS NOT NULL
  AND s.termCode <> '00';


/*  EMIS READ2 -> ACTUAL READ2
SELECT id, CONCAT(
        SUBSTR(id, 1, INSTR(id, '-')-1),
        REPEAT('.', 9 - INSTR(id, '-')),
        IF(SUBSTR(id, INSTR(id, '-')+1) < 50, SUBSTR(id, INSTR(id, '-')+1)+10, SUBSTR(id, INSTR(id, '-')+1))
    )
FROM im_ceg.concept
WHERE draft = 1
  AND id LIKE 'R2_%-%';
*/
