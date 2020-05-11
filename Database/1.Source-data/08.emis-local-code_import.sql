USE im_source;

-- ********************* CONCEPTS *********************

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
    readCode VARCHAR(50) COLLATE utf8_bin,
    readTerm VARCHAR(2) DEFAULT '00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\EMIS\\Coding_ClinicalCode.csv'
    INTO TABLE emis_read_snomed
    FIELDS OPTIONALLY ENCLOSED BY '"' TERMINATED BY ','
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (codeId, codeTerm, readTermId, snomedCTConceptId, snomedCTDescriptionId, nationalCode, nationalCodeCategory, nationalDescription, emisCodeCategoryDescription, processingId, @parentCodeId, readCode, readTerm)
    SET parentCodeId = NULLIF(@parentCodeId, ''),
        readCode = IF (INSTR(readTermId, '-') = 0, readTermId, LEFT(readTermId, INSTR(readTermId, '-')-1)),
        readTerm = IF (LENGTH(readTermId) - INSTR(readTermId, '-') = 1, CONCAT('1', RIGHT(readTermId, 1)), IF (LENGTH(readTermId) - INSTR(readTermId, '-')=2, RIGHT(readTermId, 2), '00'));
