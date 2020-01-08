-- Common/usefule ids
SELECT @coreActive := dbid FROM concept WHERE id = 'CoreActive';
SELECT @coreInactive := dbid FROM concept WHERE id = 'CoreInactive';
SELECT @codeScheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @subtypeOf := dbid FROM concept WHERE id = 'subtypeOf';
SELECT @replacedBy := dbid FROM concept WHERE id = 'replacedBy';

-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/Snomed', '1.0.0');

SET @model = LAST_INSERT_ID();

-- Add code scheme
INSERT INTO concept (model, status, id, name, description)
VALUES (@model, @coreActive, 'SNOMED', 'SNOMED','The SNOMED code scheme');
SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_relation (subject, relation, object)
VALUES (@scheme, @subtypeOf, @codeScheme);

-- INSERT CORE CONCEPTS
INSERT INTO concept (model, status, id, name, description, codeScheme, code)
SELECT @model,
       IF(active = 1, @coreActive, @coreInactive),
       concat('SN_', id),
       IF(LENGTH(term) > 255, CONCAT(LEFT(term, 252), '...'), term),
        term,
        @scheme,
        id
FROM snomed_description_filtered;

-- ROOT code is subtype of codeable - SN_138875005/SNOMED CT Concept (SNOMED RT+CTV3)
INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @subtypeOf, @codeable
FROM concept c
WHERE c.id = 'SN_138875005';




INSERT INTO concept_relation (subject, relation, object, `group`)
SELECT c.dbid, @subtypeOf, t.dbid, r.relationshipGroup
FROM snomed_relationship_active r
         JOIN concept c ON c.id = CONCAT('SN_', r.sourceId)
         JOIN concept t ON t.id = CONCAT('SN_', r.destinationId)
WHERE r.typeId = 116680003;

INSERT INTO concept_relation (subject, relation, object, `group`)
SELECT c.dbid, t.dbid, v.dbid, r.relationshipGroup
FROM snomed_relationship_active r
JOIN concept c ON c.id = CONCAT('SN_', r.sourceId)
JOIN concept v ON v.id = CONCAT('SN_', r.destinationId)
JOIN concept t ON t.id = CONCAT('SN_', r.typeId)
WHERE r.typeId <> 116680003;

-- Replacements
INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @replacedBy, r.dbid
FROM snomed_history h
JOIN concept c ON c.id = concat('SN_', h.oldConceptId)
JOIN concept r ON r.id = concat('SN_', h.newConceptId)
GROUP BY h.oldConceptId;
