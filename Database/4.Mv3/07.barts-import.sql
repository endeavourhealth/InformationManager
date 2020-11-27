USE im3;

DROP TABLE IF EXISTS barts_local_codes;
CREATE TABLE barts_local_codes (
    code VARCHAR(40) NOT NULL COLLATE utf8_bin,
    term VARCHAR(300) NOT NULL,
    snomed BIGINT,

    PRIMARY KEY barts_local_codes (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\code_export_barts.txt'
    INTO TABLE barts_local_codes
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (term, code, snomed);

SELECT * FROM barts_local_codes;

-- ********************************************************************************************************************************

-- Create concepts
INSERT IGNORE INTO module (iri) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/BARTS');
SELECT @module := dbid FROM module WHERE iri = 'http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/BARTS';

INSERT IGNORE INTO namespace (iri, prefix) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Legacy/Barts_Cerner#', 'bc:');
SELECT @namespace := dbid FROM namespace WHERE prefix = 'bc:';

SELECT @scheme := dbid FROM concept WHERE iri = ':891081000252108';

INSERT INTO concept
(namespace, module, iri, name, description, type, code, scheme, status)
SELECT DISTINCT @namespace, @module, REPLACE(t.code, 'BC_', 'bc:'), t.term, t.term, 0, REPLACE(t.code, 'BC_', ''), @scheme, 1
FROM barts_local_codes t;

-- Subtype (classification)
INSERT INTO classification
(child, parent, module)
SELECT DISTINCT c.dbid, s.dbid, @module
FROM barts_local_codes m
JOIN concept c ON c.iri = REPLACE(m.code, 'BC_', 'bc:')
JOIN concept s ON s.iri = CONCAT('sn:', m.snomed);
