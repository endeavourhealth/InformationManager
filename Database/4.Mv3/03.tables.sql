
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

USE `im3` ;

DROP TABLE IF EXISTS concept_status;
CREATE TABLE concept_status (
    dbid TINYINT NOT NULL,
    name VARCHAR(20) NOT NULL,

    PRIMARY KEY concept_status_pk (dbid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
;

INSERT INTO concept_status
(dbid, name)
VALUES
(0, 'Draft'),
(1, 'Active'),
(2, 'Inactive');

DROP TABLE IF EXISTS concept_type;
CREATE TABLE concept_type (
    dbid    TINYINT NOT NULL,
    iri     VARCHAR(140),

    PRIMARY KEY concept_type_pk (dbid)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
;

INSERT INTO concept_type
(dbid, iri)
VALUES
(0, 'Class'),
(1, 'ObjectProperty'),
(2, 'DataProperty'),
(3, 'DataType'),
(4, 'Annotation'),
(5,'Individual');

-- -----------------------------------------------------
DROP TABLE IF EXISTS axiom_type ;

CREATE TABLE IF NOT EXISTS axiom_type (
  `dbid` TINYINT NOT NULL,
  `iri` VARCHAR(140),
  PRIMARY KEY (`dbid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

INSERT INTO axiom_type
(dbid, iri)
VALUES
(0, 'SubClassOf'),
(1, 'EquivalentTo'),
(2, 'SubObjectPropertyOf'),
(3, 'SubDataPropertyOf'),
(4, 'SubAnnotationPropertyOf'),    -- Annotation sub property
(5, 'ObjectPropertyRange'),
(6,'DataPropertyRange'),
(7, 'PropertyDomain'),
(8, 'DisjointWith'),    -- Disjoint with
(9, 'SubPropertyChain'),     -- Sub property chain
(10, 'InversePropertyOf'),     -- Inverse property
(11, 'IsFunctional'),     -- Is functional
(12, 'IsTransitive'),     -- Is transitive
(13, 'IsSymmetrical'),    -- Is symmetric
(14,'IsReflexive'),
(15,'ObjectPropertyAssertion'),
(16,'DataPropertyAssertion'),
(17, 'IsType'),     -- Individual
(18,'DataTypeDefinition'),
(19,'AnnotationAssertion')
;


-- -----------------------------------------------------
DROP TABLE IF EXISTS expression_type ;

CREATE TABLE IF NOT EXISTS expression_type (
  `dbid` TINYINT NOT NULL,
  `iri` VARCHAR(140) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NOT NULL,
  PRIMARY KEY (`dbid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO expression_type
(dbid, iri)
VALUES
(0, 'Class'),
(1, 'Property'),
(2, 'Intersection'),
(3, 'Union'),
(4, 'ObjectOneOf'),
(5, 'DataOneOf'),
(6,'ObjectPropertyValue'),
(7,'DataPropertyValue'),
(8,'ComplementOf'),
(9,'DataType'),
(10,'ObjectValue')
;

-- -----------------------------------------------------
DROP TABLE IF EXISTS ontology ;

CREATE TABLE IF NOT EXISTS ontology (
  `dbid` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique ontology DBID',
  `iri` VARCHAR(255) NOT NULL COMMENT 'Ontology iri',
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  UNIQUE INDEX `ontology_iri_uq` (`iri` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS document ;

CREATE TABLE IF NOT EXISTS document (
  `dbid` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique document DBID',
  `uuid` CHAR(36) NOT NULL,
  `module` INT NULL DEFAULT NULL,
  `ontology` INT NULL DEFAULT NULL,
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  UNIQUE INDEX `document_uuid_uq` (`uuid` ASC) VISIBLE,
  INDEX `document_module_idx` (`module` ASC) VISIBLE,
  INDEX `document_ontology_idx` (`ontology` ASC) VISIBLE,
  CONSTRAINT `document_module`
    FOREIGN KEY (`module`)
    REFERENCES `module` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `document_ontology`
    FOREIGN KEY (`ontology`)
    REFERENCES `ontology` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `module` ;

CREATE TABLE IF NOT EXISTS `module` (
  `dbid` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique module DBID',
  `iri` VARCHAR(255) NOT NULL COMMENT 'Module iri',
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  UNIQUE INDEX `module_iri_uq` (`iri` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `namespace` ;

CREATE TABLE IF NOT EXISTS `namespace` (
  `dbid` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique namespace DBID',
  `iri` VARCHAR(255) NOT NULL COMMENT 'Namespace iri',
  `prefix` VARCHAR(50) NOT NULL COMMENT 'Namespace default prefix (alias)',
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  UNIQUE INDEX `namespace_iri_uq` (`iri` ASC) VISIBLE,
  UNIQUE INDEX `namespace_prefix_uq` (`prefix` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
DROP TABLE IF EXISTS concept ;

CREATE TABLE IF NOT EXISTS concept (
  `dbid` INT NOT NULL AUTO_INCREMENT,
  `namespace` INT NOT NULL,
  `module` INT NOT NULL,
  `iri` VARCHAR(140) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NOT NULL,
  `name` VARCHAR(256) NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `type` TINYINT NULL DEFAULT NULL,
  `code` VARCHAR(50) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NULL DEFAULT NULL,
  `scheme` INT NULL DEFAULT NULL,
  `status` TINYINT NOT NULL DEFAULT '0',
  `expression` BIGINT NULL DEFAULT NULL,
  `weighting` INT NOT NULL DEFAULT '0',
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  UNIQUE INDEX `concept_iri_uq` (`iri` ASC) VISIBLE,
  UNIQUE INDEX `concept_scheme_code_uq` (`scheme` ASC, `code` ASC) VISIBLE,
  INDEX `concept_updated_idx` (`updated` ASC) VISIBLE,
  INDEX `concept_expression_expression_idx` (`expression` ASC) VISIBLE,
  CONSTRAINT `concept_expression_expression`
    FOREIGN KEY (`expression`)
    REFERENCES `expression` (`dbid`),
  FULLTEXT INDEX `concept_name_ftx` (`name`) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `axiom` ;

CREATE TABLE IF NOT EXISTS `axiom` (
  `dbid` BIGINT  NOT NULL AUTO_INCREMENT,
  `module` INT NOT NULL,
  `concept` INT NOT NULL COMMENT 'the concept which is the concept of this axiom i.e. the concept which is partly defined by this axiom',
  `type` TINYINT NULL DEFAULT NULL COMMENT 'the axiom type i.e. one of the OWL2 axiom types+ Discovery axiom like types',
  `version` INT NULL DEFAULT NULL,
  PRIMARY KEY (`dbid`),
  INDEX `axiom_concept_fk` (`concept` ASC) VISIBLE,
  INDEX `axiom_module_fk` (`module` ASC) VISIBLE,
  CONSTRAINT `axiom_concept`
    FOREIGN KEY (`concept`)
    REFERENCES `concept` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `axiom_module`
    FOREIGN KEY (`module`)
    REFERENCES `module` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `concept_tct` ;

CREATE TABLE IF NOT EXISTS `concept_tct` (
  `dbid` INT NOT NULL AUTO_INCREMENT,
  `source` INT NOT NULL,
  `property` INT NOT NULL,
  `level` INT NOT NULL,
  `target` INT NOT NULL,
  PRIMARY KEY (`dbid`),
  INDEX `concept_tct_source_property_idx` (`source` ASC, `property` ASC) VISIBLE,
  INDEX `concept_tct_property_level_idx` (`property` ASC, `level` ASC) VISIBLE,
  INDEX `concept_tct_property_target_idx` (`property` ASC, `target` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
--------------------------------

DROP TABLE IF EXISTS `concept_tree`;

CREATE TABLE IF NOT EXISTS `concept_tree` (
  `dbid` BIGINT NOT NULL AUTO_INCREMENT,
  `parent` INT NOT NULL,
  `child` INT NOT NULL,
  `module` INT NOT NULL COMMENT 'OWNER OF THIS ISA RELATIONSHIP',
  PRIMARY KEY (`dbid`),
  INDEX `concept_tree_parent_idx` (`parent` ASC) VISIBLE,
  INDEX `concept_tree_child_idx` (`child` ASC) VISIBLE,
  INDEX `concept_tree_module_idx` (`module` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
-- -----------------------------------------------------
DROP TABLE IF EXISTS `concept_term` ;

CREATE TABLE IF NOT EXISTS `concept_term` (
  `dbid` INT NOT NULL AUTO_INCREMENT,
  `concept` INT NOT NULL,
  `term` VARCHAR(250) NULL DEFAULT NULL,
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  INDEX `concept_term_concept_idx` (`concept` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
DROP TABLE IF EXISTS `concept_term_map` ;

CREATE TABLE IF NOT EXISTS `concept_term_map` (
  `dbid` INT NOT NULL AUTO_INCREMENT,
  `term` VARCHAR(250) NOT NULL,
  `type` INT NOT NULL,
  `target` INT NOT NULL,
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  UNIQUE INDEX `concept_term_map_uq` (`term` ASC, `type` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
DROP TABLE IF EXISTS `expression` ;

CREATE TABLE IF NOT EXISTS `expression` (
  `dbid` BIGINT  NOT NULL AUTO_INCREMENT,
  `type` INT NOT NULL COMMENT 'the typdata_type_definitione of expression including simple or group expressions such as intersections',
  `axiom` BIGINT NULL DEFAULT NULL COMMENT 'the axiom that this expression is included in unless the exprssion is a stand alone expression',
  `parent` BIGINT NULL DEFAULT NULL COMMENT 'the parent expression this expression is nested within',
  `target_concept` INT NULL DEFAULT NULL COMMENT 'Denormalised field. If the expression is a simple type then the class or property concept which is the value',
  PRIMARY KEY (`dbid`),
  INDEX `expression_axiom_fk` (`axiom` ASC) VISIBLE,
  INDEX `expression_parent_fk` (`parent` ASC) VISIBLE,
  INDEX `expression_target_concept_fk` (`target_concept`) VISIBLE,
  CONSTRAINT `expression_axiom`
    FOREIGN KEY (`axiom`)
    REFERENCES `axiom` (`dbid`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `expression_parent`
    FOREIGN KEY (`parent`)
    REFERENCES `expression` (`dbid`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
     CONSTRAINT `expression_target_concept`
    FOREIGN KEY (`target_concept`)
    REFERENCES `concept` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
DROP TABLE IF EXISTS `property_value` ;

CREATE TABLE IF NOT EXISTS `property_value` (
  `dbid` BIGINT  NOT NULL AUTO_INCREMENT ,
  `expression` BIGINT  NOT NULL,
  `property` INT NOT NULL,
  `value_type` INT NULL,
  `inverse` TINYINT,
  `min_cardinality` INT,
  `max_cardinality` INT,
  `value_expression` BIGINT,
  `value_data` VARCHAR(255) NULL,
  PRIMARY KEY (`dbid`),
  INDEX `property_value_expression_idx` (`expression` ASC) VISIBLE,
  INDEX `property_value_property_ids` (`property` ASC) VISIBLE,
  INDEX `property_value_value_expression` (`value_expression` ASC) VISIBLE,
  CONSTRAINT `property_value_expression`
    FOREIGN KEY (`expression`)
    REFERENCES `expression` (`dbid`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
    CONSTRAINT `property_value_property`
    FOREIGN KEY (`property`)
    REFERENCES `concept` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
     CONSTRAINT `property_value_value_type`
    FOREIGN KEY (`value_type`)
    REFERENCES `concept` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `property_value_value_expression`
    FOREIGN KEY (`value_expression`)
    REFERENCES `expression` (`dbid`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
DROP TABLE IF EXISTS `datatype_definition` ;

CREATE TABLE IF NOT EXISTS `datatype_definition` (
  `dbid` INT  NOT NULL AUTO_INCREMENT ,
  `concept` INT NOT NULL,
  `module` INT NOT NULL,
  `min_operator` CHAR(2) NULL,
  `min_value` VARCHAR(255) NULL,
  `max_operator` CHAR(2) NULL,
  `max_value` VARCHAR(255) NULL,
  `pattern` VARCHAR(255) NULL,
  PRIMARY KEY (`dbid`),
  INDEX `dt_concept_idx` (`concept` ASC) VISIBLE,
  CONSTRAINT `dt_concept`
    FOREIGN KEY (`concept`)
    REFERENCES `concept` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    INDEX `dt_module_idx` (`module` ASC) VISIBLE,
    CONSTRAINT `dt_module`
      FOREIGN KEY (`module`)
      REFERENCES `module` (`dbid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
---------------------------------------------------

---------------------------------------------------
DROP TABLE IF EXISTS `valueset_tct` ;

CREATE TABLE IF NOT EXISTS `valueset_tct` (
  `dbid` INT NOT NULL AUTO_INCREMENT,
  `source` INT NOT NULL,
  `property` INT NOT NULL,
  `level` INT NOT NULL,
  `target` INT NOT NULL,
  PRIMARY KEY (`dbid`),
  INDEX `concept_tct_source_property_idx` (`source` ASC, `property` ASC) VISIBLE,
  INDEX `concept_tct_property_level_idx` (`property` ASC, `level` ASC) VISIBLE,
  INDEX `concept_tct_property_target_idx` (`property` ASC, `target` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
