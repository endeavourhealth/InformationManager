INSERT INTO legacy_concept_id_map
(legacyId, coreId)
SELECT CONCAT('O4_', code), target
FROM opcs4_map
WHERE target IS NOT NULL;
