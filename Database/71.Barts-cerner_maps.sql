-- ADD DIRECT (1:1) MAPS
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN barts_cerner bc ON  c.id = CONCAT('BC_', bc.code)
    JOIN concept v ON v.id = CONCAT('SN_', bc.snomed_expression)
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'attribute',
                                                      JSON_OBJECT('property', 'mappedTo', 'valueConcept', JSON_OBJECT('concept', v.id))
                                              )
                                      )
                                      )
    );

-- Map to expression proxies
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN barts_cerner bc ON  c.id = CONCAT('BC_', bc.code)
    JOIN barts_cerner_snomed_expressions e ON e.expression = bc.snomed_expression
    JOIN concept v ON v.id = CONCAT('DS_BC_', e.dbid)
SET cd.data = JSON_MERGE_PRESERVE(cd.data,
                                  JSON_OBJECT('subtypeOf', JSON_ARRAY(
                                          JSON_OBJECT('operator', 'AND',
                                                      'attribute',
                                                      JSON_OBJECT('property', 'mappedTo', 'valueConcept', JSON_OBJECT('concept', v.id))
                                              )
                                      )
                                      )
    );
