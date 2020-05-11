USE im_source;

-- ********************* CONCEPTS *********************

DROP TABLE IF EXISTS encounter_types;

CREATE TABLE encounter_types (
    count INT,
    legacy_iri VARCHAR(140) COLLATE utf8_bin NOT NULL,
    code VARCHAR(40),
    term VARCHAR(255),
    core_iri VARCHAR(140) COLLATE utf8_bin NOT NULL,
    core_term VARCHAR(255),

    PRIMARY KEY encounter_types_pk(legacy_iri)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\Core\\IMMapEncounters.txt'
    INTO TABLE encounter_types
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES;
