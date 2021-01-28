USE im3;

-- ********************* OPCS4 *********************

DROP TABLE IF EXISTS opcs4;
CREATE TABLE opcs4
(
    code             VARCHAR(10) NOT NULL,
    description      VARCHAR(150),
    altCode          VARCHAR(10) NOT NULL,

    PRIMARY KEY opcs4_pk (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\nhs_opcs4df_9.0.0_20191104000001\\OPCS49 CodesAndTitles Nov 2019 V1.0.txt'
    INTO TABLE opcs4
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    (@code, @description)
    SET code = @code,
        description = @description,
        altCode = REPLACE(@code, '.', '');

-- ********************************************************************************************************************************

-- Create OPCS4 concepts
INSERT IGNORE INTO module (iri) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/OPCS4');
SELECT @module := dbid FROM module WHERE iri = 'http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/OPCS4';

INSERT IGNORE INTO namespace (iri, prefix) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Legacy/OPCS4#', 'opcs4:');
SELECT @namespace := dbid FROM namespace WHERE prefix = 'opcs4:';

SELECT @scheme := dbid FROM concept WHERE iri = ':891041000252103';

INSERT INTO concept
(namespace, module, iri, name, description, type, code, scheme, status)
SELECT DISTINCT @namespace, @module, CONCAT('opcs4:', o.code), IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description), description, 0, o.code, @scheme, 1
FROM opcs4 o;

-- Mapped From axioms
INSERT INTO axiom
(module, concept, type)
SELECT DISTINCT @module, c.dbid, 22 -- AXIOM_TYPE = MAPPED_FROM
FROM opcs4 o
JOIN concept c ON c.iri = CONCAT('opcs4:', o.code)
JOIN icd10_opcs4_maps m ON m.mapTarget = o.altcode AND m.refsetId = 1126441000000105 AND m.active = 1	-- OPCS4
JOIN concept s ON s.iri = CONCAT('sn:', m.referencedComponentId);

-- Mapped From axiom expressions
INSERT INTO expression
(type, axiom, target_concept)
SELECT DISTINCT 0 as `type`, x.dbid AS axiom, s.dbid AS target_concept
FROM opcs4 o
JOIN concept c ON c.iri = CONCAT('opcs4:', o.code)
JOIN icd10_opcs4_maps m ON m.mapTarget = o.altcode AND m.refsetId = 1126441000000105 AND m.active = 1	-- OPCS4
JOIN concept s ON s.iri = CONCAT('sn:', m.referencedComponentId)
JOIN axiom x ON x.concept = c.dbid AND x.type = 22;
