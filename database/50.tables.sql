DROP TABLE IF EXISTS model;
CREATE TABLE model (
    dbid    INT AUTO_INCREMENT      COMMENT 'Unique model DBID',
    iri     VARCHAR(255) NOT NULL   COMMENT 'Model iri',
    version VARCHAR(10) NOT NULL    COMMENT 'Version (major.minor.build)',

    PRIMARY KEY model_pk(dbid),
    INDEX model_iri_idx (iri)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS document;
CREATE TABLE document
(
    dbid    INT AUTO_INCREMENT              COMMENT 'Unique document DBID',
    data    JSON NOT NULL                   COMMENT 'Document (header) JSON',

    -- Exposed (known) JSON properties
    id      CHAR(36)                        GENERATED ALWAYS AS (`data` ->> '$.documentId') STORED NOT NULL,

    PRIMARY KEY document_pk (dbid),
    UNIQUE INDEX document_id_uq (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS concept;
CREATE TABLE concept
(
    dbid        INT AUTO_INCREMENT,
    model       INT NOT NULL                COMMENT 'The model the concept belongs to',
    data        JSON NOT NULL               COMMENT 'Concept JSON blob',
    updated     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,                    -- Used for MRU

    -- Exposed (known) JSON properties
    id VARCHAR(140) COLLATE utf8_bin        GENERATED ALWAYS AS (`data` ->> '$.id') STORED NOT NULL,
    name VARCHAR(255)                       GENERATED ALWAYS AS (`data` ->> '$.name') STORED,
    scheme VARCHAR(140)                     GENERATED ALWAYS AS (`data` ->> '$.codeScheme') STORED,
    code VARCHAR(20) COLLATE utf8_bin       GENERATED ALWAYS AS (`data` ->> '$.code') STORED,
    status VARCHAR(140) COLLATE utf8_bin    GENERATED ALWAYS AS (`data` ->> '$.status') VIRTUAL,

    PRIMARY KEY concept_dbid_pk (dbid),
    UNIQUE KEY concept_id_uq (id),
    UNIQUE KEY concept_code_scheme (code, scheme),
    FULLTEXT concept_name_ftx (name)    -- TODO: Include description?
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS concept_synonym;
CREATE TABLE concept_synonym
(
    dbid    INT NOT NULL            COMMENT 'Concept DBID',
    synonym VARCHAR(255) NOT NULL   COMMENT 'Synonym text',

    INDEX concept_synonym_idx (dbid),
    FULLTEXT concept_synonym_ftx (synonym)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS concept_definition;
CREATE TABLE concept_definition (
    concept     INT NOT NULL            COMMENT 'Concept DBID',
    type        TINYINT NOT NULL        COMMENT 'Definition type concept DBID (0=subtypeOf, 1=equivalentTo, 2=mappedTo, 3=replacedBy)',
    data        JSON NOT NULL           COMMENT 'Definition JSON blob',

    INDEX concept_definition_idx (concept),
    INDEX concept_definition_concept_type_idx (concept, type)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS property_domain;
CREATE TABLE property_domain                                                                -- As fields (rather than JSON) due to many:many of property:concept
(
    property    INT NOT NULL            COMMENT 'Property DBID',
    concept     INT NOT NULL            COMMENT 'Concept DBID',
    status      INT NOT NULL            COMMENT 'Status DBID',
    minimum     INT NOT NULL DEFAULT 0  COMMENT 'Minimum occurrence count',
    maximum     INT NOT NULL DEFAULT 0  COMMENT 'Maximum occurrence count (0=unlimited)',

    INDEX property_domain_idx (property),
    INDEX property_domain_concept (concept)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS property_range;
CREATE TABLE property_range
(
    property    INT NOT NULL    COMMENT 'Property DBID',
    data        JSON NOT NULL   COMMENT 'Range expression JSON',

    INDEX property_range_idx (property)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS data_type;
CREATE TABLE data_type
(
    concept     INT NOT NULL    COMMENT 'Concept DBID',
    data        JSON NOT NULL   COMMENT 'Data type definition JSON',

    PRIMARY KEY data_type_pk (concept)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

DROP TABLE IF EXISTS concept_archive;
CREATE TABLE concept_archive (
    dbid INT NOT NULL       COMMENT 'Concept DBID',
    revision INT NOT NULL   COMMENT 'Revision',
    data JSON NOT NULL,

    PRIMARY KEY concept_archive_pk (dbid, revision),
    INDEX concept_archive_dbid (dbid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS im_instance;
CREATE TABLE im_instance
(
    dbid INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    url  VARCHAR(400) NOT NULL,

    PRIMARY KEY im_instance_pk (dbid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- RESERVED "term maps" document (dbid 0)
SET SESSION sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

INSERT INTO document (dbid, data)
VALUES (0, '{
  "documentId": "3413f20b-5a92-48f4-85bc-796d158cf3d9",
  "documentIri": "InformationModel/dm/TermMaps/Initialization",
  "modelIri": "InformationModel/dm/TermMaps",
  "baseModelVersion": "0.0.0",
  "effectiveDate": "2019-09-23 13:07:32",
  "documentStatus": "FinalRelease",
  "documentPurpose": "Authoring",
  "publisher": "OR_ENDEAVOUR",
  "targetModelVersion": "1.0.0"
}');

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

DROP TABLE IF EXISTS concept_tct;
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
