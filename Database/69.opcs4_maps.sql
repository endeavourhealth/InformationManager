-- Create PROXY MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/O4-proxy', '1.0.0');

SET @model = LAST_INSERT_ID();

-- Add missing proxy concepts
INSERT INTO concept (model, data)
VALUES (@model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', 'DC_OPCS_2',
        'name', 'delivery of a fraction of external beam radiotherapy nec',
        'description', 'delivery of a fraction of external beam radiotherapy nec'
    ));

SET @dcopcs2 = LAST_INSERT_ID();

INSERT INTO concept_definition (concept, data)
VALUES (@dcopcs2, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept'),
                JSON_OBJECT('operator', 'AND', 'concept', 'SN_33195004'),
                JSON_OBJECT('operator', 'AND', 'attribute', JSON_OBJECT(
                        'property', 'SN_424244007',
                        'valueConcept', JSON_OBJECT('concept', 'SN_115468007')
                    ))
            )));

-- Map all
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN opcs4_map m ON  c.id = CONCAT('O4_', m.code)
    JOIN concept v ON v.id = m.target
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'attribute',
                                                      JSON_OBJECT('property', 'mappedTo', 'valueConcept', JSON_OBJECT('concept', m.target))
                                              )
                                      )
                                      )
    )
WHERE m.target IS NOT NULL;

