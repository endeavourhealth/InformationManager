-- Create PROXY document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/O4-proxy', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- Add missing proxy concepts
INSERT INTO concept (document, id, name)
VALUES (@doc, 'DC_OPCS_2', 'delivery of a fraction of external beam radiotherapy nec');

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, p.dbid, v.dbid FROM concept c JOIN concept p ON p.id = 'isA' JOIN concept v ON v.id = 'CodeableConcept' WHERE c.id = 'DC_OPCS_2';

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, p.dbid, v.dbid FROM concept c JOIN concept p ON p.id = 'isRelatedTo' JOIN concept v ON v.id = 'SN_33195004' WHERE c.id = 'DC_OPCS_2';

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, p.dbid, v.dbid FROM concept c JOIN concept p ON p.id = 'SN_424244007' JOIN concept v ON v.id = 'SN_115468007' WHERE c.id = 'DC_OPCS_2';

-- Map all
INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, p.dbid, v.dbid
FROM opcs4_map m
JOIN concept c ON c.id = CONCAT('O4_', m.code)
JOIN concept p ON p.id = 'isEquivalentTo'
JOIN concept v ON v.id = m.target
WHERE m.target IS NOT NULL;

