
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

USE `im_next3` ;

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
(5, 'Individual')
;

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
(0, ':SubClassOf'),
(1, ':EquivalentTo'),
(2, ':SubObjectPropertyOf'),
(3, ':SubDataPropertyOf'),
(4, ':ObjectPropertyRange'),
(5, ':PropertyDomain'),
(6, ''),    -- Annotation property
(7, ':DisjointWith'),    -- Disjoint with
(8, ''),     -- Annotation
(9, ':SubPropertyChain'),     -- Sub property chain
(10, ':InverseOf'),     -- Inverse property
(11, ':IsFunctional'),     -- Is functional
(12, ':IsTransitive'),     -- Is transitive
(13, ':IsSymmetrical'),    -- Is symmetric
(14, ':value'),
(15,':DataPropertyRange')
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
(4, 'PropertyObject'),
(5, 'PropertyData'),
(6,'ComplementOf'),
(7,'ObjectOneOf'),
(8,'DataType'),
(9,'DataExactValue'),
(10,'DataOneOf'),
(11,'DataTypeRestriction'),
(12,'ObjectExactValue')
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
DROP TABLE IF EXISTS `concept` ;

CREATE TABLE IF NOT EXISTS `concept` (
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
  `expression` INT NULL DEFAULT NULL,
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
  `dbid` INT  NOT NULL AUTO_INCREMENT,
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
  `dbid` INT  NOT NULL AUTO_INCREMENT,
  `type` INT NOT NULL COMMENT 'the type of expression including simple or group expressions such as intersections',
  `axiom` INT NULL DEFAULT NULL COMMENT 'the axiom that this expression is included in unless the exprssion is a stand alone expression',
  `parent` INT NULL DEFAULT NULL COMMENT 'the parent expression this expression is nested within',
  `related_concept` INT NULL DEFAULT NULL COMMENT 'Denormalised field. If the expression is a simple type then the class or property concept which is the value',
  PRIMARY KEY (`dbid`),
  INDEX `expression_axiom_fk` (`axiom` ASC) VISIBLE,
  INDEX `expression_parent_fk` (`parent` ASC) VISIBLE,
  INDEX `expression_related_concept_fk` (`related_concept`) VISIBLE,
  CONSTRAINT `expression_axiom`
    FOREIGN KEY (`axiom`)
    REFERENCES `axiom` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `expression_parent`
    FOREIGN KEY (`parent`)
    REFERENCES `expression` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
     CONSTRAINT `expression_related_concept`
    FOREIGN KEY (`related_concept`)
    REFERENCES `concept` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
DROP TABLE IF EXISTS `restriction` ;

CREATE TABLE IF NOT EXISTS `restriction` (
  `dbid` INT  NOT NULL AUTO_INCREMENT ,
  `expression` INT  NOT NULL,
  `property` INT NOT NULL,
  `range_concept` INT NOT NULL,
  `inverse` TINYINT,
  `min_cardinality` INT,
  `max_cardinality` INT,
  `range_expression` INT,
  PRIMARY KEY (`dbid`),
  INDEX `restriction_expression_idx` (`expression` ASC) VISIBLE,
  INDEX `restriction_property_ids` (`property` ASC) VISIBLE,
  INDEX `restriction_range_expression` (`range_expression` ASC) VISIBLE,
  CONSTRAINT `data_value_expression`
    FOREIGN KEY (`expression`)
    REFERENCES `expression` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `restriction_property`
    FOREIGN KEY (`property`)
    REFERENCES `concept` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
     CONSTRAINT `restriction_range_concept`
    FOREIGN KEY (`range_concept`)
    REFERENCES `concept` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `restriction_range_expression`
    FOREIGN KEY (`range_expression`)
    REFERENCES `expression` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
DROP TABLE IF EXISTS `data_range` ;

CREATE TABLE IF NOT EXISTS `data_range` (
  `dbid` INT  NOT NULL AUTO_INCREMENT ,
  `restriction` INT NULL,
  `expression` INT  NULL,
  `exact_value` VARCHAR(255) NULL,
  `from_operator` CHAR(1) NULL,
  `from` VARCHAR(255) NULL,
  `to_operator` CHAR(1) NULL,
  `to` VARCHAR(255) NULL,
  PRIMARY KEY (`dbid`),
  INDEX `dr_expression_idx` (`expression` ASC) VISIBLE,
  INDEX `dr_restriction_idx` (`restriction` ASC) VISIBLE,
  INDEX `dr_exact_value` (`exact_value` ASC) VISIBLE,
  CONSTRAINT `dr_expression`
    FOREIGN KEY (`expression`)
    REFERENCES `expression` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `dr_restriction`
    FOREIGN KEY (`restriction`)
    REFERENCES `restriction` (`dbid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
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
