USE im3;

DROP TABLE IF EXISTS emis_read_snomed;
CREATE TABLE emis_read_snomed (
    codeId BIGINT,
    codeTerm VARCHAR(300) NOT NULL,
    readTermId VARCHAR(50) COLLATE utf8_bin,
    snomedCTConceptId BIGINT,
    snomedCTDescriptionId BIGINT,
    nationalCode VARCHAR(10),
    nationalCodeCategory VARCHAR(100),
    nationalDescription VARCHAR(300),
    emisCodeCategoryDescription VARCHAR(200),
    processingId INT,
    parentCodeId BIGINT,
    snomed_final BIGINT,

    INDEX emis_read_snomed_idx (codeId),
    INDEX emis_read_snomed_term_idx (readTermId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\373783-374080_Coding_ClinicalCode_20201118063100_B3FA020B-05FC-4196-A4DE-E91B70BEF1D2.csv'
    INTO TABLE emis_read_snomed
    FIELDS OPTIONALLY ENCLOSED BY '"' TERMINATED BY ','
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (codeId, codeTerm, readTermId, snomedCTConceptId, snomedCTDescriptionId, nationalCode, nationalCodeCategory, nationalDescription, emisCodeCategoryDescription, processingId, @parentCodeId)
    SET parentCodeId = NULLIF(@parentCodeId, '');

-- Set map where official exists
UPDATE emis_read_snomed e
JOIN read_v2_snomed_map m ON m.emisRead = e.readTermId
SET e.snomed_final = m.conceptId;

-- Set where snomed is real
UPDATE emis_read_snomed e
JOIN concept c ON c.iri = CONCAT('sn:', e.snomedCTConceptId)
SET e.snomed_final = e.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- ********************************************************************************************************************************

-- Create concepts
INSERT IGNORE INTO module (iri) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/EMIS');
SELECT @module := dbid FROM module WHERE iri = 'http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/EMIS';

INSERT IGNORE INTO namespace (iri, prefix) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Legacy/EMIS#', 'emis:');
SELECT @namespace := dbid FROM namespace WHERE prefix = 'emis:';

SELECT @scheme := dbid FROM concept WHERE iri = ':891031000252107';

INSERT INTO concept
(namespace, module, iri, name, description, type, code, scheme, status)
SELECT DISTINCT @namespace, @module, CONCAT('emis:', r.readTermId), r.codeTerm, r.codeTerm, 10, r.readTermId, @scheme, 1
FROM emis_read_snomed r
LEFT JOIN read_v2_snomed_map m ON m.emisRead = r.readTermId
WHERE r.snomed_final IS NOT NULL
AND m.emisRead IS NULL;

-- Mapped From axioms
INSERT INTO axiom
(module, concept, type)
SELECT DISTINCT @module, c.dbid, 22 -- AXIOM_TYPE = MAPPED_FROM
FROM emis_read_snomed m
JOIN concept c ON c.iri = CONCAT('emis:', m.readTermId)
JOIN concept s ON s.iri = CONCAT('sn:', m.snomed_final);

-- Mapped From axiom expressions
INSERT INTO expression
(type, axiom, target_concept)
SELECT DISTINCT 0 as `type`, x.dbid AS axiom, s.dbid AS target_concept
FROM emis_read_snomed m
JOIN concept c ON c.iri = CONCAT('emis:', m.readTermId)
JOIN concept s ON s.iri = CONCAT('sn:', m.snomed_final)
JOIN axiom x ON x.concept = c.dbid AND x.type = 22;
