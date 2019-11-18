SET SESSION sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

/*-- RESERVED "term maps" document (dbid 0)
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
}');*/

INSERT INTO operator
    (dbid, name)
VALUES (0, 'AND'),
       (1, 'OR'),
       (2, 'NOT'),
       (3, 'ANDNOT'),
       (4, 'ORNOT'),
       (5, 'XOR');

INSERT INTO concept_definition_type
    (dbid, name)
VALUES (0, 'subtype of'),
       (1, 'equivalent to'),
       (2, 'mapped to'),
       (3, 'term code of'),
       (4, 'replaced by'),
       (5, 'child of'),
       (6, 'inverse property of'),
       (7, 'disjoint with');

INSERT INTO constraint_type
    (dbid, name)
VALUES (0, 'class'),
       (1, 'class or subtypes'),
       (2, 'subtypes'),
       (3, 'value set'),
       (4, 'data type');

INSERT INTO workflow_task_category
    (dbid, name)
VALUES (0, 'Concept mapping'),
       (1, 'Term mapping');
