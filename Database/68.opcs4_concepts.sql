-- Create document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/OPCS4', '1.0.0');

SET @doc = LAST_INSERT_ID();

INSERT INTO concept(document, data)
VALUES (@doc, JSON_OBJECT(
                    'id', 'OPCS4',
                    'name', 'OPCS4',
                    'description', 'The OPCS4 code scheme',
                    'is_subtype_of', JSON_OBJECT('id', 'CodeScheme'),
                    'code_prefix', 'O4_'
    ));

-- CONCEPTS
INSERT INTO concept (document, data)
SELECT @doc, JSON_OBJECT(
           'id', concat('O4_',code),
           'name', if(length(description) > 255, concat(left(description, 252), '...'), description),
           'description', description,
           'code_scheme', JSON_OBJECT('id', 'OPCS4'),
           'code', code,
           'is_subtype_of', JSON_OBJECT(
               'id','CodeableConcept'
               )
           )
FROM opcs4;
