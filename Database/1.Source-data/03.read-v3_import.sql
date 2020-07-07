USE im_source;

-- CONCEPTS

DROP TABLE IF EXISTS read_v3_concept;
CREATE TABLE read_v3_concept (
                                 code VARCHAR(7) NOT NULL COLLATE utf8_bin       COMMENT 'The CTV3 code',
                                 status  VARCHAR(2) NOT NULL                     COMMENT '(C)urrent/(R)edundant/(O)ptional/(E)xtinct',
                                 role VARCHAR(2) NOT NULL                        COMMENT '(N)on-attribute/(A)ttribute',
                                 categoryId VARCHAR(7) NOT NULL                  COMMENT 'The category id',
                                 PRIMARY KEY read_v3_concept_pk (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\CTV3\\Concept.v3'
    INTO TABLE read_v3_concept
    FIELDS TERMINATED BY '|'
    LINES TERMINATED BY '\r\n'
    (code, status, role, categoryId);

-- DESCRIPTIONS

DROP TABLE IF EXISTS read_v3_desc;
CREATE TABLE read_v3_desc (
                              code VARCHAR(7) NOT NULL COLLATE utf8_bin     COMMENT 'Description id',
                              termId VARCHAR(7) NOT NULL COLLATE utf8_bin     COMMENT 'Term id',
                              type VARCHAR(2) NOT NULL                        COMMENT '(P)referred/(S)ynonym',
                              PRIMARY KEY read_v3_desc_pk (code, type, termId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\CTV3\\Descrip.v3'
    INTO TABLE read_v3_desc
    FIELDS TERMINATED BY '|'
    LINES TERMINATED BY '\r\n'
    (code, termId, type);

-- TERMS

DROP TABLE IF EXISTS read_v3_terms;
CREATE TABLE read_v3_terms (
                               termId  VARCHAR(7) NOT NULL COLLATE utf8_bin    COMMENT 'The CTV3 code',
                               status  VARCHAR(2) NOT NULL                     COMMENT '(C)urrent/(R)edundant/(O)ptional/(E)xtinct',
                               term_31 VARCHAR(31) NOT NULL                    COMMENT 'Term restricted to 31 characters',
                               term_62 VARCHAR(62)                             COMMENT 'Term restricted to 62 characters',
                               term VARCHAR(1024)                              COMMENT 'Full term text',
                               PRIMARY KEY read_v3_terms_code_pk (termId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\CTV3\\Terms.v3'
    INTO TABLE read_v3_terms
    FIELDS TERMINATED BY '|'
    LINES TERMINATED BY '\r\n'
    (termId, status, term_31, @term_62, @term)
    SET term_62 = nullif(@term_62, ''),
        term = nullif(@term, '');

-- HIERARCHY

DROP TABLE IF EXISTS read_v3_hier;
CREATE TABLE read_v3_hier (
                              code VARCHAR(7) NOT NULL COLLATE utf8_bin   COMMENT '',
                              parent VARCHAR(7) NOT NULL COLLATE utf8_bin COMMENT '',
                              `order` INT NOT NULL                        COMMENT '',
                              PRIMARY KEY read_v3_hier_pk (code, parent)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\CTV3\\V3hier.v3'
    INTO TABLE read_v3_hier
    FIELDS TERMINATED BY '|'
    LINES TERMINATED BY '\r\n'
    (code, parent, `order`);

-- MAPS
DROP TABLE IF EXISTS read_v3_map;
CREATE TABLE read_v3_map (
                             id VARCHAR(40) NOT NULL,
                             ctv3Concept VARCHAR(6) COLLATE utf8_bin NOT NULL,
                             ctv3Term VARCHAR(6) COLLATE utf8_bin NOT NULL,
                             ctv3Type VARCHAR(1),
                             conceptId BIGINT,
                             descriptionId BIGINT,
                             status BOOLEAN NOT NULL,
                             effectiveDate VARCHAR(10) NOT NULL,
                             assured BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\nhs_datamigration_29.0.0_20200401000001\\Mapping Tables\\Updated\\Clinically Assured\\ctv3sctmap2_uk_20200401000001.txt'
    INTO TABLE read_v3_map
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (id, ctv3Concept, ctv3Term, @ctv3Type, @conceptId, @descriptionId, status, effectiveDate, assured)
    SET ctv3Type = nullif(@ctv3Type, ''),
        conceptId = nullif(@conceptId, '_DRUG'),
        descriptionId = nullif(@descriptionId, '');


DROP TABLE IF EXISTS read_v3_alt_map;
CREATE TABLE read_v3_alt_map (
                                 ctv3Concept VARCHAR(6) COLLATE utf8_bin NOT NULL,
                                 ctv3Term VARCHAR(6) COLLATE utf8_bin NOT NULL,
                                 conceptId BIGINT,
                                 descriptionId BIGINT,
                                 useAlt VARCHAR(1),
                                 PRIMARY KEY read_v3_alt_map_pk (ctv3Concept, ctv3Term)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\nhs_datamigration_29.0.0_20200401000001\\Mapping Tables\\Updated\\Clinically Assured\\codesWithValues_AlternateMaps_CTV3_20200401000001.txt'
    INTO TABLE read_v3_alt_map
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    IGNORE 1 LINES
    (ctv3Concept, ctv3Term, @conceptId, @descriptionId, @use)
    SET conceptId = nullif(@conceptId, ''),
        descriptionId = nullif(@descriptionId, ''),
        useAlt = nullif(@use, '');

-- Build work table
DROP TABLE IF EXISTS read_v3_summary;
CREATE TABLE read_v3_summary
SELECT
    c.code, ifnull(term_62, term_31) as name, term as description, c.status
FROM read_v3_concept c
         JOIN read_v3_desc d ON d.code = c.code AND d.type = 'P'
         JOIN read_v3_terms t ON t.termId = d.termId AND t.status = 'C'
-- WHERE c.status IN ('C', 'O');   -- (C)urrent, (O)ptional       NOW IMPORT ALL

DROP TABLE IF EXISTS read_v3_map_tmp;
CREATE TABLE read_v3_map_tmp (
                                 ctv3Concept VARCHAR(6) COLLATE utf8_bin NOT NULL,
                                 conceptId BIGINT NOT NULL,
                                 INDEX read_v3_map_tmp (ctv3Concept)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO read_v3_map_tmp
(ctv3Concept, conceptId)
SELECT DISTINCT m.ctv3Concept, m.conceptId
FROM read_v3_map m
WHERE m.status = 1
  AND m.assured = 1
  AND (m.ctv3Type = 'P' OR m.ctv3Type IS NULL)
  AND m.conceptId IS NOT NULL;

-- Populate summary
DROP TABLE IF EXISTS read_v3_map_summary;
CREATE TABLE read_v3_map_summary (
                                     ctv3Concept VARCHAR(6) COLLATE utf8_bin NOT NULL,
                                     multi BOOLEAN DEFAULT FALSE,
                                     altConceptId BIGINT DEFAULT NULL,
                                     PRIMARY KEY read_v3_map_summary_pk (ctv3Concept),
                                     INDEX read_v3_map_summary_idx (multi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO read_v3_map_summary
(ctv3Concept, multi, altConceptId)
SELECT t.ctv3Concept, COUNT(DISTINCT t.conceptId) > 1 as multi, a.conceptId
FROM read_v3_map_tmp t
LEFT JOIN read_v3_alt_map a ON a.ctv3Concept = t.ctv3Concept AND a.conceptId IS NOT NULL AND a.useAlt = 'Y'
GROUP BY t.ctv3Concept;
