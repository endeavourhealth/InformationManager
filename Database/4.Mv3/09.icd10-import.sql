USE im3;

-- ********************* ICD10 *********************

DROP TABLE IF EXISTS icd10;
CREATE TABLE icd10
(
    code             VARCHAR(10) NOT NULL,
    alt_code         VARCHAR(10) NOT NULL,
    `usage`          VARCHAR(10) NOT NULL,
    usage_uk         TINYINT     NOT NULL,
    description      VARCHAR(200),
    modifier_4       VARCHAR(100),
    modifier_5       VARCHAR(75),
    qualifiers       VARCHAR(400),
    gender_mask      TINYINT,
    min_age          TINYINT,
    max_age          TINYINT,
    tree_description VARCHAR(120),

    PRIMARY KEY icd10_pk (code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\ICD10_Edition5_20160401\\Content\\ICD10_Edition5_CodesAndTitlesAndMetadata_GB_20160401.txt'
    INTO TABLE icd10
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES
    (code, alt_code, `usage`, usage_uk, description, @modifier_4, @modifier_5, @qualifiers, @gender_mask, @min_age, @max_age, @tree_description)
    SET modifier_4 = nullif(@modifier_4, ''),
        modifier_5 = nullif(@modifier_5, ''),
        qualifiers = nullif(@qualifiers, ''),
        gender_mask = nullif(@gender_mask, ''),
        min_age = nullif(@min_age, ''),
        max_age = nullif(@max_age, ''),
        tree_description = nullif(@tree_description, '');

-- ********************************************************************************************************************************

-- Create ICD10 concepts
INSERT IGNORE INTO module (iri) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/ICD10');
SELECT @module := dbid FROM module WHERE iri = 'http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/ICD10';

INSERT IGNORE INTO namespace (iri, prefix) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Legacy/ICD10#', 'icd10:');
SELECT @namespace := dbid FROM namespace WHERE prefix = 'icd10:';

SELECT @scheme := dbid FROM concept WHERE iri = ':891021000252109';

INSERT INTO concept
(namespace, module, iri, name, description, type, code, scheme, status)
SELECT DISTINCT @namespace, @module, CONCAT('icd10:', i.code), IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description), description, 10, i.code, @scheme, 1
FROM icd10 i;

-- Mapped From axioms
INSERT INTO axiom
(module, concept, type)
SELECT DISTINCT @module, c.dbid, 22 -- AXIOM_TYPE = MAPPED_FROM
FROM icd10 i
JOIN concept c ON c.iri = CONCAT('icd10:', i.code)
JOIN icd10_opcs4_maps m ON m.mapTarget = i.alt_code AND m.refsetId = 999002271000000101 AND m.active = 1	-- ICD10
JOIN concept s ON s.iri = CONCAT('sn:', m.referencedComponentId);

-- Mapped From axiom expressions
INSERT INTO expression
(type, axiom, target_concept)
SELECT DISTINCT 0 as `type`, x.dbid AS axiom, s.dbid AS target_concept
FROM icd10 i
JOIN concept c ON c.iri = CONCAT('icd10:', i.code)
JOIN icd10_opcs4_maps m ON m.mapTarget = i.alt_code AND m.refsetId = 999002271000000101 AND m.active = 1	-- ICD10
JOIN concept s ON s.iri = CONCAT('sn:', m.referencedComponentId)
JOIN axiom x ON x.concept = c.dbid AND x.type = 22;
