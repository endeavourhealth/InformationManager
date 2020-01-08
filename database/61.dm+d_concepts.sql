-- Common/usefule ids
SELECT @coreActive := dbid FROM concept WHERE id = 'CoreActive';
SELECT @coreInactive := dbid FROM concept WHERE id = 'CoreInactive';
SELECT @codeScheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @subtypeOf := dbid FROM concept WHERE id = 'subtypeOf';
SELECT @replacedBy := dbid FROM concept WHERE id = 'replacedBy';
SELECT @property := dbid FROM concept WHERE id = 'property';
SELECT @dataType := dbid FROM concept WHERE id = 'subtypeOf';
SELECT @numeric := dbid FROM concept WHERE id = 'numericProperty';

-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/DMD', '1.0.0');

SET @model = LAST_INSERT_ID();

-- ********************* DM+D CONCEPTS *********************
INSERT INTO concept(model, status, id, name, description)
VALUES
(@model, @coreActive, 'DMD_VTM', 'Virtual therapeutic moiety', null),
(@model, @coreActive, 'DMD_VMP', 'Virtual medicinal product', null),
(@model, @coreActive, 'DMD_has_moiety', 'Has moiety relationship', null),
(@model, @coreActive, 'DMD_VMPP', 'Virtual medicinal product pack', null),
(@model, @coreActive, 'DMD_is_pack_of', 'Is pack of relationship', null),
(@model, @coreActive, 'DMD_AMP', 'Actual medicinal product', null),
(@model, @coreActive, 'DMD_is_branded', 'An actual (branded) instance of a virtual (generic) product', null),
(@model, @coreActive, 'DMD_AMPP', 'Actual medicinal product pack', null),
(@model, @coreActive, 'DMD_has_ingredient', 'Has ingredient relationship', null),
(@model, @coreActive, 'DMD_Ingredient', 'Ingredient', null),
(@model, @coreActive, 'DM+D', 'DM+D code scheme', 'Dictionary of Medicines & Devices'),
(@model, @coreActive, 'DMD_UOM', 'Units of measure', 'DM+D specified units of measure'),
(@model, @coreActive, 'DMD_numerator_value', 'DM+D numerator value', 'Numerator value for an ingredient'),
(@model, @coreActive, 'DMD_numerator_units', 'DM+D numerator units', 'Numerator unit of measure for an ingredient'),
(@model, @coreActive, 'DMD_denominator_value', 'DM+D denominator value', 'Denominator value for an ingredient'),
(@model, @coreActive, 'DMD_denominator_units', 'DM+D denominator units', 'Denominator unit of measure for an ingredient')
;

SELECT @dmdScheme := dbid FROM concept WHERE id = 'DM+D';
SELECT @hasMoiety := dbid FROM concept WHERE id = 'DMD_has_moiety';

INSERT INTO concept_relation (subject, relation, object)
VALUES (@dmdScheme, @subtypeOf, @codeScheme);

INSERT INTO concept_relation (subject, relation, object)
SELECT dbid, @subtypeOf, @property
FROM concept
WHERE id in ('DMD_numerator_value', 'DMD_numerator_units', 'DMD_denominator_value', 'DMD_denominator_units');


INSERT INTO concept_relation (subject, relation, object)
SELECT dbid, @dataType, @numeric
FROM concept
WHERE id in ('DMD_numerator_value', 'DMD_denominator_value');

INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @dataType, u.dbid
FROM concept c
JOIN concept u ON u.id = 'DMD_UOM'
WHERE c.id in ('DMD_numerator_value', 'DMD_denominator_value');

-- ********************* UNITS OF MEASURE *********************
INSERT INTO concept(model, status, id, name, description, codeScheme, code)
SELECT @model, @coreActive,
       concat('DMD_', v.cd),
       if(length(v.desc) > 255, concat(left(v.desc, 252), '...'), v.desc),
       v.desc,
       @dmdScheme,
       v.cd
FROM dmd_lu_uom v;

INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @subtypeOf, u.dbid
FROM dmd_lu_uom v
JOIN concept c ON c.id = concat('DMD_', v.cd)
JOIN concept u ON u.id = 'DMD_UOM';

-- ********************* VIRTUAL THERAPEUTIC MOIETY *********************

-- Create concepts
INSERT INTO concept(model, status, id, name, description, codeScheme, code)
SELECT @model, @coreActive,
        concat('DMD_', v.vtmid),
        ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)),
        v.nm,
        @dmdScheme,
        v.vtmid
FROM dmd_vtm v
WHERE v.invalid IS NULL;


INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @subtypeOf, o.dbid
FROM dmd_vtm v
JOIN concept c ON c.id = concat('DMD_', v.vtmid)
JOIN concept o ON o.id = 'DMD_VTM';

-- ********************* VIRTUAL MEDICINAL PRODUCTS *********************

-- Create concepts
INSERT INTO concept(model, status, id, name, description, codeScheme, code)
SELECT @model, @coreActive,
        concat('DMD_', v.vpid),
        ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)),
        v.nm,
        @dmdScheme,
        v.vpid
FROM dmd_vmp v
WHERE v.invalid IS NULL;

INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @subtypeOf, o.dbid
FROM dmd_vmp v
JOIN concept c ON c.id = concat('DMD_', v.vpid)
JOIN concept o ON o.id = 'DMD_VMP'
WHERE v.invalid IS NULL;

-- Moiety
INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @hasMoiety, o.dbid
FROM dmd_vmp v
JOIN concept c ON c.id = concat('DMD_', v.vpid)
JOIN concept o ON o.id = concat('DMD_', v.vtmid)
WHERE v.invalid IS NULL
AND v.vtmid IS NOT NULL;

-- ********************* VIRTUAL MEDICINAL PRODUCT PACKS *********************
-- Create concepts
INSERT INTO concept(model, status, id, name, description, codeScheme, code)
SELECT @model,
        @coreActive,
        concat('DMD_', v.vppid),
        ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)),
        v.nm,
        @dmdScheme,
        v.vppid
FROM dmd_vmpp v
WHERE v.invalid IS NULL;

INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @subtypeOf, o.dbid
FROM dmd_vmpp v
JOIN concept c ON c.id = concat('DMD_', v.vppid)
JOIN concept o ON o.id = 'DMD_VMPP'
WHERE v.invalid IS NULL;

-- Virtual product pack
INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, r.dbid, o.dbid
FROM dmd_vmpp v
JOIN concept c ON c.id = concat('DMD_', v.vppid)
JOIN concept o ON o.id = concat('DMD_', v.vpid)
JOIN concept r ON r.id = 'DMD_is_pack_of'
WHERE v.invalid IS NULL
AND v.vpid IS NOT NULL;

-- ********************* ACTUAL MEDICINAL PRODUCTS *********************
-- Create concepts
INSERT INTO concept(model, status, id, name, description, codeScheme, code)
SELECT @model, @coreActive,
        concat('DMD_', v.apid),
        ifnull(v.abbrevnm, v.desc),
        v.desc,
        @dmdScheme,
        v.apid
FROM dmd_amp v
WHERE v.invalid IS NULL;

INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @subtypeOf, o.dbid
FROM dmd_amp v
         JOIN concept c ON c.id = concat('DMD_', v.apid)
         JOIN concept o ON o.id = 'DMD_AMP'
WHERE v.invalid IS NULL;

-- Branded
INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, r.dbid, o.dbid
FROM dmd_amp v
         JOIN concept c ON c.id = concat('DMD_', v.apid)
         JOIN concept o ON o.id = concat('DMD_', v.vpid)
         JOIN concept r ON r.id = 'DMD_is_branded'
WHERE v.invalid IS NULL
  AND v.vpid IS NOT NULL;

-- ********************* ACTUAL MEDICINAL PRODUCT PACKS *********************
-- Create concepts
INSERT INTO concept(model, status, id, name, description, codeScheme, code)
SELECT @model, @coreActive,
        concat('DMD_', v.appid),
        ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)),
        v.nm,
        @dmdScheme,
        v.appid
FROM dmd_ampp v
WHERE v.invalid IS NULL;

INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @subtypeOf, o.dbid
FROM dmd_ampp v
         JOIN concept c ON c.id = concat('DMD_', v.appid)
         JOIN concept o ON o.id = 'DMD_AMPP'
WHERE v.invalid IS NULL;

-- Branded
INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, r.dbid, o.dbid
FROM dmd_ampp v
         JOIN concept c ON c.id = concat('DMD_', v.appid)
         JOIN concept o ON o.id = concat('DMD_', v.vppid)
         JOIN concept r ON r.id = 'DMD_is_branded'
WHERE v.invalid IS NULL
  AND v.vppid IS NOT NULL;

-- Pack
INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, r.dbid, o.dbid
FROM dmd_ampp v
         JOIN concept c ON c.id = concat('DMD_', v.appid)
         JOIN concept o ON o.id = concat('DMD_', v.apid)
         JOIN concept r ON r.id = 'DMD_is_pack_of'
WHERE v.invalid IS NULL
  AND v.apid IS NOT NULL;

-- ********************* VIRTUAL PRODUCT INGREDIENT *********************
-- Create concepts
INSERT INTO concept(model, status, id, name, description, codeScheme, code)
SELECT @model, @coreActive,
        concat('DMD_', v.isid),
        if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm),
        v.nm,
        @dmdScheme,
        v.isid
FROM dmd_ingredient v
WHERE v.invalid = 0;

INSERT INTO concept_relation (subject, relation, object)
SELECT c.dbid, @subtypeOf, o.dbid
FROM dmd_ingredient v
         JOIN concept c ON c.id = concat('DMD_', v.isid)
         JOIN concept o ON o.id = 'DMD_Ingredient'
WHERE v.invalid IS NULL;

-- Numerator value
INSERT INTO concept_property_data (concept, relation, value)
SELECT c.dbid, r.dbid, v.strnt_nmrtr_val
FROM dmd_vmp_vpi v
    JOIN dmd_ingredient i ON i.isid = v.isid AND i.invalid = 0
    JOIN concept c ON c.id = concat('DMD_', v.isid)
    JOIN concept r ON r.id = 'DMD_numerator_value'
WHERE v.strnt_nmrtr_val IS NOT NULL;

-- Numerator uom
INSERT INTO concept_property_data (concept, relation, value)
SELECT c.dbid, r.dbid, v.strnt_nmrtr_val
FROM dmd_vmp_vpi v
         JOIN dmd_ingredient i ON i.isid = v.isid AND i.invalid = 0
         JOIN concept c ON c.id = concat('DMD_', v.isid)
         JOIN concept r ON r.id = 'DMD_numerator_uom'
        JOIN concept u ON u.id = concat('DMD_', v.strnt_nmrtr_uomcd)
WHERE v.strnt_nmrtr_uomcd IS NOT NULL;

-- Denominator value
INSERT INTO concept_property_data (concept, relation, value)
SELECT c.dbid, r.dbid, v.strnt_dnmtr_val
FROM dmd_vmp_vpi v
         JOIN dmd_ingredient i ON i.isid = v.isid AND i.invalid = 0
         JOIN concept c ON c.id = concat('DMD_', v.isid)
         JOIN concept r ON r.id = 'DMD_denominator_value'
WHERE v.strnt_dnmtr_val IS NOT NULL;

-- Denominator uom
INSERT INTO concept_property_data (concept, relation, value)
SELECT c.dbid, r.dbid, v.strnt_nmrtr_val
FROM dmd_vmp_vpi v
         JOIN dmd_ingredient i ON i.isid = v.isid AND i.invalid = 0
         JOIN concept c ON c.id = concat('DMD_', v.isid)
         JOIN concept r ON r.id = 'DMD_denominator_uom'
         JOIN concept u ON u.id = concat('DMD_', v.strnt_dnmtr_uomcd)
WHERE v.strnt_dnmtr_uomcd IS NOT NULL;

