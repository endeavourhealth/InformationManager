
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

USE im6 ;

-- -----------------------------------------------------
DROP TABLE IF EXISTS im_schema;
CREATE TABLE im_schema
   (
   dbid int,
   version int,
    PRIMARY KEY (dbid)
   );

INSERT INTO im_schema
(dbid, version)
VALUES
(1,1);


-- -----------------------------------------------------
DROP TABLE IF EXISTS namespace ;

CREATE TABLE IF NOT EXISTS namespace (
  dbid INT NOT NULL AUTO_INCREMENT COMMENT 'Unique prefix DBID',
  iri VARCHAR(255) NOT NULL COMMENT 'Namespace iri',
  prefix VARCHAR(50) NOT NULL COMMENT 'Namespace default prefix (alias)',
  name VARCHAR(255) NULL COMMENT 'name of namespace',
  updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (dbid),
  UNIQUE INDEX ns_iri_uq (iri ASC) ,
  UNIQUE INDEX ns_prefix_uq (prefix ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

-- ------------------------------------------------------
DROP TABLE IF EXISTS concept_type ;

CREATE TABLE IF NOT EXISTS concept_type (
  dbid BIGINT NOT NULL AUTO_INCREMENT,
  concept INT NOT NULL,
  type VARCHAR(140) NOT NULL,
  updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (dbid),
  INDEX ct_c_t (concept ASC, type ASC),
  INDEX ct_t_c (type ASC, concept ASC)
  )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
DROP TABLE IF EXISTS concept ;

CREATE TABLE IF NOT EXISTS concept (
  dbid INT NOT NULL AUTO_INCREMENT,
  iri VARCHAR(140) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NOT NULL,
  name VARCHAR(256) NULL DEFAULT NULL,
  description TEXT NULL DEFAULT NULL,
  code VARCHAR(50) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin' NULL DEFAULT NULL,
  scheme VARCHAR(140) NULL DEFAULT NULL,
  status VARCHAR(140) NOT NULL DEFAULT '0',
  definition json NULL,
  updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (dbid),
  UNIQUE INDEX concept_iri_uq (iri ASC) ,
  UNIQUE INDEX concept_scheme_code_uq (scheme ASC, code ASC) ,
  INDEX concept_updated_idx (updated ASC) ,
  INDEX concept_code_idx (code ASC) ,
  INDEX concept_scheme_idx (scheme ASC),
  FULLTEXT INDEX concept_name_ftx (name) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;



-- -----------------------------------------------------
DROP TABLE IF EXISTS tct ;

CREATE TABLE IF NOT EXISTS tct (
  dbid INT NOT NULL AUTO_INCREMENT,
  ancestor INT NOT NULL,
  descendant INT NOT NULL,
  type INT NOT NULL,
  level INT NOT NULL,
  PRIMARY KEY (dbid),
  INDEX tct_anc_dec_idx (ancestor ASC,descendant ASC,type ASC) ,
  INDEX tct_descendent_idx (descendant ASC, ancestor,type ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
-- ------------------------------

DROP TABLE IF EXISTS tpl;

CREATE TABLE IF NOT EXISTS tpl (
  dbid BIGINT NOT NULL AUTO_INCREMENT,
  subject INT NOT NULL,
  object INT NOT NULL,
  graph INT NOT NULL COMMENT 'OWNER OF THIS ISA RELATIONSHIP',
  predicate INT NOT NULL,
  PRIMARY KEY (dbid),
  INDEX isa_pct_idx (object ,subject,predicate) ,
  INDEX isa_cpt_idx (subject,object,predicate) ,
  INDEX isa_graph_idx (graph ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
DROP TABLE IF EXISTS concept_term ;

CREATE TABLE IF NOT EXISTS concept_term (
  dbid INT NOT NULL AUTO_INCREMENT,
  concept INT NOT NULL,
  term VARCHAR(250) NULL DEFAULT NULL,
  code VARCHAR (250) NULL,
  updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (dbid),
  INDEX concept_term_concept_idx (term,concept ASC) ,
  CONSTRAINT ct_concept_fk
  FOREIGN KEY(concept)
  REFERENCES concept (dbid)
   ON DELETE CASCADE
   ON UPDATE NO ACTION,
  FULLTEXT (term)
  )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;




-- -----------------------------------------------------
DROP TABLE IF EXISTS tpl_group ;

CREATE TABLE IF NOT EXISTS tpl_group (
  dbid bigint  NOT NULL auto_increment,
  subject INT  NOT NULL,
  graph INT NULL DEFAULT NULL,
  group_number INT NOT NULL DEFAULT 0,
  predicate INT NOT NULL,
  object INT NULL,
  PRIMARY KEY (dbid),
   INDEX cpo_pred_sub_idx (predicate ASC,subject ASC) ,
   INDEX cpo_pred_oc_idx (predicate ASC,object ASC) ,
   INDEX cpo_sub_pred_obj (subject ASC, predicate, object,group_number),
   INDEX cpo_ob_pred_sub (object ASC, predicate,subject,group_number),
   CONSTRAINT cpo_sub_fk 
   FOREIGN KEY (subject)
   REFERENCES concept (dbid),
   CONSTRAINT cpo_pred_fk 
   FOREIGN KEY (predicate)
   REFERENCES concept (dbid)
   ON DELETE CASCADE
   ON UPDATE NO ACTION,
    CONSTRAINT cpo_graph_fk
    FOREIGN KEY (graph)
    REFERENCES concept (dbid)
    )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
DROP TABLE IF EXISTS tpl_group_data ;

CREATE TABLE IF NOT EXISTS tpl_group_data (
  dbid bigint  NOT NULL auto_increment,
  subject INT  NOT NULL,
  graph INT NULL DEFAULT NULL,
  group_number int NOT NULL DEFAULT 0,
  predicate INT NOT NULL,
  literal VARCHAR(1024) NULL,
  data_type INT NULL,
  PRIMARY KEY (dbid),
   INDEX cpd_pred_sub_idx (predicate ASC,subject ASC) ,
   INDEX cpd_l_pred_sub (literal(20) ASC, predicate,subject,group_number),
   CONSTRAINT cpd_sub_fk 
   FOREIGN KEY (subject)
   REFERENCES concept (dbid),
   CONSTRAINT cpd_pred_fk 
   FOREIGN KEY (predicate)
   REFERENCES concept (dbid)
   ON DELETE CASCADE
   ON UPDATE NO ACTION
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

-- ------------------------------------------------------


-- -----------------------------------------------------
-- SET SQL_MODE=@OLD_SQL_MODE;
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;