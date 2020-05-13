-- Common/useful ids
SELECT @childOf := id FROM concept WHERE iri = ':CM_isChildCodeOf';

SELECT @ns := id FROM namespace WHERE prefix = ':';
SELECT @scheme := id FROM concept WHERE iri = ':Read2';

-- CORE CONCEPTS (TermCode = 00)
INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition, status, origin)
SELECT @ns, CONCAT(':R2_', REPLACE(code, '.', '')), IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term), term, @scheme, REPLACE(code, '.', ''), '{}', 1, 1
FROM read_v2;

-- CHILD HIERARCHY
INSERT INTO concept_property_object (concept, property, object)
SELECT p.id, @childOf, c.id
FROM read_v2 r
JOIN concept p ON p.iri = concat(':R2_', REPLACE(r.code, '.', ''))
JOIN concept c ON c.iri = concat(':R2_', LEFT(REPLACE(r.code, '.', ''), LENGTH(REPLACE(r.code, '.', ''))-1))
WHERE r.code <> '.....';

/*
-- TERM VARIANTS (TermCode != 00)
INSERT INTO concept (model, data)
SELECT DISTINCT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('R2_', readCode, termCode),
        'description', @desc:=IFNULL(term198, IFNULL(term60, term30)),
        'name', if(length(@desc) > 255, concat(left(@desc, 252), '...'), @desc),
        'codeScheme', 'READ2',
        'code', concat(readCode, '-', termCode)
    )
FROM read_v2_key
WHERE termCode <> '00';
*/
