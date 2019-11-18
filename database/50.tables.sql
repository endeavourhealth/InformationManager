DROP TABLE IF EXISTS concept_tct;
DROP TABLE IF EXISTS workflow_task_category;
DROP TABLE IF EXISTS im_instance;
DROP TABLE IF EXISTS concept_archive;
DROP TABLE IF EXISTS concept_term_map;
DROP TABLE IF EXISTS state_definition;
DROP TABLE IF EXISTS data_set;
DROP TABLE IF EXISTS cohort;
DROP TABLE IF EXISTS value_set;
DROP TABLE IF EXISTS data_type;
DROP TABLE IF EXISTS property_range;
DROP TABLE IF EXISTS property_domain;
DROP TABLE IF EXISTS constraint_type;
DROP TABLE IF EXISTS role_group_attribute;
DROP TABLE IF EXISTS concept_definition_role_group;
DROP TABLE IF EXISTS concept_definition_concept;
DROP TABLE IF EXISTS concept_definition_type;
DROP TABLE IF EXISTS operator;
DROP TABLE IF EXISTS concept_synonym;
DROP TABLE IF EXISTS document;
DROP TABLE IF EXISTS concept;
DROP TABLE IF EXISTS model;

CREATE TABLE model (
    dbid    INT AUTO_INCREMENT      COMMENT 'Unique model DBID',
    iri     VARCHAR(255) NOT NULL   COMMENT 'Model iri',
    version VARCHAR(10) NOT NULL    COMMENT 'Version (major.minor.build)',

    PRIMARY KEY model_pk(dbid),
    INDEX model_iri_idx (iri)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE concept
(
    dbid        INT AUTO_INCREMENT,
    model       INT NOT NULL                COMMENT 'The model the concept belongs to',

    id VARCHAR(140) COLLATE utf8_bin NOT NULL,
    name VARCHAR(255),
    description VARCHAR(400),
    scheme VARCHAR(140),
    code VARCHAR(20) COLLATE utf8_bin,
    status VARCHAR(140) COLLATE utf8_bin,

    updated     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,                    -- Used for MRU
    weighting   INT NOT NULL DEFAULT 0      COMMENT 'Weighting value',

    PRIMARY KEY concept_dbid_pk (dbid),
    UNIQUE KEY concept_id_uq (id),
    UNIQUE KEY concept_code_scheme (code, scheme),
    INDEX concept_updated_idx (updated),    -- For MRU
    FULLTEXT concept_name_ftx (name)       -- TODO: Include description?
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE document
(
    dbid    INT AUTO_INCREMENT              COMMENT 'Unique document DBID',
    id      CHAR(36) NOT NULL,
    iri     VARCHAR(255) NOT NULL,
    model   INT NOT NULL,
    base_model_version VARCHAR(10),
    effective_date  TIMESTAMP,
    status  INT,
    purpose VARCHAR(20),
    publisher INT,
    target_model_version VARCHAR(10),

    PRIMARY KEY document_pk (dbid),
    UNIQUE INDEX document_id_uq (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE concept_synonym
(
    dbid    INT NOT NULL            COMMENT 'Concept DBID',
    synonym VARCHAR(255) NOT NULL   COMMENT 'Synonym text',

    INDEX concept_synonym_idx (dbid),
    FULLTEXT concept_synonym_ftx (synonym)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE operator (
    dbid    TINYINT AUTO_INCREMENT,
    name    VARCHAR(20) NOT NULL,

    PRIMARY KEY operator_pk (dbid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

CREATE TABLE concept_definition_type (
    dbid    INT NOT NULL,
    name    VARCHAR(20) NOT NULL,

    PRIMARY KEY concept_definition_type_pk (dbid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE concept_definition_concept (
    concept         INT NOT NULL            COMMENT 'Concept DBID',
    type            INT NOT NULL            COMMENT 'Concept definition type',
    name            VARCHAR(20),
    operator        TINYINT,
    concept_value   INT NOT NULL,

    INDEX concept_definition_idx (concept, type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE concept_definition_role_group (
    concept         INT NOT NULL            COMMENT 'Concept DBID',
    type            INT NOT NULL            COMMENT 'Concept definition type',
    role_group      INT NOT NULL,
    operator        TINYINT,
    property        INT NOT NULL,
    value           VARCHAR(250),
    value_concept   INT,

    INDEX concept_definition_role_group_concept_type_idx (concept, type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE constraint_type (
                                 dbid        TINYINT AUTO_INCREMENT,
                                 name        VARCHAR(20) NOT NULL,

                                 PRIMARY KEY constraint_type_pk (dbid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

CREATE TABLE property_domain                                                                -- As fields (rather than JSON) due to many:many of property:concept
(
    class               INT NOT NULL            COMMENT 'Concept DBID',
    property            INT NOT NULL            COMMENT 'Property DBID',
    status              INT NOT NULL            COMMENT 'Status DBID',
    minimum             INT NOT NULL DEFAULT 0  COMMENT 'Minimum occurrence count',
    maximum             INT NOT NULL DEFAULT 0  COMMENT 'Maximum occurrence count (0=unlimited)',
    max_in_group        INT                     COMMENT 'Maximum cardinality within a role group',

    UNIQUE INDEX property_domain_idx (property, class),
    INDEX property_domain_concept_idx (class)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE property_range
(
    class       INT NOT NULL    COMMENT 'Concept DBID',
    property    INT NOT NULL    COMMENT 'Property DBID',
    status      INT NOT NULL    COMMENT 'Status DBID',
    name        VARCHAR(20),
    operator    TINYINT,
    type        TINYINT,
    concept     INT,

    PRIMARY KEY property_range_pk (property)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE data_type
(
    concept     INT NOT NULL    COMMENT 'Concept DBID',
    data        JSON NOT NULL   COMMENT 'Data type definition JSON',

    PRIMARY KEY data_type_pk (concept)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

-- ---------------------------------------------------------------------------------------------------------

CREATE TABLE concept_term_map
(
    term      VARCHAR(250) NOT NULL,
    type      INT          NOT NULL,
    target    INT          NOT NULL,
    PRIMARY KEY concept_term_map_pk (term, type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE concept_archive (
    dbid INT NOT NULL       COMMENT 'Concept DBID',
    revision INT NOT NULL   COMMENT 'Revision',
    data JSON NOT NULL,

    PRIMARY KEY concept_archive_pk (dbid, revision),
    INDEX concept_archive_dbid (dbid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

CREATE TABLE workflow_task_category
(
    dbid TINYINT     NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY workflow_task_category_pk (dbid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE concept_tct (
    source INT NOT NULL,
    property INT NOT NULL,
    target INT NOT NULL,
    level INT NOT NULL,

    PRIMARY KEY concept_pk (source, property, target),
    KEY concept_tct_source_property_level_idx (source, property, level),
    KEY concept_tct_source_property_idx (source, property),
    KEY concept_tct_property_target_idx (property, target)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP PROCEDURE IF EXISTS proc_build_tct;

/* TODO: Rewrite for new db structure
DELIMITER $$
CREATE PROCEDURE `proc_build_tct`(property_id VARCHAR(150))
BEGIN
    SELECT @lvl := 0;

    SELECT @property_dbid := dbid
    FROM concept
    WHERE id = property_id;

    DELETE FROM concept_tct
    WHERE property = @property_dbid;

    -- Insert LOWEST level (i.e. concepts without children)
    INSERT INTO concept_tct
    (source, property, target, level)
    SELECT o.dbid, @property_dbid, o.value, 0
    FROM concept_property_object o
    LEFT JOIN concept_property_object p ON p.value = o.dbid AND p.property = @property_dbid
    WHERE o.property = @property_dbid
    AND p.dbid IS NULL;

    SELECT @inserted := ROW_COUNT();

    WHILE @inserted > 0 DO
        SELECT @lvl := @lvl + 1;

        -- Insert parents of last tct entries
        REPLACE INTO concept_tct
        (source, property, target, level)
        SELECT DISTINCT h.dbid, @property_dbid, h.value, @lvl
        FROM concept_property_object h
        JOIN concept_tct t ON h.dbid = t.target AND t.level = @lvl - 1 AND t.property = @property_dbid
        WHERE h.property = @property_dbid;

        -- Inherit relationships
        REPLACE INTO concept_tct
        (source, property, target, level)
        SELECT DISTINCT t.source, @property_dbid, p.target, @lvl
        FROM concept_tct t
        JOIN concept_tct p ON p.source = t.target
        WHERE t.level = @lvl-1;

        SELECT @inserted := ROW_COUNT();
    END WHILE;
END$$
DELIMITER ;
*/
