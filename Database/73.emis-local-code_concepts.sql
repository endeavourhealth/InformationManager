INSERT INTO document
(path, version, draft)
VALUES
('InformationModel/dm/EmisLocal', '1.0.0', 1);

SET @doc:=LAST_INSERT_ID();

INSERT INTO concept
(document, data)
VALUES
(@doc, JSON_OBJECT(
        'id', 'EMIS_LOCAL',
        'name', 'EMIS Local',
        'description', 'EMIS local codes scheme',
        'is_subtype_of', JSON_OBJECT('id', 'CodeScheme'),
        'code_prefix', 'EMLOC_'
    ));

INSERT INTO concept
(document, data)
SELECT @doc, JSON_OBJECT(
        'id', CONCAT('EMLOC_', local_code),
        'name', local_term,
        'code', local_code,
        'code_scheme', JSON_OBJECT( 'id', 'EMIS_LOCAL'),
        'has_synonym', JSON_ARRAYAGG(local_term),
        'is_subtype_of', JSON_OBJECT( 'id', 'CodeableConcept')
    ) AS data
FROM emis_local_codes
GROUP BY local_code
;
