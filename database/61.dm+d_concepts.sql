-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/DMD', '1.0.0');

SET @model = LAST_INSERT_ID();

-- ********************* DM+D CONCEPTS *********************
INSERT INTO concept(model, data)
VALUES
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_VTM', 'name', 'Virtual therapeutic moiety')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_VMP', 'name', 'Virtual medicinal product')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_has_moiety', 'name', 'Has moiety relationship')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_VMPP', 'name', 'Virtual medicinal product pack')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_is_pack_of', 'name', 'Is pack of relationship')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_AMP', 'name', 'Actual medicinal product')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_is_branded', 'name', 'An actual (branded) instance of a virtual (generic) product')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_AMPP', 'name', 'Actual medicinal product pack')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_has_ingredient', 'name', 'Has ingredient relationship')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_Ingredient', 'name', 'Ingredient')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DM+D', 'name', 'DM+D code scheme', 'description', 'Dictionary of Medicines & Devices')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_UOM', 'name', 'Units of measure', 'description', 'DM+D specified units of measure')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_numerator_value', 'name', 'DM+D numerator value', 'description', 'Numerator value for an ingredient')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_numerator_units', 'name', 'DM+D numerator units', 'description', 'Numerator unit of measure for an ingredient')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_denominator_value', 'name', 'DM+D denominator value', 'description', 'Denominator value for an ingredient')),
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'DMD_denominator_units', 'name', 'DM+D denominator units', 'description', 'Denominator unit of measure for an ingredient'))
;

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept','CodeScheme'))
    )
FROM concept
WHERE id = 'DM+D';

SELECT @CoreActive:=dbid FROM concept WHERE id = 'CoreActive';

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'dataProperty'))
    )
FROM concept
WHERE id in ('DMD_numerator_value', 'DMD_numerator_units', 'DMD_denominator_value', 'DMD_denominator_units');


INSERT INTO property_range (property, status, range_class)
SELECT dbid, @CoreActive, JSON_OBJECT('dataType', 'Numeric')
FROM concept
WHERE id in ('DMD_numerator_value', 'DMD_denominator_value');

INSERT INTO property_range (property, status, range_class)
SELECT dbid, @CoreActive, JSON_OBJECT('class', 'DMD_UOM')
FROM concept
WHERE id in ('DMD_numerator_units', 'DMD_denominator_units');

-- ********************* UNITS OF MEASURE *********************
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('DMD_', v.cd),
        'name', if(length(v.desc) > 255, concat(left(v.desc, 252), '...'), v.desc),
        'description', v.desc,
        'codeScheme', 'DM+D',
        'code', v.cd
    )
FROM dmd_lu_uom v;

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'DMD_UOM'))
    )
FROM dmd_lu_uom v
         JOIN concept c ON c.id = concat('DMD_', v.cd);

-- ********************* VIRTUAL THERAPEUTIC MOIETY *********************

-- Create concepts
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('DMD_', v.vtmid),
        'name', ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)),
        'description', v.nm,
        'codeScheme', 'DM+D',
        'code', v.vtmid
    )
FROM dmd_vtm v
WHERE v.invalid IS NULL;

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'DMD_VTM'))
    )
FROM dmd_vtm v
         JOIN concept c ON c.id = concat('DMD_', v.vtmid);

-- ********************* VIRTUAL MEDICINAL PRODUCTS *********************

-- Create concepts
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('DMD_', v.vpid),
        'name', ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)),
        'description', v.nm,
        'codeScheme', 'DM+D',
        'code', v.vpid
    )
FROM dmd_vmp v
WHERE v.invalid IS NULL;

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'DMD_VMP'))
    )
FROM dmd_vmp v
         JOIN concept c ON c.id = concat('DMD_', v.vpid)
WHERE v.invalid IS NULL;

-- Moiety
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN dmd_vmp rel ON rel.vpid = c.code AND c.scheme = 'DM+D'
SET cd.data = JSON_MERGE_PRESERVE(cd.data, JSON_OBJECT(
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('roleGroup', JSON_OBJECT('operator', 'AND',
                            'attribute', JSON_OBJECT(
                                    'property', 'DMD_has_moiety',
                                    'valueConcept', JSON_OBJECT('concept', concat('DMD_', rel.vtmid))
                                )
                    ))
            )
    ))
WHERE rel.vtmid IS NOT NULL
  AND rel.invalid IS NULL;

-- ********************* VIRTUAL MEDICINAL PRODUCT PACKS *********************
-- Create concepts
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('DMD_', v.vppid),
        'name', ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)),
        'description', v.nm,
        'codeScheme', 'DM+D',
        'code', v.vppid
    )
FROM dmd_vmpp v
WHERE v.invalid IS NULL;

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'DMD_VMPP'))
    )
FROM dmd_vmpp v
         JOIN concept c ON c.id = concat('DMD_', v.vppid)
WHERE v.invalid IS NULL;

-- Virtual product pack
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN dmd_vmpp rel ON rel.vppid = c.code AND c.scheme = 'DM+D'
SET cd.data = JSON_MERGE_PRESERVE(cd.data, JSON_OBJECT(
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('roleGroup', JSON_OBJECT('operator', 'AND',
                            'attribute', JSON_OBJECT(
                                    'property', 'DMD_is_pack_of',
                                    'valueConcept', JSON_OBJECT('concept', concat('DMD_', rel.vpid))
                                )
                    ))
            )
    ))
WHERE rel.invalid IS NULL
  AND rel.vpid IS NOT NULL;


-- ********************* ACTUAL MEDICINAL PRODUCTS *********************
-- Create concepts
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('DMD_', v.apid),
        'name', ifnull(v.abbrevnm, v.desc),
        'description', v.desc,
        'codeScheme', 'DM+D',
        'code', v.apid
    )
FROM dmd_amp v
WHERE v.invalid IS NULL;

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'DMD_AMP'))
    )
FROM dmd_amp v
         JOIN concept c ON c.id = concat('DMD_', v.apid)
WHERE v.invalid IS NULL;

-- Branded
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN dmd_amp rel ON rel.apid = c.code AND c.scheme = 'DM+D'
SET cd.data = JSON_MERGE_PRESERVE(cd.data, JSON_OBJECT(
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('roleGroup', JSON_OBJECT('operator', 'AND',
                            'attribute', JSON_OBJECT(
                                    'property', 'DMD_is_branded',
                                    'valueConcept', JSON_OBJECT('concept', concat('DMD_', rel.vpid))
                                )
                    ))
            )
    ))
WHERE rel.invalid IS NULL
  AND rel.vpid IS NOT NULL;

-- ********************* ACTUAL MEDICINAL PRODUCT PACKS *********************
-- Create concepts
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('DMD_', v.appid),
        'name', ifnull(v.abbrevnm, if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm)),
        'description', v.nm,
        'codeScheme', 'DM+D',
        'code', v.appid
    )
FROM dmd_ampp v
WHERE v.invalid IS NULL;

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'DMD_AMPP'))
    )
FROM dmd_ampp v
         JOIN concept c ON c.id = concat('DMD_', v.appid)
WHERE v.invalid IS NULL;

-- Branded
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN dmd_ampp rel ON rel.appid = c.code AND c.scheme = 'DM+D'
SET cd.data = JSON_MERGE_PRESERVE(cd.data, JSON_OBJECT(
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('roleGroup', JSON_OBJECT('operator', 'AND',
                            'attribute', JSON_OBJECT(
                                    'property', 'DMD_is_branded',
                                    'valueConcept', JSON_OBJECT('concept', concat('DMD_', rel.vppid))
                                )
                    ))
            )
    ))
WHERE rel.invalid IS NULL
  AND rel.vppid IS NOT NULL;

-- Pack
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN dmd_ampp rel ON rel.appid = c.code AND c.scheme = 'DM+D'
SET cd.data = JSON_MERGE_PRESERVE(cd.data, JSON_OBJECT(
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('roleGroup', JSON_OBJECT('operator', 'AND',
                            'attribute', JSON_OBJECT(
                                    'property', 'DMD_is_pack_of',
                                    'valueConcept', JSON_OBJECT('concept', concat('DMD_', rel.apid))
                                )
                    ))
            )
    ))
WHERE rel.invalid IS NULL
  AND rel.apid IS NOT NULL;

-- ********************* VIRTUAL PRODUCT INGREDIENT *********************
-- Create concepts
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('DMD_', v.isid),
        'name', if(length(v.nm) > 255, concat(left(v.nm, 252), '...'), v.nm),
        'description', v.nm,
        'codeScheme', 'DM+D',
        'code', v.isid
    )
FROM dmd_ingredient v
WHERE v.invalid = 0;

INSERT INTO concept_definition (concept, data)
SELECT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'DMD_Ingredient'))
    )
FROM dmd_ingredient v
         JOIN concept c ON c.id = concat('DMD_', v.isid)
WHERE v.invalid = 0;

-- Numerator value
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN dmd_vmp_vpi rel ON rel.isid = c.code AND c.scheme = 'DM+D'
    JOIN dmd_ingredient i ON i.isid = rel.isid AND i.invalid = 0
SET cd.data = JSON_MERGE_PRESERVE(cd.data, JSON_OBJECT(
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('roleGroup', JSON_OBJECT('operator', 'AND',
                            'attribute', JSON_OBJECT(
                                    'property', 'DMD_numerator_value',
                                    'value', rel.strnt_nmrtr_val)
                    ))
            )
    ))
WHERE rel.strnt_nmrtr_val IS NOT NULL;

-- Numerator uom
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN dmd_vmp_vpi rel ON rel.isid = c.code AND c.scheme = 'DM+D'
    JOIN dmd_ingredient i ON i.isid = rel.isid AND i.invalid = 0
SET cd.data = JSON_MERGE_PRESERVE(cd.data, JSON_OBJECT(
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('roleGroup', JSON_OBJECT('operator', 'AND',
                            'attribute', JSON_OBJECT(
                                    'property', 'DMD_numerator_uom',
                                    'valueConcept', JSON_OBJECT('concept', concat('DMD_', rel.strnt_nmrtr_uomcd))
                                )
                    ))
            )
    ))
WHERE rel.strnt_nmrtr_uomcd IS NOT NULL;

-- Denominator value
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN dmd_vmp_vpi rel ON rel.isid = c.code AND c.scheme = 'DM+D'
    JOIN dmd_ingredient i ON i.isid = rel.isid AND i.invalid = 0
SET cd.data = JSON_MERGE_PRESERVE(cd.data, JSON_OBJECT(
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('roleGroup', JSON_OBJECT('operator', 'AND',
                            'attribute', JSON_OBJECT(
                                    'property', 'DMD_denominator_value',
                                    'value', rel.strnt_dnmtr_val)
                    )
            )
    ))
WHERE rel.strnt_dnmtr_val IS NOT NULL;

-- Denominator uom
UPDATE concept_definition cd
    JOIN concept c ON c.dbid = cd.concept
    JOIN dmd_vmp_vpi rel ON rel.isid = c.code AND c.scheme = 'DM+D'
    JOIN dmd_ingredient i ON i.isid = rel.isid AND i.invalid = 0
SET cd.data = JSON_MERGE_PRESERVE(cd.data, JSON_OBJECT(
        'subtypeOf', JSON_ARRAY(
                JSON_OBJECT('roleGroup', JSON_OBJECT('operator', 'AND',
                            'attribute', JSON_OBJECT(
                                    'property', 'DMD_denominator_uom',
                                    'valueConcept', JSON_OBJECT('concept', concat('DMD_', rel.strnt_dnmtr_uomcd))
                                )
                    ))
            )
    ))
WHERE rel.strnt_dnmtr_uomcd IS NOT NULL;
