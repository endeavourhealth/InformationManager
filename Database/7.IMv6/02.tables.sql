
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

USE `im6` ;

-- -----------------------------------------------------
DROP TABLE IF EXISTS im_schema;
CREATE TABLE im_schema
   (
   dbid int,
   version int,
    PRIMARY KEY (`dbid`)
   );

INSERT INTO im_schema
(dbid, version)
VALUES
(1,1);


-- -----------------------------------------------------
DROP TABLE IF EXISTS `namespace` ;

CREATE TABLE IF NOT EXISTS `namespace` (
  `dbid` INT NOT NULL AUTO_INCREMENT COMMENT 'Unique prefix DBID',
  `iri` VARCHAR(255) NOT NULL COMMENT 'Namespace iri',
  `prefix` VARCHAR(50) NOT NULL COMMENT 'Namespace default prefix (alias)',
  `name` VARCHAR(255) NULL COMMENT 'name of namespace',
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  UNIQUE INDEX `ns_iri_uq` (`iri` ASC) ,
  UNIQUE INDEX `ns_prefix_uq` (`prefix` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
DROP TABLE IF EXISTS concept ;

CREATE TABLE IF NOT EXISTS concept (
  `id` INT NOT NULL AUTO_INCREMENT,
   `model_type` VARCHAR(140) NOT NULL,
  `iri` VARCHAR(140) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NOT NULL,
  `name` VARCHAR(256) NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `code` VARCHAR(50) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NULL DEFAULT NULL,
  `scheme` VARCHAR(140) NULL DEFAULT NULL,
  `status` VARCHAR(140) NOT NULL DEFAULT '0',
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `concept_iri_uq` (`iri` ASC) ,
  UNIQUE INDEX `concept_scheme_code_uq` (`scheme` ASC, `code` ASC) ,
  INDEX `concept_updated_idx` (`updated` ASC) ,
  INDEX `concept_code_idx` (`code` ASC) ,
  INDEX `concept_scheme_idx` (`scheme` ASC),
  INDEX `concept_type_idx` (`model_type` ASC),
  FULLTEXT INDEX `concept_name_ftx` (`name`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;



-- -----------------------------------------------------
DROP TABLE IF EXISTS `tct` ;

CREATE TABLE IF NOT EXISTS `tct` (
  `dbid` INT NOT NULL AUTO_INCREMENT,
  `ancestor` INT NOT NULL,
  `descendant` INT NOT NULL,
  `type` INT NOT NULL,
  `level` INT NOT NULL,
  PRIMARY KEY (`dbid`),
  INDEX `tct_ancestor_idx` (`ancestor` ASC) ,
  INDEX `tct_descendent_idx` (`descendant` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
-- ------------------------------

DROP TABLE IF EXISTS `classification`;

CREATE TABLE IF NOT EXISTS `classification` (
  `dbid` BIGINT NOT NULL AUTO_INCREMENT,
  `parent` INT NOT NULL,
  `child` INT NOT NULL,
  `graph` INT NOT NULL COMMENT 'OWNER OF THIS ISA RELATIONSHIP',
  `type` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`dbid`),
  INDEX `isa_parent_idx` (`parent` ASC) ,
  INDEX `isa_child_idx` (`child` ASC) ,
  INDEX `isa_graph_idx` (`graph` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
-- -----------------------------------------------------
DROP TABLE IF EXISTS `concept_term` ;

CREATE TABLE IF NOT EXISTS `concept_term` (
  `dbid` INT NOT NULL AUTO_INCREMENT,
  `concept` INT NOT NULL,
  `term` VARCHAR(250) NULL DEFAULT NULL,
  `code` VARCHAR (250) NULL,
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  INDEX `concept_term_code_idx` (`code` ASC) ,
  INDEX `concept_term_term_idx`(`term` ASC) ,
  FULLTEXT (`term`),
  INDEX `concept_term_concept_idx` (`concept` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
DROP TABLE IF EXISTS `concept_term_map` ;

CREATE TABLE IF NOT EXISTS `concept_term_map` (
  `dbid` INT NOT NULL AUTO_INCREMENT,
  `term` VARCHAR(250) NOT NULL,
  `type` INT NOT NULL,
  `target` INT NOT NULL,
  `updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dbid`),
  UNIQUE INDEX `concept_term_map_uq` (`term` ASC, `type` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
DROP TABLE IF EXISTS `statement` ;

CREATE TABLE IF NOT EXISTS `statement` (
  `dbid` bigint  NOT NULL auto_increment,
  `subject` INT  NOT NULL,
  `graph` INT NULL DEFAULT NULL,
  `subject_blank` bigint NULL DEFAULT NULL,
  `predicate` VARCHAR(140) NULL,
  `node_type` tinyint NOT NULL,
  `object` INT NULL,
  `data_type` INT NULL,
  `literal` VARCHAR(512),
  `info` TEXT NULL,
  PRIMARY KEY (`dbid`),
	INDEX `st_subject_idx` (`subject` ASC) ,
  INDEX `st_predicate_idx` (`predicate` ASC) ,
   INDEX `st_oc_idx` (`object` ASC) ,
   INDEX `st_l_idx` (`literal` ASC) ,
   INDEX `st_odt_idx` (`data_type` ASC) ,
   INDEX `st_graph_idx`(`graph` ASC),
   INDEX `st_pred_sub_idx` (`predicate` ASC,`subject` ASC) ,
   INDEX `st_pred_oc_idx` (`predicate` ASC,`object` ASC) ,
    INDEX `st_pred_odt_idx` (`predicate` ASC,`data_type` ASC) ,
   INDEX `st_pred_l_idx` (`predicate` ASC,`literal` ASC),
   CONSTRAINT `st_sub_fk` 
   FOREIGN KEY (`subject`)
   REFERENCES `concept` (`id`)
   ON DELETE CASCADE
   ON UPDATE NO ACTION,
    CONSTRAINT `st_graph_fk`
    FOREIGN KEY (`graph`)
    REFERENCES `concept` (`id`)
    )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
DROP TABLE IF EXISTS feed_version;

CREATE TABLE feed_version (
    dbid INT NOT NULL AUTO_INCREMENT,
    feed VARCHAR(40) NOT NULL,
    version VARCHAR(10) NOT NULL,

    PRIMARY KEY feed_version_pk (dbid),
    UNIQUE INDEX feed_version_feed_uq (feed)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
