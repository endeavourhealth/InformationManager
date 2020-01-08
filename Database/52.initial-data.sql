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

INSERT INTO workflow_task_category
    (dbid, name)
VALUES (0, 'Concept mapping'),
       (1, 'Term mapping');

-- RESERVED (SEED) CONCEPTS --
