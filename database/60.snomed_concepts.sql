-- Common/useful ids
SELECT @coreActive := id FROM concept WHERE iri = 'cm:CoreActive';
SELECT @coreInactive := id FROM concept WHERE iri = 'cm:CoreInactive';
SELECT @property := id FROM concept WHERE iri = 'cm:Property';

SELECT @subClassOf := id FROM axiom WHERE token = 'SubClassOf';
SELECT @subPropertyOf := id FROM axiom WHERE token = 'SubPropertyOf';
SELECT @replacedBy := id FROM axiom WHERE token = 'ReplacedBy';

/*
SELECT @codeScheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @replacedBy := dbid FROM concept WHERE id = 'replacedBy';
*/
-- Create MODEL
INSERT INTO namespace (iri, name, prefix)
VALUES ('InformationModel/dm/Snomed', 'SNOMED', 'sn');

SELECT @ns := id FROM namespace WHERE prefix = 'sn';


-- INSERT CORE CONCEPTS
INSERT INTO concept (namespace, status, iri, name, description, code)
SELECT @ns,
       IF(active = 1, @coreActive, @coreInactive),
       concat('sn:', id),
       IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term),
        term,
        id
FROM snomed_description_filtered;

-- ROOT code is subtype of Class/Type - SN_138875005/SNOMED CT Concept (SNOMED RT+CTV3)
INSERT INTO subtype (concept, axiom, supertype)
SELECT c.id, @subClassOf, s.id
FROM concept c
JOIN concept s ON s.iri = 'cm:TypeClass'
WHERE c.iri = 'sn:138875005';


-- Definitions
-- Subtypes
INSERT INTO subtype (concept, axiom, supertype)
SELECT c.id, @subClassOf, t.id
FROM snomed_relationship_active r
         JOIN concept c ON c.iri = CONCAT('sn:', r.sourceId)
         JOIN concept t ON t.iri = CONCAT('sn:', r.destinationId)
WHERE r.typeId = 116680003;

INSERT INTO property_class (concept, axiom, `group`, property, object)
SELECT c.id, @subClassOf, r.relationshipGroup, p.id, o.id
FROM snomed_relationship_active r
         JOIN concept c ON c.iri = CONCAT('sn:', r.sourceId)
         JOIN concept p ON p.iri = CONCAT('sn:', r.typeId)
         JOIN concept o ON o.iri = CONCAT('sn:', r.destinationId)
WHERE r.typeId <> 116680003;


-- Replacements
INSERT INTO subtype (concept, axiom, supertype)
SELECT c.id, @replacedBy, r.id
FROM snomed_history h
JOIN concept c ON c.iri = concat('sn:', h.oldConceptId)
JOIN concept r ON r.iri = concat('sn:', h.newConceptId)
GROUP BY h.oldConceptId;


-- !!HORRIBLE SNOMED ATTRIBUTE HACK!!
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
