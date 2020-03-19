DROP TABLE IF EXISTS ontology;
CREATE TABLE ontology (
    id      INT AUTO_INCREMENT      COMMENT 'Unique ontology DBID',
    iri     VARCHAR(255) NOT NULL   COMMENT 'Ontology iri',
    prefix  VARCHAR(10) NOT NULL    COMMENT 'Ontology (default) prefix',
    version VARCHAR(10)             COMMENT 'Version (major.minor.build)',

    PRIMARY KEY model_pk(id),
    UNIQUE INDEX model_iri_uq (iri),
    UNIQUE INDEX model_prefix_uq (prefix)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_definition_type;
CREATE TABLE concept_definition_type (
    id      INT AUTO_INCREMENT,
    name    VARCHAR(50) NOT NULL,

    PRIMARY KEY concept_definition_type_pk (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO concept_definition_type
(id, name)
VALUES
(1, 'Class'),
(2, 'ObjectProperty'),
(3, 'DataProperty'),
(4, 'DataType'),
(5, 'AnnotationProperty');

DROP TABLE IF EXISTS concept;
CREATE TABLE concept
(
    id          INT AUTO_INCREMENT,
    ontology    INT NOT NULL,
    iri         VARCHAR(140) COLLATE utf8_bin NOT NULL,
    type        INT NOT NULL,
    definition  JSON NOT NULL,
    updated     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,                    -- Used for MRU
    weighting   INT NOT NULL DEFAULT 0,

    -- Indexed JSON
    name        VARCHAR(255) GENERATED ALWAYS AS (`definition` ->> '$.name') STORED NOT NULL,

    PRIMARY KEY concept_pk (id),
    UNIQUE KEY concept_iri_uq (ontology, iri),
    INDEX concept_updated_idx (updated),    -- For MRU
    FULLTEXT concept_name_ftx (name)       -- TODO: Include description?
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
