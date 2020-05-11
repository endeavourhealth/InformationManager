DROP TABLE IF EXISTS concept_term_map_meta;
CREATE TABLE concept_term_map_meta
(
    term      VARCHAR(500) NOT NULL,
    context   VARCHAR(140) NOT NULL COLLATE utf8_bin,
    target    VARCHAR(140) NOT NULL COLLATE utf8_bin,
    PRIMARY KEY concept_term_map_pk (term, context)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- #################### LEGACY ENCOUNTER TYPES ####################
INSERT INTO concept_term_map_meta
(term, context, target)
SELECT term, 'LE_TYPE', code
FROM encounter_types;
