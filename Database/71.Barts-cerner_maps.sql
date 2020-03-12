INSERT INTO legacy_concept_id_map
(legacyId, coreId)
SELECT CONCAT('BC_', code), IF(e.dbid IS NULL, CONCAT('SN_', snomed_expression), CONCAT('DS_BC_', e.dbid))
FROM barts_cerner b
LEFT JOIN barts_cerner_snomed_expressions e ON e.expression = b.snomed_expression
WHERE snomed_expression IS NOT NULL;
