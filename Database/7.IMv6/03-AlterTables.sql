ALTER TABLE tpl
DROP FOREIGN KEY tpl_pred_fk ;
ALTER TABLE tpl
DROP FOREIGN KEY tpl_sub_fk;
DROP INDEX tpl_pred_sub_idx on tpl;
DROP INDEX tpl_pred_oc_idx on tpl;
DROP INDEX tpl_sub_pred_obj on tpl;
DROP INDEX tpl_ob_pred_sub on tpl;
ALTER TABLE tpl ADD blank_node BIGINT NULL DEFAULT NULL after subject;
ALTER TABLE tpl
ADD CONSTRAINT tpl_blank_fk 
FOREIGN KEY (blank_node)
REFERENCES tpl (dbid),
ADD CONSTRAINT tpl_sub_fk
FOREIGN KEY (subject)
REFERENCES concept(dbid),
ADD CONSTRAINT tpl_pred_fk
FOREIGN KEY (predicate)
REFERENCES concept (dbid);
CREATE INDEX tpl_pred_sub_idx on tpl (predicate ASC,subject ASC,blank_node ASC);
CREATE INDEX tpl_pred_oc_idx ON tpl (predicate ASC,object ASC);
CREATE INDEX tpl_sub_pred_obj on tpl (subject ASC, predicate, object,group_number,blank_node);
CREATE INDEX tpl_ob_pred_sub on tpl (object ASC, predicate,subject,group_number,blank_node);

ALTER TABLE concept
DROP COLUMN definition;
--  -------

ALTER TABLE tpl_data
DROP FOREIGN KEY tpld_pred_fk;
ALTER TABLE tpl_data
DROP FOREIGN KEY tpld_sub_fk;
DROP INDEX tpld_pred_sub_idx on tpl_data;
DROP INDEX tpld_l_pred_sub on tpl_data;
ALTER TABLE tpl_data ADD blank_node BIGINT NULL DEFAULT NULL after subject;
ALTER TABLE tpl_data 
ADD column json JSON  NULL after data_type,
MODIFY COLUMN literal VARCHAR(1024) NULL,
ADD CONSTRAINT tpld_blank_fk 
FOREIGN KEY (blank_node)
REFERENCES tpl(dbid),
ADD CONSTRAINT tpld_sub_fk
FOREIGN KEY (subject)
REFERENCES concept(dbid),
ADD CONSTRAINT tpld_pred_fk
FOREIGN KEY (predicate)
REFERENCES concept(dbid);
CREATE INDEX tpld_pred_sub_idx on tpl_data (predicate ASC,subject ASC,blank_node ASC) ;
 CREATE INDEX tpld_l_pred_sub on tpl_data (literal(20) ASC, predicate,subject,group_number,blank_node);
   
   