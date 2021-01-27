USE im3;

DROP TABLE IF EXISTS tpp_local_codes;
CREATE TABLE tpp_local_codes (
    ctv3_code VARCHAR(6) NOT NULL COLLATE utf8_bin,
    ctv3_term VARCHAR(300) NOT NULL,
    snomed_concept_id BIGINT,
    is_tpp_code BOOLEAN,

    PRIMARY KEY tpp_local_codes_pk (ctv3_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\code_export_tpp.txt'
    INTO TABLE tpp_local_codes
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (ctv3_code, ctv3_term, @snomed_concept_id, is_tpp_code)
    SET snomed_concept_id = nullif(@snomed_concept_id, '');

-- Remove non-local codes
DELETE FROM tpp_local_codes
WHERE ctv3_code NOT LIKE 'Y%';

-- ********************************************************************************************************************************

-- Create concepts
INSERT IGNORE INTO module (iri) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/TPP');
SELECT @module := dbid FROM module WHERE iri = 'http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/TPP';

INSERT IGNORE INTO namespace (iri, prefix) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Legacy/TPP#', 'tpp:');
SELECT @namespace := dbid FROM namespace WHERE prefix = 'tpp:';

SELECT @scheme := dbid FROM concept WHERE iri = ':631000252102';

INSERT INTO concept
(namespace, module, iri, name, description, type, code, scheme, status)
SELECT DISTINCT @namespace, @module, CONCAT('tpp:', t.ctv3_code), t.ctv3_term, t.ctv3_term, 0, t.ctv3_code, @scheme, 1
FROM tpp_local_codes t;

-- Mapped From axioms
INSERT INTO axiom
(module, concept, type)
SELECT DISTINCT @module, c.dbid, 22 -- AXIOM_TYPE = MAPPED_FROM
FROM tpp_local_codes m
JOIN concept c ON c.iri = CONCAT('tpp:', m.ctv3_code)
JOIN concept s ON s.iri = CONCAT('sn:', m.snomed_concept_id)
WHERE m.snomed_concept_id IS NOT NULL;

-- Mapped From axiom expressions
INSERT INTO expression
(type, axiom, target_concept)
SELECT DISTINCT 0 as `type`, x.dbid AS axiom, s.dbid AS target_concept
FROM tpp_local_codes m
JOIN concept c ON c.iri = CONCAT('tpp:', m.ctv3_code)
JOIN concept s ON s.iri = CONCAT('sn:', m.snomed_concept_id)
JOIN axiom x ON x.concept = c.dbid
WHERE m.snomed_concept_id IS NOT NULL;
