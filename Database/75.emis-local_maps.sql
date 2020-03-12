REPLACE INTO legacy_concept_id_map
(legacyId, coreId)
SELECT read_code,snomed_concept_id
FROM emis_read_snomed;
