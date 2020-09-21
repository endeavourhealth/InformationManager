USE im_next2;

DROP TABLE IF EXISTS namespace;
CREATE TABLE namespace (
    dbid        INT AUTO_INCREMENT              COMMENT 'Unique namespace DBID',
    iri         VARCHAR(255) NOT NULL           COMMENT 'Namespace iri',
    prefix      VARCHAR(50) NOT NULL            COMMENT 'Namespace default prefix (alias)',
    updated     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY namespace_pk(dbid),
    UNIQUE INDEX namespace_iri_uq (iri),
    UNIQUE INDEX namespace_prefix_uq (prefix)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ---------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS concept_status;
CREATE TABLE concept_status (
    dbid TINYINT NOT NULL,
    name VARCHAR(20) NOT NULL,

    PRIMARY KEY concept_status_pk (dbid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

INSERT INTO concept_status
(dbid, name)
VALUES
(0, 'Draft'),
(1, 'Active'),
(2, 'Inactive');

DROP TABLE IF EXISTS concept;
CREATE TABLE concept
(
    dbid        INT AUTO_INCREMENT,
    namespace   INT NOT NULL,
    id          VARCHAR(36),
    iri         VARCHAR(140) COLLATE utf8_bin NOT NULL,
    name        VARCHAR(256),
    description TEXT,
    code        VARCHAR(50) COLLATE utf8_bin,
    scheme      INT,
    status      TINYINT NOT NULL DEFAULT 0,
    weighting   INT NOT NULL DEFAULT 0,
    updated     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- Used for MRU & deltas

    PRIMARY KEY concept_pk (dbid),

    UNIQUE KEY concept_id_uq (id),
    UNIQUE KEY concept_iri_uq (iri),
    UNIQUE KEY concept_scheme_code_uq (scheme, code),
    INDEX concept_updated_idx (updated),

    FULLTEXT concept_name_ftx (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_axiom;
CREATE TABLE concept_axiom (
   dbid             INT AUTO_INCREMENT,
   axiom            VARCHAR(36),
   concept          INT,
   definition       JSON,
   version          INT,

   PRIMARY KEY concept_axiom_pk (dbid),

   UNIQUE KEY concept_axiom_uq (axiom),
   INDEX concept_axiom_concept_idx (concept)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS concept_property_object;
CREATE TABLE concept_property_object (
    dbid            INT AUTO_INCREMENT,
    concept         INT NOT NULL,
    `group`         TINYINT NOT NULL DEFAULT 0,
    property        INT NOT NULL,
    object          INT NOT NULL,
    minCardinality  INT,
    maxCardinality  INT,
    operator        CHAR(3),
    updated         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY concept_property_object_pk (dbid),
    UNIQUE INDEX concept_property_object_uq (concept, property, object, `group`),
    INDEX concept_property_object_concept_idx (concept),
    INDEX concept_property_object_property_idx (property, object)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_property_data;
CREATE TABLE concept_property_data (
    dbid            INT AUTO_INCREMENT,
    concept         INT NOT NULL,
    `group`         TINYINT NOT NULL DEFAULT 0,
    property        INT NOT NULL,
    data            VARCHAR(512) NOT NULL,     -- Should be TEXT?
    minCardinality  INT,
    maxCardinality  INT,
    operator        CHAR(3),
    updated         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY concept_property_data_pk (dbid),
    UNIQUE INDEX concept_property_data_uq (concept, property, data, `group`),
    INDEX concept_property_data_concept_property_idx (concept, property)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_tct;
CREATE TABLE concept_tct (
    dbid        INT AUTO_INCREMENT,
    source      INT NOT NULL,
    property    INT NOT NULL,
    level       INT NOT NULL,
    target      INT NOT NULL,

    PRIMARY KEY concept_tct_pk (dbid),
    INDEX concept_tct_source_property_idx (source, property),
    INDEX concept_tct_property_level_idx (property, level),
    INDEX concept_tct_property_target_idx (property, target)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS concept_term;
CREATE TABLE concept_term (
    dbid            INT AUTO_INCREMENT,
    concept         INT NOT NULL,
    term            VARCHAR(250),
    updated         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY concept_term_pk (dbid),
    INDEX concept_term_concept_idx (concept)
    -- Term index - full text or regular?!
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ---------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS concept_term_map;
CREATE TABLE concept_term_map
(
    dbid      INT AUTO_INCREMENT,
    term      VARCHAR(250) NOT NULL,
    type      INT          NOT NULL,
    target    INT          NOT NULL,
    updated   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY concept_term_map_pk (dbid),
    UNIQUE INDEX concept_term_map_uq (term, type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ---------------------------------------------------------------------------------------------------------
/*
-- Concept text dictionary and lookup tables

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
    concept BIGINT NOT NULL,

    INDEX concept_word_word_position_idx (word, position),
    UNIQUE INDEX concept_word_concept_position_idx (concept, position)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- DROP WORD MAINTENANCE TRIGGERS HERE (Initially populated with app, triggers created in 99.cleanup) --

DROP TRIGGER IF EXISTS concept_word_trigger_ins;
DROP TRIGGER IF EXISTS concept_word_trigger_upd;

-- ---------------------------------------------------------------------------------------------------------

-- Workflow manager tables
DROP TABLE IF EXISTS workflow_task;
CREATE TABLE workflow_task
(
    dbid      INTEGER AUTO_INCREMENT,
    category  TINYINT NOT NULL  COMMENT '0=Concept mapping, 1=Term mapping',
    userId    CHAR(36) BINARY   COMMENT 'The id of the last user to modify this task',
    userName  VARCHAR(50)       COMMENT 'Their (display) name',
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
*/
