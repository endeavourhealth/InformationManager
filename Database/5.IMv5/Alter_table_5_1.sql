use im3;
DROP TABLE IF EXISTS im_schema;
CREATE TABLE im_schema
   (
   dbid int,
   version int,
    PRIMARY KEY (`dbid`)
   );

INSERT INTO im_schema (dbid, version) VALUES (1,5) ON DUPLICATE KEY UPDATE dbid=1;


INSERT INTO axiom_type (dbid, iri) VALUES (19, 'Member') ON DUPLICATE KEY UPDATE iri='Member';     -- ValueSet type
INSERT INTO axiom_type (dbid, iri) VALUES (20,'MemberExpansion') ON DUPLICATE KEY UPDATE iri='MemberExpansion'; -- Value set type
INSERT INTO axiom_type (dbid, iri) VALUES (21,'Property') ON DUPLICATE KEY UPDATE iri='Propery'; -- Recod type
INSERT INTO axiom_type (dbid, iri) VALUES (22,'MappedFrom') ON DUPLICATE KEY UPDATE iri='MappedFrom'; -- Legacy mapping
INSERT INTO axiom_type (dbid, iri) VALUES (23,'Role') ON DUPLICATE KEY UPDATE iri='Role'; -- inferred Role
INSERT INTO axiom_type (dbid, iri) VALUES (24,'MemberExc') ON DUPLICATE KEY UPDATE iri='MemberExc'; -- exclusion of classes
INSERT INTO axiom_type (dbid, iri) VALUES (25,'MemberInstance') ON DUPLICATE KEY UPDATE iri='MemberInstance'; -- instance concepts
INSERT INTO axiom_type (dbid, iri) VALUES (26,'MemberExcInstance') ON DUPLICATE KEY UPDATE iri='MemberExcInstance'; -- instance exclusion

INSERT INTO expression_type (dbid, iri) VALUES  (11, 'PropertyConstraint') ON DUPLICATE KEY UPDATE iri='PropertyConstraint'; -- Record type properties
INSERT INTO expression_type (dbid, iri) VALUES  (12, 'PropertyValue') ON DUPLICATE KEY UPDATE iri='PropertyValue'; -- new property value type
INSERT INTO expression_type (dbid, iri) VALUES  (13, 'Role') ON DUPLICATE KEY UPDATE iri='Role'; -- role and role group

DROP PROCEDURE IF EXISTS add_exclude;

DELIMITER $$

CREATE DEFINER=CURRENT_USER PROCEDURE add_exclude ( ) 
BEGIN
DECLARE colName TEXT;
SELECT column_name INTO colName
FROM information_schema.columns 
WHERE table_schema = 'im3'
    AND table_name = 'expression'
AND column_name = 'exclude';

IF colName is null THEN 
    ALTER TABLE  expression ADD  exclude TINYINT NOT NULL DEFAULT  0  COMMENT  'excludes the class defined by expression';
END IF; 
END$$

DELIMITER ;

CALL add_exclude;

DROP PROCEDURE add_exclude;