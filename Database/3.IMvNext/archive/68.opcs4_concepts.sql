-- Common/useful ids
SELECT @ns := id FROM namespace WHERE prefix = ':';
SELECT @scheme := id FROM concept WHERE iri = ':OPCS4';

-- CONCEPTS
INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition, status, origin)
SELECT @ns, CONCAT(':O4_', code), IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description), description, @scheme, code, '{}', 1, 1
FROM opcs4;
