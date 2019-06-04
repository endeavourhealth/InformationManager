-- ********************* CONCEPT *********************

DROP TABLE IF EXISTS opcs4;
CREATE TABLE opcs4
(
    code             VARCHAR(10) NOT NULL,
    description      VARCHAR(150),

    PRIMARY KEY opcs4_pk (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\opcs4\\OPCS48 CodesAndTitles Nov 2016 V1.0.txt'
    INTO TABLE opcs4
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n';

-- ********************* MAPS *********************

DROP TABLE IF EXISTS opcs4_map;
CREATE TABLE opcs4_map
(
    code VARCHAR(10) COLLATE utf8_bin NOT NULL,
    def JSON,
    target VARCHAR(140),
    target_def JSON,

    INDEX opcs4_map_code_pk (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\opcs4\\OPCS map.txt'
    INTO TABLE opcs4_map
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (@count, code, @orig_term, @target, @tgt_term, @target_def, @leg_con, @def, @map_alg, @auth)
    SET target=NULLIF(@target, ''),
        def=NULLIF(@def, ''),
        target_def=NULLIF(@target_def,'')
;
