SELECT @ns := id FROM namespace WHERE prefix = ':';
SELECT @scheme := id FROM concept WHERE iri = ':Snomed-CT';

-- Concepts
-- ********** NOTE: Stated not yet available, definition == {}
-- ********** NOTE: USE REPLACE AS SOME SNOMED CODES REFERENCED IN CORE
REPLACE INTO concept
(namespace, iri, name, description, scheme, code, definition, status, origin)
SELECT @ns, CONCAT(':SN_', id), IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term), term, @scheme, id, '{}', IF(active = 1, 1, 2), 0
FROM snomed_description_filtered;

-- ********** NOTE: Stated not yet available, insert inferred definitions

-- Common/useful ids
SELECT @subClassOf := id FROM concept WHERE iri = ':SN_116680003'; -- (Is a)
SELECT @replacedBy := id FROM concept WHERE iri = ':SN_370124000'; -- (replaced by)

/*

-- ROOT code is subtype of Class/Type - SN_138875005/SNOMED CT Concept (SNOMED RT+CTV3)
INSERT INTO concept_property_object (concept, property, object)
SELECT c.id, @subClassOf, s.id
FROM concept c
JOIN concept s ON s.iri = 'Snomed-CT#TypeClass'
WHERE c.iri = 'Snomed-CT#138875005';

*/


-- Subtypes
INSERT INTO concept_property_object (concept, property, object, `group`)
SELECT c.id, p.id, o.id, r.relationshipGroup
FROM snomed_relationship_active r
JOIN concept c ON c.iri = CONCAT(':SN_', r.sourceId)
JOIN concept p ON p.iri = CONCAT(':SN_', r.typeId)
JOIN concept o ON o.iri = CONCAT(':SN_', r.destinationId);

-- Replacements
INSERT INTO concept_property_object (concept, property, object)
SELECT c.id, @replacedBy, r.id
FROM snomed_history h
JOIN concept c ON c.iri = concat(':SN_', h.oldConceptId)
JOIN concept r ON r.iri = concat(':SN_', h.newConceptId)
GROUP BY h.oldConceptId;

-- !!HORRIBLE SNOMED ATTRIBUTE HACK!!
/*
UPDATE subtype s
JOIN concept c ON c.id = s.concept
SET axiom = @subPropertyOf, supertype = @property
WHERE c.iri = 'sn:106237007';  -- Linkage concepts

UPDATE subtype s
JOIN concept c ON c.id = s.concept
SET axiom = @subPropertyOf
WHERE c.iri LIKE 'sn:%'
AND c.name LIKE '%(attribute)'
AND s.axiom = @subClassOf;  -- Linkage concepts
*/
