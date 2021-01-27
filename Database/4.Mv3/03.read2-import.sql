USE im3;

-- ********************* READ V2 *********************

DROP TABLE IF EXISTS read_v2;
CREATE TABLE read_v2 (
    code VARCHAR(6) COLLATE utf8_bin NOT NULL               COMMENT 'The READ code id',
    termid VARCHAR(6) NOT NULL COLLATE utf8_bin NOT NULL    COMMENT 'The term',
    type VARCHAR(1) NOT NULL                                COMMENT '(P)rimary, (S)ynonym, ????',
    children BOOLEAN NOT NULL                               COMMENT 'Has children',
    multiple BOOLEAN NOT NULL                               COMMENT '',
    qualifier BOOLEAN NOT NULL                              COMMENT '',
    status VARCHAR(1) NOT NULL                              COMMENT '(C)urrent, ????',
    contype INT NOT NULL                                    COMMENT '????',
    level INT NOT NULL                                      COMMENT 'Hierarchy level',

    PRIMARY KEY read_v2_code_pk (code, termid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\READ\\DESC.csv'
    INTO TABLE read_v2
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (code, termid, type, children, multiple, qualifier, status, contype, @level)
    SET level = IF(@level='', 0, @level);

-- ********************* READ V2 *********************

DROP TABLE IF EXISTS read_v2_terms;
CREATE TABLE read_v2_terms (
    termid VARCHAR(5) NOT NULL COLLATE utf8_bin,
    termstatus VARCHAR(1) NOT NULL              COMMENT '(C)urrent, ????',
    term30 VARCHAR(30) NOT NULL,
    term60 VARCHAR(60),
    status VARCHAR(1) NOT NULL                  COMMENT '(C)urrent, ????',
    contype INT NOT NULL                        COMMENT '????',
    level INT NOT NULL                          COMMENT 'Hierarchy level',

    PRIMARY KEY  read_v2_terms_pk (termid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\READ\\Term.csv'
    INTO TABLE read_v2_terms
    FIELDS OPTIONALLY ENCLOSED BY '"' TERMINATED BY ','
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (termid, termstatus, term30, @term60, status, contype, level)
    SET term60 = nullif(@term60, '');

-- ********************* READ V2 -> SNOMED MAP *********************

DROP TABLE IF EXISTS read_v2_map;
CREATE TABLE read_v2_map (
    id VARCHAR(40) NOT NULL,
    readCode VARCHAR(6) COLLATE utf8_bin NOT NULL,
    termCode VARCHAR(2) NOT NULL,
    conceptId BIGINT NOT NULL,
    descriptionId BIGINT,
    assured BOOLEAN NOT NULL,
    effectiveDate VARCHAR(10) NOT NULL,
    status BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\SNOMED\\Mapping Tables\\Updated\\Clinically Assured\\rcsctmap2_uk_20200401000001.txt'
    INTO TABLE read_v2_map
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (id, readCode, termCode, conceptId, @descriptionId, assured, effectiveDate, status)
    SET descriptionId = nullif(@descriptionId, '');

ALTER TABLE read_v2_map ADD INDEX read_v2_map_assured_idx (assured, status, termCode);

DROP TABLE IF EXISTS read_v2_alt_map;
CREATE TABLE read_v2_alt_map (
    readCode VARCHAR(6) COLLATE utf8_bin NOT NULL,
    termCode VARCHAR(2) NOT NULL,
    conceptId BIGINT,
    descriptionId BIGINT,
    useAlt VARCHAR(1),
    PRIMARY KEY read_v2_alt_map_pk (readCode, termCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\SNOMED\\Mapping Tables\\Updated\\Clinically Assured\\codesWithValues_AlternateMaps_READ2_20200401000001.txt'
    INTO TABLE read_v2_alt_map
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (readCode, termCode, @conceptId, @descriptionId, @use)
    SET conceptId = nullif(@conceptId, ''),
        descriptionId = nullif(@descriptionId, ''),
        useAlt = nullif(@use, '');

DROP TABLE IF EXISTS read_v2_snomed_map;
CREATE TABLE read_v2_snomed_map (
    readCode VARCHAR(6) COLLATE utf8_bin NOT NULL,
    termCode VARCHAR(2) NOT NULL,
    conceptId BIGINT NOT NULL,
    emisRead VARCHAR(10) COLLATE utf8_bin,

    PRIMARY KEY read_v2_snomed_map_pk (readCode, termCode),
    UNIQUE INDEX read_v2_snomed_map_emis_uq (emisRead)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

REPLACE INTO read_v2_snomed_map
(readCode, termCode, conceptId)
SELECT m.readCode, m.termCode, IFNULL(a.conceptId, m.conceptId)
FROM read_v2_map m
         LEFT JOIN read_v2_alt_map a ON a.readCode = m.readCode AND a.termCode = m.termCode AND a.useAlt = 'Y'
WHERE m.status = 1
ORDER BY m.readCode, m.termCode, m.assured DESC, m.effectiveDate -- Ensure latest for each row

-- Generate EMIS read from standard read

UPDATE im3.read_v2_snomed_map
SET emisRead = REPLACE(readCode, '.', '')
WHERE termCode = '00';

UPDATE im3.read_v2_snomed_map
SET emisRead = CONCAT(REPLACE(readCode, '.', ''), '-', SUBSTR(termCode,2))
WHERE termCode LIKE '1%';

UPDATE im3.read_v2_snomed_map
SET emisRead = CONCAT(REPLACE(readCode, '.', ''), '-', termCode)
WHERE emisRead IS NULL;

-- Create concepts
INSERT IGNORE INTO module (iri) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/READ2');
SELECT @module := dbid FROM module WHERE iri = 'http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/READ2';

INSERT IGNORE INTO namespace (iri, prefix) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Legacy/READ2#', 'r2:');
SELECT @namespace := dbid FROM namespace WHERE prefix = 'r2:';

SELECT @scheme := dbid FROM concept WHERE iri = ':891141000252104';

INSERT INTO concept
(namespace, module, iri, name, description, type, code, scheme, status)
SELECT DISTINCT @namespace, @module, CONCAT('r2:', r.code), t.term60, t.term60, 0, r.code, @scheme, 1
FROM im3.read_v2 r
JOIN im3.read_v2_terms t ON t.termid = r.termid
WHERE type = 'P';

-- Mapped From axioms
INSERT INTO axiom
(module, concept, type)
SELECT DISTINCT @module, c.dbid, 22 -- AXIOM_TYPE = MAPPED_FROM
FROM read_v2_snomed_map m
JOIN concept c ON c.iri = CONCAT('r2:', m.readCode)
JOIN concept s ON s.iri = CONCAT('sn:', m.conceptId)
WHERE m.termCode = '00';

-- Mapped From axiom expressions
INSERT INTO expression
(type, axiom, target_concept)
SELECT DISTINCT 0 as `type`, x.dbid AS axiom, s.dbid AS target_concept
FROM read_v2_snomed_map m
JOIN concept c ON c.iri = CONCAT('r2:', m.readCode)
JOIN concept s ON s.iri = CONCAT('sn:', m.conceptId)
JOIN axiom x ON x.concept = c.dbid
WHERE m.termCode = '00';
