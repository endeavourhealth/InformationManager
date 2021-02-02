use im3;
DROP TABLE IF EXISTS im_schema;
CREATE TABLE im_schema
   (
   dbid int,
   version int,
    PRIMARY KEY (`dbid`)
   );
DELETE FROM im_schema;
INSERT INTO im_schema
(dbid, version)
VALUES
(1,5);

INSERT INTO axiom_type (dbid, iri) VALUES (19, 'Member') ON DUPLICATE KEY UPDATE iri='Member';     -- ValueSet type
INSERT INTO axiom_type (dbid, iri) VALUES (20,'MemberExpansion') ON DUPLICATE KEY UPDATE iri='MemberExpansion'; -- Value set type
INSERT INTO axiom_type (dbid, iri) VALUES (21,'Property') ON DUPLICATE KEY UPDATE iri='Propery'; -- Recod type
INSERT INTO axiom_type (dbid, iri) VALUES (22,'MappedFrom') ON DUPLICATE KEY UPDATE iri='MappedFrom'; -- Legacy mapping

INSERT INTO expression_type (dbid, iri) VALUES  (11, 'PropertyConstraint') ON DUPLICATE KEY UPDATE iri='PropertyConstraint'; -- Record type properties

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