USE im_next3;

INSERT IGNORE INTO namespace
(iri, prefix)
VALUES
('http://www.w3.org/2002/07/owl#', 'owl:'),
('http://www.DiscoveryDataService.org/InformationModel/HealthCare#', ':'),
('http://snomed.info/sct#', 'sn:'),
('http://www.w3.org/2001/XMLSchema#', 'xsd:');

SELECT @ns_dds := dbid FROM namespace WHERE prefix = ':';
SELECT @ns_owl := dbid FROM namespace WHERE prefix = 'owl:';
SELECT @ns_sno := dbid FROM namespace WHERE prefix = 'sn:';
SELECT @ns_xsd := dbid FROM namespace WHERE prefix = 'xsd:';

INSERT IGNORE INTO concept
(namespace, id, iri, name, status)
VALUES
(@ns_dds, '7aabea4d-ce89-4abe-9412-a7f649d18fc7', ':891071000252105', 'DiscoveryCode', 1);

SELECT @scm_dc := dbid FROM concept WHERE iri = ':891071000252105';

INSERT IGNORE INTO concept
(namespace, id, iri, name, description, status)
VALUES
(@ns_owl, 'da81aa80-4ae7-4dc6-8bae-b39032884c85', 'owl:Thing', 'Owl thing', 'Top level owl concept', 1);

INSERT IGNORE INTO concept
(namespace, id, iri, name, description, status)
VALUES
(@ns_sno, 'f20fe46c-ce80-4266-85c5-dc70d91ee908', 'sn:116680003', 'Is a', 'Is a', 1);

INSERT IGNORE INTO concept
(namespace, id, iri, name, description, status)
VALUES
(@ns_xsd, '0e07f13b-4dd9-42cf-aafa-b1958f4f0986', 'xsd:int', 'Integer data type', 'Integer data type', 1),
(@ns_xsd, 'a9b61843-57b0-445c-b327-288ae958a474', 'xsd:string', 'String data type', 'String data type', 1);
