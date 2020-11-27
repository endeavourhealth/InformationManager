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

    INDEX emis_read_snomed_idx (codeId)
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

-- Fill in with parents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p1.snomedCTConceptId)
SET e.snomed_final = p1.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p2.snomedCTConceptId)
SET e.snomed_final = p2.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p3.snomedCTConceptId)
SET e.snomed_final = p3.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-great-grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN emis_read_snomed p4 ON p4.codeId = p3.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p4.snomedCTConceptId)
SET e.snomed_final = p4.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-great-great-grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN emis_read_snomed p4 ON p4.codeId = p3.parentCodeId
JOIN emis_read_snomed p5 ON p5.codeId = p4.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p5.snomedCTConceptId)
SET e.snomed_final = p5.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-great-great-grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN emis_read_snomed p4 ON p4.codeId = p3.parentCodeId
JOIN emis_read_snomed p5 ON p5.codeId = p4.parentCodeId
JOIN emis_read_snomed p6 ON p6.codeId = p5.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p6.snomedCTConceptId)
SET e.snomed_final = p6.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-great-great-grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN emis_read_snomed p4 ON p4.codeId = p3.parentCodeId
JOIN emis_read_snomed p5 ON p5.codeId = p4.parentCodeId
JOIN emis_read_snomed p6 ON p6.codeId = p5.parentCodeId
JOIN emis_read_snomed p7 ON p7.codeId = p6.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p7.snomedCTConceptId)
SET e.snomed_final = p7.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-great-great-grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN emis_read_snomed p4 ON p4.codeId = p3.parentCodeId
JOIN emis_read_snomed p5 ON p5.codeId = p4.parentCodeId
JOIN emis_read_snomed p6 ON p6.codeId = p5.parentCodeId
JOIN emis_read_snomed p7 ON p7.codeId = p6.parentCodeId
JOIN emis_read_snomed p8 ON p8.codeId = p7.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p8.snomedCTConceptId)
SET e.snomed_final = p8.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-great-great-grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN emis_read_snomed p4 ON p4.codeId = p3.parentCodeId
JOIN emis_read_snomed p5 ON p5.codeId = p4.parentCodeId
JOIN emis_read_snomed p6 ON p6.codeId = p5.parentCodeId
JOIN emis_read_snomed p7 ON p7.codeId = p6.parentCodeId
JOIN emis_read_snomed p8 ON p8.codeId = p7.parentCodeId
JOIN emis_read_snomed p9 ON p9.codeId = p8.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p9.snomedCTConceptId)
SET e.snomed_final = p9.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-great-great-grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN emis_read_snomed p4 ON p4.codeId = p3.parentCodeId
JOIN emis_read_snomed p5 ON p5.codeId = p4.parentCodeId
JOIN emis_read_snomed p6 ON p6.codeId = p5.parentCodeId
JOIN emis_read_snomed p7 ON p7.codeId = p6.parentCodeId
JOIN emis_read_snomed p8 ON p8.codeId = p7.parentCodeId
JOIN emis_read_snomed p9 ON p9.codeId = p8.parentCodeId
JOIN emis_read_snomed p10 ON p10.codeId = p9.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p10.snomedCTConceptId)
SET e.snomed_final = p10.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-great-great-grandparents
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN emis_read_snomed p4 ON p4.codeId = p3.parentCodeId
JOIN emis_read_snomed p5 ON p5.codeId = p4.parentCodeId
JOIN emis_read_snomed p6 ON p6.codeId = p5.parentCodeId
JOIN emis_read_snomed p7 ON p7.codeId = p6.parentCodeId
JOIN emis_read_snomed p8 ON p8.codeId = p7.parentCodeId
JOIN emis_read_snomed p9 ON p9.codeId = p8.parentCodeId
JOIN emis_read_snomed p10 ON p10.codeId = p9.parentCodeId
JOIN emis_read_snomed p11 ON p11.codeId = p10.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p11.snomedCTConceptId)
SET e.snomed_final = p11.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- Fill in with great-great-great-grandparents **** NO MORE AT OR BEYOND THIS LEVEL
UPDATE emis_read_snomed e
JOIN emis_read_snomed p1 ON p1.codeId = e.parentCodeId
JOIN emis_read_snomed p2 ON p2.codeId = p1.parentCodeId
JOIN emis_read_snomed p3 ON p3.codeId = p2.parentCodeId
JOIN emis_read_snomed p4 ON p4.codeId = p3.parentCodeId
JOIN emis_read_snomed p5 ON p5.codeId = p4.parentCodeId
JOIN emis_read_snomed p6 ON p6.codeId = p5.parentCodeId
JOIN emis_read_snomed p7 ON p7.codeId = p6.parentCodeId
JOIN emis_read_snomed p8 ON p8.codeId = p7.parentCodeId
JOIN emis_read_snomed p9 ON p9.codeId = p8.parentCodeId
JOIN emis_read_snomed p10 ON p10.codeId = p9.parentCodeId
JOIN emis_read_snomed p11 ON p11.codeId = p10.parentCodeId
JOIN emis_read_snomed p12 ON p12.codeId = p11.parentCodeId
JOIN concept c ON c.iri = CONCAT('sn:', p12.snomedCTConceptId)
SET e.snomed_final = p12.snomedCTConceptId
WHERE e.snomed_final IS NULL;

-- Delete unmapped ancestries where mapped one exists
DELETE e1
FROM emis_read_snomed e1
JOIN emis_read_snomed e2 ON e2.codeId = e1.codeId AND e2.snomed_final IS NOT NULL
WHERE e1.snomed_final IS NULL;

-- ********************************************************************************************************************************

-- Create concepts
INSERT IGNORE INTO module (iri) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/EMIS');
SELECT @module := dbid FROM module WHERE iri = 'http://www.DiscoveryDataService.org/InformationModel/Module/Legacy/EMIS';

INSERT IGNORE INTO namespace (iri, prefix) VALUE ('http://www.DiscoveryDataService.org/InformationModel/Legacy/EMIS#', 'emis:');
SELECT @namespace := dbid FROM namespace WHERE prefix = 'emis:';

SELECT @scheme := dbid FROM concept WHERE iri = ':891031000252107';

INSERT INTO concept
(namespace, module, iri, name, description, type, code, scheme, status)
SELECT DISTINCT @namespace, @module, CONCAT('emis:', r.readTermId), r.codeTerm, r.codeTerm, 0, r.readTermId, @scheme, 1
FROM emis_read_snomed r
LEFT JOIN read_v2_snomed_map m ON m.emisRead = r.readTermId
WHERE r.snomed_final IS NOT NULL
AND m.emisRead IS NULL;

-- Subtype (classification)
INSERT INTO classification
(child, parent, module)
SELECT DISTINCT c.dbid, s.dbid, @module
FROM emis_read_snomed m
JOIN concept c ON c.iri = CONCAT('emis:', m.readTermId)
JOIN concept s ON s.iri = CONCAT('sn:', m.snomed_final);
