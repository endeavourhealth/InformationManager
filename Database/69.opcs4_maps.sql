-- Map all

UPDATE concept c
INNER JOIN opcs4_map m ON CONCAT('O4_', m.code) = c.id AND m.target IS NOT NULL
INNER JOIN concept s ON s.id = m.target
SET c.data = JSON_MERGE(c.data, JSON_OBJECT('is_equivalent_to', JSON_OBJECT('id', m.target)));

-- Create PROXY document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/O4-proxy', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- Add missing proxy concepts
INSERT INTO concept
(document, data)
SELECT @doc, target_def
FROM opcs4_map
WHERE target_def IS NOT NULL;

