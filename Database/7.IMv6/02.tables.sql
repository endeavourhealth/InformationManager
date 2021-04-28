
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
  status VARCHAR(140) NOT NULL DEFAULT 'http://endhealth.info/im#Draft',
  json JSON NULL,
  updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (dbid),
  UNIQUE INDEX concept_iri_uq (iri ASC) ,
  UNIQUE INDEX concept_scheme_code_uq (scheme ASC, code ASC) ,
  INDEX concept_updated_idx (updated ASC) ,
  INDEX concept_code_idx (code ASC) ,
  INDEX concept_scheme_idx (scheme ASC),
  index concept_name_idx (name ASC),
  FULLTEXT INDEX concept_name_ftx (name) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------
DROP TABLE IF EXISTS `instance`;
--   -------------------------
CREATE TABLE `instance`(
dbid BIGINT NOT NULL AUTO_INCREMENT,
iri VARCHAR(256) NOT NULL,
type INT NULL,
name VARCHAR(256) NULL,
primary key (dbid),
unique index ins_iri_idx (iri),
index ins_tiri_idx (type,iri),
index ins_n_idx (name)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

-- -------------------------------
DROP TABLE IF EXISTS `tpl_ins_object`;
--   -------------------------
CREATE TABLE `tpl_ins_object`(
dbid BIGINT NOT NULL AUTO_INCREMENT,
subject BIGINT NOT NULL,
blank_node BIGINT NULL,
predicate INT NOT NULL,
object BIGINT NULL,
primary key (dbid),
index insi_ops_idx (object,predicate,subject),
index insi_spo_idx (subject,predicate,object),
index insi_ps_idx (predicate,subject),
index insi_po_idx (predicate,object),
 CONSTRAINT insi_s_fk 
   FOREIGN KEY (subject)
   REFERENCES instance (dbid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
-- -----------------------------------
DROP TABLE IF EXISTS `tpl_ins_data`;
--   -------------------------
CREATE TABLE `tpl_ins_data`(
dbid BIGINT NOT NULL AUTO_INCREMENT,
subject BIGINT NOT NULL,
blank_node BIGINT NULL,
predicate INT NOT NULL,
literal VARCHAR(1600) NOT NULL,
data_type INT NOT NULL,
primary key (dbid),
index insd_l_p_idx (literal(256),predicate,subject),
index insd_spl_idx (subject,predicate,literal(256)),
index insd_pd_idx (predicate,subject),
 CONSTRAINT ins_s_fk 
   FOREIGN KEY (subject)
   REFERENCES instance (dbid)
)
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

DROP TABLE IF EXISTS concept_term ;

CREATE TABLE IF NOT EXISTS concept_term (
  dbid INT NOT NULL AUTO_INCREMENT,
  concept INT NOT NULL,
  term VARCHAR(250) NULL DEFAULT NULL,
  code VARCHAR (250) NOT NULL COMMENT 'code or termid or hash of term',
  scheme INT NOT NULL,
  concept_term_code VARCHAR(250) NULL COMMENT 'might be a termid of the concept, which may be the same code as the code',
  updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (dbid),
  INDEX ct_tcs_idx (term,concept ASC) ,
  INDEX ct_cs_idx(code,scheme,concept),
  INDEX ct_sc_idx(scheme,code,concept),
  CONSTRAINT ct_concept_fk
  FOREIGN KEY(concept)
  REFERENCES concept (dbid)
   ON DELETE CASCADE
   ON UPDATE NO ACTION,
  FULLTEXT ct_term_ftx (term)
  )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;




-- -----------------------------------------------------
DROP TABLE IF EXISTS tpl ;

CREATE TABLE IF NOT EXISTS tpl (
  dbid bigint  NOT NULL auto_increment,
  subject INT  NOT NULL,
  blank_node BIGINT NULL DEFAULT NULL,
  graph INT NULL DEFAULT NULL,
  group_number INT NOT NULL DEFAULT 0,
  predicate INT NOT NULL,
  object INT NULL,
  PRIMARY KEY (dbid),
   INDEX tpl_pred_sub_idx (predicate ASC,subject ASC,blank_node) ,
   INDEX tpl_pred_oc_idx (predicate ASC,object ASC) ,
   INDEX tpl_sub_pred_obj (subject ASC, predicate, object,group_number,blank_node),
   INDEX tpl_ob_pred_sub (object ASC, predicate,subject,group_number,blank_node),
   CONSTRAINT tpl_blank_fk
   FOREIGN KEY (blank_node)
   REFERENCES tpl (dbid)
   ON DELETE CASCADE
   ON UPDATE NO ACTION,
   CONSTRAINT tpl_sub_fk 
   FOREIGN KEY (subject)
   REFERENCES concept (dbid),
   CONSTRAINT tpl_pred_fk 
   FOREIGN KEY (predicate)
   REFERENCES concept (dbid)
   ON DELETE CASCADE
   ON UPDATE NO ACTION,
    CONSTRAINT tpl_graph_fk
    FOREIGN KEY (graph)
    REFERENCES concept (dbid)
    )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
DROP TABLE IF EXISTS tpl_data ;

CREATE TABLE IF NOT EXISTS tpl_data (
  dbid bigint  NOT NULL auto_increment,
  subject INT  NOT NULL,
  blank_node BIGINT NULL DEFAULT NULL,
  graph INT NULL DEFAULT NULL,
  group_number int NOT NULL DEFAULT 0,
  predicate INT NOT NULL,
  literal VARCHAR(16000) NULL,
  data_type INT NULL,
  PRIMARY KEY (dbid),
   INDEX tpld_pred_sub_idx (predicate ASC,subject ASC,group_number,blank_node) ,
   INDEX tpld_l_pred_sub (literal(50) ASC, predicate,subject,group_number,blank_node),
   CONSTRAINT tpld_blank_fk
   FOREIGN KEY (blank_node)
   REFERENCES tpl  (dbid)
   ON DELETE CASCADE
   ON UPDATE NO ACTION,
   CONSTRAINT tpld_sub_fk 
   FOREIGN KEY (subject)
   REFERENCES concept (dbid),
   CONSTRAINT tpld_pred_fk 
   FOREIGN KEY (predicate)
   REFERENCES concept (dbid)
   ON DELETE CASCADE
   ON UPDATE NO ACTION
    )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------

DROP TABLE IF EXISTS concept_search ;

CREATE TABLE IF NOT EXISTS concept_search(
    dbid INT NOT NULL AUTO_INCREMENT,
    term VARCHAR(256) NULL DEFAULT NULL,
    concept_dbid INT NOT NULL,
    weighting INT NOT NULL DEFAULT 0,
    PRIMARY KEY(dbid),
    CONSTRAINT concept_dbid_fk
        FOREIGN KEY (concept_dbid)
            REFERENCES concept (dbid),
    FULLTEXT INDEX concept_search_term_ftx (term)
    )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
-- ---------------------------------
DROP TABLE IF EXISTS config ;

CREATE TABLE IF NOT EXISTS config
(
    dbid   INT         NOT NULL AUTO_INCREMENT,
    name   VARCHAR(50) NOT NULL,
    config JSON        NULL,
    PRIMARY KEY (dbid),
    UNIQUE INDEX cf_name_uq (name ASC)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4;



-- -----------------------------------------------------


-- SET SQL_MODE=@OLD_SQL_MODE;
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
