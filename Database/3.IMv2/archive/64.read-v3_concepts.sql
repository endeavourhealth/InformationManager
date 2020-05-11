-- Common/useful ids
SELECT @childOf := id FROM concept WHERE iri = ':CM_isChildCodeOf';

SELECT @ns := id FROM namespace WHERE prefix = ':';
SELECT @scheme := id FROM concept WHERE iri = ':CTV3';

-- CORE CONCEPTS (TermCode = 00)
INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition, status, origin)
SELECT @ns, CONCAT(':CTV3_', code), IF(LENGTH(name) > 250, CONCAT(LEFT(name, 247), '...'), name), ifnull(description, name), @scheme, code, '{}', 1, 1
FROM read_v3_current;

-- Relationships
INSERT INTO concept_property_object (concept, property, object)
SELECT c.id, @childOf, v.id
FROM read_v3_hier rel
JOIN read_v3_concept p ON p.code = rel.parent
JOIN concept c ON c.iri = concat(':CTV3_', rel.code)
JOIN concept v ON v.iri = concat(':CTV3_', rel.parent)
GROUP BY rel.code;

