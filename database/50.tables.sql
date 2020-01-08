DROP TABLE IF EXISTS model;
CREATE TABLE model (
    dbid    INT AUTO_INCREMENT      COMMENT 'Unique model DBID',
    iri     VARCHAR(255) NOT NULL   COMMENT 'Model iri',
    version VARCHAR(10) NOT NULL    COMMENT 'Version (major.minor.build)',

    PRIMARY KEY model_pk(dbid),
    INDEX model_iri_idx (iri)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept;
CREATE TABLE concept
(
    dbid        INT AUTO_INCREMENT,
    model       INT NOT NULL                COMMENT 'The model the concept belongs to',

    id          VARCHAR(140) COLLATE utf8_bin NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(400),
    codeScheme  INT,
    code        VARCHAR(20) COLLATE utf8_bin,
    status      INT,

    updated     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,                    -- Used for MRU
    weighting   INT NOT NULL DEFAULT 0      COMMENT 'Weighting value',

    PRIMARY KEY concept_pk (dbid),
    UNIQUE KEY concept_id_uq (id),
    UNIQUE KEY concept_code_scheme_uq (code, codeScheme),
    INDEX concept_updated_idx (updated),    -- For MRU
    FULLTEXT concept_name_ftx (name)       -- TODO: Include description?
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_relation;
CREATE TABLE concept_relation (
    dbid            INT AUTO_INCREMENT,
    subject         INT NOT NULL,
    `group`         TINYINT NOT NULL DEFAULT 0,
    relation        INT NOT NULL,
    object          INT,

    PRIMARY KEY object_property_pk (dbid),
    INDEX object_property_subject_idx (subject),
    INDEX object_property_relation_idx (relation),
    INDEX object_property_object_id (object)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_property_data;
CREATE TABLE concept_property_data (
    dbid            INT AUTO_INCREMENT,
    concept         INT NOT NULL,
    relation        INT NOT NULL,
    value           VARCHAR(400),

    PRIMARY KEY concept_property_data_pk (dbid),
    INDEX concept_property_data_concept_idx (concept),
    INDEX concept_property_data_relation_idx (relation)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_relation_cardinality;
CREATE TABLE concept_relation_cardinality (
    dbid            INT NOT NULL,
    minCardinality  INT,
    maxCardinality  INT,
    maxInGroup      INT,

    PRIMARY KEY concept_relation_cardinality_pk (dbid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS document;
CREATE TABLE document
(
    dbid    INT AUTO_INCREMENT              COMMENT 'Unique document DBID',
    id      CHAR(36) NOT NULL,
    iri     VARCHAR(255) NOT NULL,
    model   INT NOT NULL,
    base_model_version VARCHAR(10),
    effective_date  TIMESTAMP,
    status  INT,
    purpose VARCHAR(100),
    publisher INT,
    target_model_version VARCHAR(10),

    PRIMARY KEY document_pk (dbid),
    UNIQUE INDEX document_id_uq (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS transaction;
CREATE TABLE IF NOT EXISTS transaction (
    id INT AUTO_INCREMENT,
    crud INT NOT NULL,
    type INT NOT NULL,
    object INT NOT NULL,

    PRIMARY KEY (id),
    INDEX transaction_object_type_idx (object, type)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS concept_synonym;
CREATE TABLE concept_synonym
(
    dbid    INT NOT NULL            COMMENT 'Concept DBID',
    synonym VARCHAR(255) NOT NULL   COMMENT 'Synonym text',

    INDEX concept_synonym_idx (dbid),
    FULLTEXT concept_synonym_ftx (synonym)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS operator;
CREATE TABLE operator (
    dbid    TINYINT AUTO_INCREMENT,
    name    VARCHAR(20) NOT NULL,

    PRIMARY KEY operator_pk (dbid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS value_set;
CREATE TABLE value_set
(
    dbid        INT AUTO_INCREMENT      COMMENT 'Value set DBID',
    data        JSON NOT NULL           COMMENT 'Value set definition',

    -- Exposed (know) JSON properties
    id VARCHAR(140) COLLATE utf8_bin    GENERATED ALWAYS AS (`data` ->> '$.id') STORED NOT NULL,
    name VARCHAR(255)                   GENERATED ALWAYS AS (`data` ->> '$.name') VIRTUAL,

    PRIMARY KEY value_set_pk (dbid),
    UNIQUE INDEX value_set_id (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS cohort;
CREATE TABLE cohort
(
    dbid        INT AUTO_INCREMENT      COMMENT 'Cohort DBID',
    data        JSON NOT NULL           COMMENT 'Cohort definition JSON',

    -- Exposed (know) JSON properties
    id VARCHAR(140) COLLATE utf8_bin        GENERATED ALWAYS AS (`data` ->> '$.id') STORED NOT NULL,
    name VARCHAR(255)                       GENERATED ALWAYS AS (`data` ->> '$.name') VIRTUAL,

    PRIMARY KEY cohort_pk (dbid),
    UNIQUE INDEX cohort_id (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS data_set;
CREATE TABLE data_set
(
    dbid        INT AUTO_INCREMENT      COMMENT 'Data set DBID',
    data        JSON NOT NULL           COMMENT 'Data set definition JSON',

    -- Exposed (know) JSON properties
    id VARCHAR(140) COLLATE utf8_bin        GENERATED ALWAYS AS (`data` ->> '$.id') STORED NOT NULL,
    name VARCHAR(255)                       GENERATED ALWAYS AS (`data` ->> '$.name') VIRTUAL,

    PRIMARY KEY data_set_pk (dbid),
    UNIQUE INDEX data_set_id (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS state_definition;
CREATE TABLE state_definition
(
    dbid        INT AUTO_INCREMENT      COMMENT 'State definition DBID',
    data        JSON NOT NULL           COMMENT 'State defintion JSON',

    -- Exposed (know) JSON properties
    id VARCHAR(140) COLLATE utf8_bin    GENERATED ALWAYS AS (`data` ->> '$.id') STORED NOT NULL,
    name VARCHAR(255)                   GENERATED ALWAYS AS (`data` ->> '$.name') VIRTUAL,

    PRIMARY KEY state_definition_pk (dbid),
    UNIQUE INDEX state_definition_id (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS word;
CREATE TABLE word (
    dbid INT AUTO_INCREMENT,
    word VARCHAR(250) NOT NULL,
    useCount BIGINT NOT NULL DEFAULT 1,

    PRIMARY KEY word_pk (dbid),
    UNIQUE INDEX word_word_idx (word)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_word;
CREATE TABLE concept_word (
    word INT NOT NULL,
    position TINYINT NOT NULL,
    concept INT NOT NULL,

    INDEX concept_word_word_position_idx (word, position),
    UNIQUE INDEX concept_word_concept_position_idx (concept, position)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- DROP WORD MAINTENANCE TRIGGERS (Populated with app, triggers created in 99.cleanup) --

DROP TRIGGER IF EXISTS concept_word_trigger_ins;
DROP TRIGGER IF EXISTS concept_word_trigger_upd;

-- ---------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS concept_term_map;
CREATE TABLE concept_term_map
(
    term      VARCHAR(250) NOT NULL,
    type      INT          NOT NULL,
    target    INT          NOT NULL,
    PRIMARY KEY concept_term_map_pk (term, type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS im_instance;
CREATE TABLE im_instance
(
    dbid INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    url  VARCHAR(400) NOT NULL,

    PRIMARY KEY im_instance_pk (dbid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Workflow manager tables
DROP TABLE IF EXISTS workflow_task;
CREATE TABLE workflow_task
(
    dbid      INTEGER AUTO_INCREMENT,
    category  TINYINT NOT NULL  COMMENT '0=Concept mapping, 1=Term mapping',
    user_id   CHAR(36) BINARY   COMMENT 'The id of the last user to modify this task',
    user_name VARCHAR(50)       COMMENT 'Their (display) name',
    subject   VARCHAR(100)      COMMENT 'The task subject/name/title',
    status    INT NOT NULL      COMMENT 'Task status - 0=New, 1=In progress, 2=Complete, 3=Archived',
    data      JSON              COMMENT 'The task data',
    created   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY workflow_task_pk (dbid),
    INDEX workflow_task_category (category)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS workflow_task_category;
CREATE TABLE workflow_task_category
(
    dbid TINYINT     NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY workflow_task_category_pk (dbid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Project manager tables
DROP TABLE IF EXISTS project;
CREATE TABLE project (
    dbid        INTEGER AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    brief       VARCHAR(250),
    description TEXT,

    PRIMARY KEY project_pk (dbid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_tct;
CREATE TABLE concept_tct (
    source INT NOT NULL,
    target INT NOT NULL,
    level INT NOT NULL,

    PRIMARY KEY concept_pk (source, target),
    INDEX concept_tct_source_level_idx (source, level),
    INDEX concept_tct_target_idx (target),
    INDEX concept_tct_level_idx (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP PROCEDURE IF EXISTS proc_build_tct;

-- TODO: Rewrite for new db structure
DELIMITER $$
CREATE PROCEDURE `proc_build_tct`(property_id VARCHAR(150))
BEGIN
    SELECT @lvl := 0;

    SELECT @property_dbid := dbid
    FROM concept
    WHERE id = property_id;

    TRUNCATE TABLE concept_tct;

    -- Insert LOWEST level (i.e. concepts without children)
    INSERT INTO concept_tct
    (source, target, level)
    SELECT o.dbid, o.object, 0
    FROM concept_relation o
    LEFT JOIN concept_relation p ON p.object = o.dbid AND p.relation = @property_dbid
    WHERE o.relation = @property_dbid
      AND p.dbid IS NULL;

    SELECT @inserted := ROW_COUNT();

    WHILE @inserted > 0 DO
            SELECT @lvl := @lvl + 1;

            -- Insert parents of last tct entries
            REPLACE INTO concept_tct
            (source, target, level)
            SELECT DISTINCT h.subject, h.object, @lvl
            FROM concept_relation h
            JOIN concept_tct t ON h.dbid = t.target AND t.level = @lvl - 1
            WHERE h.relation = @property_dbid;

            -- Inherit relationships
            REPLACE INTO concept_tct
            (source, target, level)
            SELECT DISTINCT t.source, p.target, @lvl
            FROM concept_tct t
            JOIN concept_tct p ON p.source = t.target
            WHERE t.level = @lvl-1;

            SELECT @inserted := ROW_COUNT();
        END WHILE;
END$$
DELIMITER ;

