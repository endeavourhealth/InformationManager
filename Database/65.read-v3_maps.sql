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

-- Common/useful concept
SELECT @equiv := dbid FROM concept WHERE id = 'is_equivalent_to';
SELECT @related := dbid FROM concept WHERE id = 'is_related_to';
SELECT @subtype := dbid FROM concept WHERE id = 'is_subtype_of';
SELECT @prefix := dbid FROM concept WHERE id = 'code_prefix';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';

-- Add 1:1 maps
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @equiv, v.dbid
FROM read_v3_map_summary s
JOIN read_v3_map_tmp t ON t.ctv3Concept = s.ctv3Concept
JOIN concept c ON c.id = CONCAT('R3_', s.ctv3Concept)
JOIN concept v ON v.id = CONCAT('SN_', t.conceptId)
WHERE s.multi = FALSE;

-- Add 1:n maps with 1:1 (alternative) overrides
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @equiv, v.dbid
FROM read_v3_map_summary s
JOIN concept c ON c.id = CONCAT('R3_', s.ctv3Concept)
JOIN concept v ON v.id = CONCAT('SN_', s.altConceptId)
WHERE s.multi = TRUE
AND s.altConceptId IS NOT NULL;

-- Create PROXY document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/R3-proxy', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- Create proxy concepts
INSERT INTO concept (document, id, name, description)
SELECT @doc, CONCAT('DS_R3_', s.ctv3Concept), c.name, c.description
FROM read_v3_map_summary s
JOIN read_v3_map_tmp t ON t.ctv3Concept = s.ctv3Concept
JOIN concept c ON c.id = CONCAT('R3_', s.ctv3Concept)
WHERE s.multi = TRUE
AND s.altConceptId IS NULL;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM read_v3_map_summary s
JOIN read_v3_map_tmp t ON t.ctv3Concept = s.ctv3Concept
JOIN concept c ON c.id = CONCAT('R3_', s.ctv3Concept)
WHERE s.multi = TRUE
AND s.altConceptId IS NULL;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @related, v.dbid
FROM read_v3_map_summary s
JOIN read_v3_map_tmp t ON t.ctv3Concept = s.ctv3Concept
JOIN concept c ON c.id = CONCAT('R3_', s.ctv3Concept)
JOIN concept v ON v.id = CONCAT('SN_', t.conceptId)
WHERE s.multi = TRUE
AND s.altConceptId IS NULL;

-- Add 1:n maps with no alternative overrides (proxy concepts)
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @equiv, v.dbid
FROM read_v3_map_summary s
JOIN concept c ON c.id = CONCAT('R3_', s.ctv3Concept)
JOIN concept v ON v.id = CONCAT('DS_R3_', s.ctv3Concept)
WHERE s.multi = TRUE
AND s.altConceptId IS NULL;
