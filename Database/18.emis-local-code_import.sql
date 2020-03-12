DROP TABLE IF EXISTS emis_local_codes;
CREATE TABLE emis_local_codes (
    local_code   VARCHAR(20) NOT NULL COLLATE utf8_bin,
    local_term   VARCHAR(400),
    code_type    VARCHAR(50),

    PRIMARY KEY emis_local_codes_pk (local_code, local_term),
    INDEX emis_local_codes_code_idx (local_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\emis_local_codes.csv'
    INTO TABLE emis_local_codes
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES;

DROP TABLE IF EXISTS emis_read_snomed;
CREATE TABLE emis_read_snomed (
    medication BOOLEAN,
    code_id BIGINT NOT NULL,
    code_type VARCHAR(50),
    read_term VARCHAR(200),
    read_code VARCHAR(50) COLLATE utf8_bin,
    snomed_concept_id BIGINT,
    snomed_description_id BIGINT,
    snomed_term VARCHAR(200),
    national_code INT,
    national_code_category INT,
    national_code_description VARCHAR(200),
    parent_code_id BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\emis_read_snomed.csv'
    INTO TABLE emis_read_snomed
    FIELDS OPTIONALLY ENCLOSED BY '"' TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES
    (medication, code_id, code_type, read_term, @read_code, snomed_concept_id, snomed_description_id, snomed_term, national_code, national_code_category, national_code_description, parent_code_id)
    SET read_code = IF(LENGTH(@read_code) < 5, CONCAT(@read_code, REPEAT('.', 5-LENGTH(@read_code))) , @read_code);
