-- Common/useful ids
SELECT @ns := id FROM namespace WHERE prefix = ':';
SELECT @scheme := id FROM concept WHERE iri = ':EMISLocal';


-- EMIS local codes
SELECT DISTINCT e.readTermId, e.codeTerm
FROM emis_read_snomed e
         LEFT JOIN read_v2 r ON REPLACE(r.code, '.', '') = e.readTermId
WHERE r.code IS NULL;


-- EMIS local code maps
WITH RECURSIVE emis_cte AS (
    SELECT e.readTermId, e.snomedCTConceptId, e.parentCodeId
    FROM emis_read_snomed e
             LEFT JOIN read_v2 r ON REPLACE(r.code, '.', '') = e.readTermId
    WHERE r.code IS NULL
    UNION ALL
    SELECT t.readTermId, e.snomedCTConceptId, e.parentCodeId
    FROM emis_cte t
             INNER JOIN emis_read_snomed e ON e.codeId = t.parentCodeId
    WHERE t.snomedCTConceptId LIKE '%1000006___'
)
SELECT readTermId, snomedCTConceptId
FROM emis_cte
WHERE snomedCTConceptId NOT LIKE '%1000006___'
ORDER BY readTermId;



-- Concepts
INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition, status, origin)
SELECT @ns, CONCAT(':EMLOC_', local_code), IF(LENGTH(local_term) > 250, CONCAT(LEFT(local_term, 247), '...'), local_term), local_term, @scheme, local_code, '{}', 1, 1
FROM emis_local_codes
GROUP BY local_code;

INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', CONCAT('EMLOC_', local_code),
        'name', local_term,
        'codeScheme', 'EMIS_LOCAL',
        'code', local_code)
FROM emis_local_codes
GROUP BY local_code;

/*
INSERT INTO concept_definition (concept, data)
SELECT c.dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('concept', 'CodeableConcept')
            )
    )
FROM emis_local_codes e
JOIN concept c ON c.id = CONCAT('EMLOC_', local_code)
GROUP BY local_code;

INSERT INTO concept_synonym (dbid, synonym)
SELECT c.dbid, e.local_term
FROM emis_local_codes e
JOIN concept c ON c.id = CONCAT('EMLOC_', local_code) AND c.name <> e.local_term;
*/
