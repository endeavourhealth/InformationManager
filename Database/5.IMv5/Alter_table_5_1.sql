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

INSERT INTO axiom_type
(dbid, iri)
VALUES
(19, 'Member'),     -- ValueSet type
(20,'MemberExpansion'), -- Value set type
(21,'Property'), -- Recod type
(22,'MappedFrom')
;
INSERT INTO expression_type
(dbid, iri)
VALUES
(11, 'PropertyConstraint')
;

    