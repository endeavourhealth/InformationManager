DROP TABLE IF EXISTS covid_maps;
CREATE TABLE covid_maps (
    legacyId VARCHAR(140) NOT NULL,
    coreId VARCHAR(140) NOT NULL,

    PRIMARY KEY covid_maps_pk(legacyId)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\covid_maps.txt'
    INTO TABLE covid_maps
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;

REPLACE INTO legacy_concept_id_map
(legacyId, coreId)
SELECT legacyId, coreId
FROM covid_maps;

INSERT INTO concept_term_map_meta
(term, context, target)
VALUES
('SARS-CoV-2 RNA DETECTED', 'BC_687309281', 'BC_Fr1gvtFQJAIPqpoHK'),
('SARS-CoV-2 RNA NOT detected', 'BC_687309281', 'BC_ddOYGVC0cM33GsbmH');
