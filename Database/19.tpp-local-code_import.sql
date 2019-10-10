DROP TABLE IF EXISTS tpp_local_codes;
CREATE TABLE tpp_local_codes (
                                  ctv3_code   VARCHAR(6) NOT NULL COLLATE utf8_bin,
                                  ctv3_text   VARCHAR(400) NOT NULL,

                                  PRIMARY KEY tpp_local_codes_pk (ctv3_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\tpp_local_codes.txt'
    INTO TABLE tpp_local_codes
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES;
