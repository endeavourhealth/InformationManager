USE im_next;

-- "Is a" concept
SELECT @is_a := id, iri, name FROM concept c WHERE iri = ':SN_116680003';

-- 1.Legacy --> Core maps
DROP TABLE IF EXISTS encounter_maps;
CREATE TABLE encounter_maps
SELECT REPLACE(c.iri, ':', '') AS coreIri, c.name AS coreTerm, REPLACE(l.iri, ':', '') AS legacyIri, l.name AS legacyTerm, lc.dbid AS legacyDbid 
FROM concept l
JOIN im_live_july2020.concept lc ON lc.id = REPLACE(l.iri, ':', '')
JOIN concept_property_object o ON o.concept = l.id AND o.property = @is_a
JOIN concept c ON c.id = o.object
WHERE l.iri LIKE ':LE\_%'
ORDER BY c.name;

SELECT * FROM encounter_maps;

-- 2.Encounter TCT
DROP TABLE IF EXISTS encounter_tct;
CREATE TABLE encounter_tct
SELECT DISTINCT REPLACE(g.iri, ':', '') AS parentIri, g.name AS parentTerm, REPLACE(REPLACE(s.iri, ':CTV3_', ':R3_'), ':', '') AS childIri, s.name AS childTerm, t2.level
FROM concept r
JOIN concept_tct t ON t.target = r.id AND t.property = @is_a
JOIN concept_tct t2 ON t2.source = t.source AND t2.property = @is_a AND t2.level <= t.level
JOIN concept s ON s.id = t2.source
JOIN concept g ON g.id = t2.target
WHERE r.iri = ':DM_EncounterEntry'
ORDER BY g.iri, t2.level, s.iri;

SELECT * FROM encounter_tct;

-- 2.Core encounter type hierarchy
DROP TABLE IF EXISTS encounter_cpo;
CREATE TABLE encounter_cpo
SELECT DISTINCT REPLACE(p.iri, ':', '') AS parentIri, p.name AS parentTerm, REPLACE(REPLACE(s.iri, ':CTV3_', ':R3_'), ':', '') AS childIri, s.name AS childTerm
FROM concept c
JOIN concept_tct t ON t.target = c.id AND t.property = @is_a
JOIN concept s ON s.id = t.source
JOIN concept_property_object cpo ON cpo.concept = s.id AND cpo.property = @is_a
JOIN concept p ON p.id = cpo.object
WHERE c.iri = ':DM_EncounterEntry'
ORDER BY p.iri, s.iri;

SELECT * FROM encounter_cpo;

-- All encounter concepts
DROP TABLE IF EXISTS encounter_concepts;
CREATE TABLE encounter_concepts
SELECT DISTINCT childIri AS iri
FROM encounter_cpo
WHERE SUBSTR(childIri, 1, 3) IN ('CM_', 'DM_', 'LE_')
ORDER BY childIri;

SELECT * FROM encounter_concepts;

-- vs:VSetHospitalEncounter1
SELECT DISTINCT REPLACE(c.iri, ':', '') AS coreIri, c.name AS coreTerm, REPLACE(REPLACE(l.iri, ':CTV3_', ':R3_'), ':', '') AS legacyIri, l.name AS legacyTerm, lc.dbid AS legacyDbid
FROM concept c
JOIN concept_tct t ON t.target = c.id AND t.property = @is_a
JOIN concept l ON l.id = t.source
JOIN im_live_july2020.concept lc ON lc.id = REPLACE(l.iri, ':', '')
WHERE c.iri IN (':DM_AandEEncounterEntry')
ORDER BY c.iri, l.iri
;

-- vs:VSetHospitalEncounter2
SELECT DISTINCT REPLACE(c.iri, ':', '') AS coreIri, c.name AS coreTerm, REPLACE(REPLACE(l.iri, ':CTV3_', ':R3_'), ':', '') AS legacyIri, l.name AS legacyTerm, lc.dbid AS legacyDbid
FROM concept c
JOIN concept_tct t ON t.target = c.id AND t.property = @is_a
JOIN concept l ON l.id = t.source
JOIN im_live_july2020.concept lc ON lc.id = REPLACE(l.iri, ':', '')
WHERE c.iri = ':CM_HospitalInpAdmitEncounter'
ORDER BY c.iri, l.iri
;

-- vs:VSetHospitalEncounter3
SELECT DISTINCT REPLACE(c.iri, ':', '') AS coreIri, c.name AS coreTerm, REPLACE(REPLACE(l.iri, ':CTV3_', ':R3_'), ':', '') AS legacyIri, l.name AS legacyTerm, lc.dbid AS legacyDbid
FROM concept c
JOIN concept_tct t ON t.target = c.id AND t.property = @is_a
JOIN concept l ON l.id = t.source
JOIN im_live_july2020.concept lc ON lc.id = REPLACE(l.iri, ':', '')
WHERE c.iri = ':CM_HospitalInpDischEncounter'
ORDER BY c.iri, l.iri
;

-- vs:VSetHospitalEncounter4
SELECT DISTINCT REPLACE(c.iri, ':', '') AS coreIri, c.name AS coreTerm, REPLACE(REPLACE(l.iri, ':CTV3_', ':R3_'), ':', '') AS legacyIri, l.name AS legacyTerm, lc.dbid AS legacyDbid
FROM concept c
JOIN concept_tct t ON t.target = c.id AND t.property = @is_a
JOIN concept l ON l.id = t.source
JOIN im_live_july2020.concept lc ON lc.id = REPLACE(l.iri, ':', '')
WHERE c.iri = ':DM_HospitalOpdEntry'
ORDER BY c.iri, l.iri
;

-- vs:VSetHospitalEncounter5
SELECT DISTINCT REPLACE(c.iri, ':', '') AS coreIri, c.name AS coreTerm, REPLACE(REPLACE(l.iri, ':CTV3_', ':R3_'), ':', '') AS legacyIri, l.name AS legacyTerm, lc.dbid AS legacyDbid
FROM concept c
JOIN concept_tct t ON t.target = c.id AND t.property = @is_a
JOIN concept l ON l.id = t.source
JOIN im_live_july2020.concept lc ON lc.id = REPLACE(l.iri, ':', '')
WHERE c.iri IN (':CM_HospitalDayCase')
ORDER BY c.iri, l.iri
;
