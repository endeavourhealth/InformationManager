USE im_v1_meta;

-- #################### TABLE ####################
DROP TABLE IF EXISTS concept_term_map_meta;
CREATE TABLE concept_term_map_meta (
    term varchar(250) NOT NULL,
    type VARCHAR(150) NOT NULL,
    target VARCHAR(150) NOT NULL,
    updated datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- #################### ENCOUNTERS ####################
INSERT INTO concept_term_map_meta
(term, type, target)
SELECT term, 'LENC', code
FROM im_source.encounter_types;

-- #################### BARTS ####################
INSERT INTO concept_term_map_meta
(term, type, target)
SELECT term, 'BC_687309281', target
FROM im_source.barts_cerner_term_maps;
