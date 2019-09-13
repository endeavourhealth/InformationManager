-- Create DOCUMENT
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/DMD', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- ********************* DM+D CONCEPTS *********************
INSERT INTO concept(document, id, name, description)
VALUES
(@doc, 'DMD_VTM', 'Virtual therapeutic moiety', null),
(@doc, 'DMD_VMP', 'Virtual medicinal product', null),
(@doc, 'DMD_has_moiety', 'Has moiety relationship', null),
(@doc, 'DMD_VMPP', 'Virtual medicinal product pack', null),
(@doc, 'DMD_is_pack_of', 'Is pack of relationship', null),
(@doc, 'DMD_AMP', 'Actual medicinal product', null),
(@doc, 'DMD_is_branded', 'An actual (branded) instance of a virtual (generic) product', null),
(@doc, 'DMD_AMPP', 'Actual medicinal product pack', null),
(@doc, 'DMD_has_ingredient', 'Has ingredient relationship', null),
(@doc, 'DMD_Ingredient', 'Ingredient', null),
(@doc, 'DM+D', 'DM+D code scheme', 'Dictionary of Medicines & Devices'),
(@doc, 'DMD_UOM', 'Units of measure', 'DM+D specified units of measure'),
(@doc, 'DMD_numerator_value', 'DM+D numerator value', 'Numerator value for an ingredient'),
(@doc, 'DMD_numerator_units', 'DM+D numerator units', 'Numerator unit of measure for an ingredient'),
(@doc, 'DMD_denominator_value', 'DM+D denominator value', 'Denominator value for an ingredient'),
(@doc, 'DMD_denominator_units', 'DM+D denominator units', 'Denominator unit of measure for an ingredient')
;

-- Common/useful IDs
SELECT @isA := dbid FROM concept WHERE id = 'isA';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @prefix := dbid FROM concept WHERE id = 'codePrefix';
SELECT @codescheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @dataprop := dbid FROM concept WHERE id = 'dataProperty';
SELECT @scheme := dbid FROM concept WHERE id = 'DM+D';
SELECT @moiety := dbid FROM concept WHERE id = 'DMD_has_moiety';
SELECT @uom := dbid FROM concept WHERE id = 'DMD_UOM';
SELECT @pack := dbid FROM concept WHERE id = 'DMD_is_pack_of';
SELECT @branded := dbid FROM concept WHERE id = 'DMD_is_branded';
SELECT @vtm := dbid FROM concept WHERE id = 'DMD_VTM';
SELECT @vmp := dbid FROM concept WHERE id = 'DMD_VMP';
SELECT @vmpp := dbid FROM concept WHERE id = 'DMD_VMPP';
SELECT @amp := dbid FROM concept WHERE id = 'DMD_AMP';
SELECT @ampp := dbid FROM concept WHERE id = 'DMD_AMPP';
SELECT @ingredient := dbid FROM concept WHERE id = 'DMD_Ingredient';
SELECT @numval := dbid FROM concept WHERE id = 'DMD_numerator_value';
SELECT @numuom := dbid FROM concept WHERE id = 'DMD_numerator_units';
SELECT @denval := dbid FROM concept WHERE id = 'DMD_denominator_value';
SELECT @denuom := dbid FROM concept WHERE id = 'DMD_denominator_units';

-- Properties
# INSERT INTO concept_property (dbid, property, concept)
# SELECT dbid, @isA, @codeable FROM concept WHERE id in ('DMD_VTM', 'DMD_VMP', 'DMD_VMPP', 'DMD_AMP', 'DMD_AMPP', 'DMD_Ingredient', 'DMD_UOM');

# INSERT INTO concept_property (dbid, property, concept)
# SELECT dbid, @subtype, @relationship FROM concept WHERE id in ('DMD_has_moiety', 'DMD_is_pack_of', 'DMD_is_branded', 'DMD_has_ingredient');

INSERT INTO concept_property (dbid, property, concept)
VALUES (@scheme, @isA, @codescheme);

INSERT INTO concept_property (dbid, property, concept)
SELECT dbid, @isA, @dataprop FROM concept WHERE id in ('DMD_numerator_value', 'DMD_numerator_units', 'DMD_denominator_value', 'DMD_denominator_units');

INSERT INTO concept_property (dbid, property, value)
VALUES (@scheme, @prefix, 'DMD_');

INSERT INTO concept_range (dbid, `range`)
SELECT dbid, 'Numeric'
FROM concept WHERE id in ('DMD_numerator_value', 'DMD_denominator_value');

INSERT INTO concept_range (dbid, `range`)
SELECT dbid, 'DMD_UOM'
FROM concept WHERE id in ('DMD_numerator_units', 'DMD_denominator_units');

-- ********************* UNITS OF MEASURE *********************
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('DMD_', v.cd), if(length(v.desc) > 255, concat(left(v.desc, 252), '...'), v.desc), v.desc, @scheme, v.cd
FROM dmd_lu_uom v;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, @uom
FROM dmd_lu_uom v
JOIN concept c ON c.id = concat('DMD_', v.cd);

-- ********************* VIRTUAL THERAPEUTIC MOIETY *********************

-- Create concepts
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('DMD_', v.vtmid), ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)), v.nm, @scheme, v.vtmid
FROM dmd_vtm v
WHERE v.invalid IS NULL;

INSERT INTO concept_property (dbid, property, concept)
SELECT dbid, @codescheme, @scheme
FROM dmd_vtm v
JOIN concept c ON c.id = concat('DMD_', v.vtmid)
WHERE v.invalid IS NULL;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, @vtm
FROM dmd_vtm v
JOIN concept c ON c.id = concat('DMD_', v.vtmid)
WHERE v.invalid IS NULL;

-- ********************* VIRTUAL MEDICINAL PRODUCTS *********************

-- Create concepts
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('DMD_', v.vpid), ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)), v.nm, @scheme, v.vpid
FROM dmd_vmp v
WHERE v.invalid IS NULL;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, @vmp
FROM dmd_vmp v
JOIN concept c ON c.id = concat('DMD_', v.vpid)
WHERE v.invalid IS NULL;

-- Relationships
INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @moiety, m.dbid
FROM dmd_vmp rel
JOIN concept c ON c.id = concat('DMD_', rel.vpid)
JOIN concept m ON m.id = concat('DMD_', rel.vtmid)
WHERE rel.vtmid IS NOT NULL
  AND rel.invalid IS NULL;

-- ********************* VIRTUAL MEDICINAL PRODUCT PACKS *********************
-- Create concepts
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('DMD_', v.vppid), ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)), v.nm, @scheme, v.vppid
FROM dmd_vmpp v
WHERE v.invalid IS NULL;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, @vmpp
FROM dmd_vmpp v
JOIN concept c ON c.id = concat('DMD_', v.vppid)
WHERE v.invalid IS NULL;

-- Relationships
INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @pack, p.dbid
FROM dmd_vmpp rel
JOIN concept c ON c.id = concat('DMD_', rel.vppid)
JOIN concept p ON p.id = concat('DMD_', rel.vpid)
WHERE rel.invalid IS NULL;


-- ********************* ACTUAL MEDICINAL PRODUCTS *********************
-- Create concepts
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('DMD_', v.apid), ifnull(v.abbrevnm, v.desc), null, @scheme, v.apid
FROM dmd_amp v
WHERE v.invalid IS NULL;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, @amp
FROM dmd_amp v
JOIN concept c ON c.id = concat('DMD_', v.apid)
WHERE v.invalid IS NULL;

-- Relationships
INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @branded, p.dbid
FROM dmd_amp rel
JOIN concept c ON c.id = concat('DMD_', rel.apid)
JOIN concept p ON p.id = concat('DMD_', rel.vpid)
WHERE rel.invalid IS NULL;

-- ********************* ACTUAL MEDICINAL PRODUCT PACKS *********************
-- Create concepts
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('DMD_', v.appid), ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)), v.nm, @scheme, v.appid
FROM dmd_ampp v
WHERE v.invalid IS NULL;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, @ampp
FROM dmd_ampp v
JOIN concept c ON c.id = concat('DMD_', v.appid)
WHERE v.invalid IS NULL;

-- Relationships
INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @branded, p.dbid
FROM dmd_ampp rel
JOIN concept c ON c.id = concat('DMD_', rel.appid)
JOIN concept p ON p.id = concat('DMD_', rel.vppid)
WHERE rel.invalid IS NULL;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @pack, p.dbid
FROM dmd_ampp rel
JOIN concept c ON c.id = concat('DMD_', rel.appid)
JOIN concept p ON p.id = concat('DMD_', rel.apid)
WHERE rel.invalid IS NULL;

-- ********************* VIRTUAL PRODUCT INGREDIENT *********************
-- Create concepts
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('DMD_', v.isid), if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm), v.nm, @scheme, v.isid
FROM dmd_ingredient v
WHERE v.invalid = 0;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, @ingredient
FROM dmd_ingredient v
JOIN concept c ON c.id = concat('DMD_', v.isid)
WHERE v.invalid IS NULL;

-- Relationships
INSERT INTO concept_property (dbid, property, value)
SELECT c.dbid, @numval, rel.strnt_nmrtr_val
FROM dmd_vmp_vpi rel
JOIN dmd_ingredient i ON i.isid = rel.isid AND i.invalid = 0
JOIN concept c ON c.id = concat('DMD_', rel.isid)
WHERE rel.strnt_nmrtr_val IS NOT NULL;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @numuom, p.dbid
FROM dmd_vmp_vpi rel
JOIN dmd_ingredient i ON i.isid = rel.isid AND i.invalid = 0
JOIN concept c ON c.id = concat('DMD_', rel.isid)
JOIN concept p ON p.id = concat('DMD_', rel.strnt_nmrtr_uomcd);

INSERT INTO concept_property (dbid, property, value)
SELECT c.dbid, @denval, rel.strnt_dnmtr_val
FROM dmd_vmp_vpi rel
JOIN dmd_ingredient i ON i.isid = rel.isid AND i.invalid = 0
JOIN concept c ON c.id = concat('DMD_', rel.isid)
WHERE rel.strnt_dnmtr_val IS NOT NULL;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @denuom, p.dbid
FROM dmd_vmp_vpi rel
JOIN dmd_ingredient i ON i.isid = rel.isid AND i.invalid = 0
JOIN concept c ON c.id = concat('DMD_', rel.isid)
JOIN concept p ON p.id = concat('DMD_', rel.strnt_dnmtr_uomcd);
