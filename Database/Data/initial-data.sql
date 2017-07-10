INSERT INTO information_model.concept
(id, name, status, class, short_name, description)
VALUES
-- CLASS DEFINITIONS (class = 1)
(1, 'Class', 0, 1, 'CLSS', 'All classes are concrete classes of an abstract class for example a Concept class is "class"'),
(2, 'Concept', 0, 1, 'CNCPT', 'has an identifier a name and a class All objects (including classes and relationships) other than simple data types are also concepts'),
(3, 'Record type', 0, 1, 'RCTYP', 'An entry that holds stored data. Variously referred to as a table or resource and sometimes link tables. A record type  has fields'),
(4, 'Code concept', 0, 1, 'CDCNCPT', 'A codeable concept that "is a " child of another concept. It might be further defined by attribute value pairs or attribute groups or additional codeable concepts'),
(5, 'Attribute group', 0, 1, 'ATTRBTGRP', 'A grouping construct for attributes holding one or more attribute value pairs'),
(6, 'Quantity', 0, 1, 'QNTTY', 'An attribute group specialising in holding units and values'),
(7, 'Range', 0, 1, 'RNG', 'structure that has a range type operator and upper and lower value'),
(8, 'Numeric', 0, 1, 'NMRC', 'A type of field that holds a number (integer or float)'),
(9, 'Date', 0, 1, 'DT', 'A type of field that holds a date'),
(10, 'Code', 0, 1, 'CD', 'A field that holds a simple code'),
(11, 'Text', 0, 1, 'TXT', 'Contains a simple text value'),
(12, 'Boolean', 0, 1, 'BLN', 'Only a 1 or a zero (Y or N)'),
(13, 'Codeable concept', 0, 1, 'CDBLCNCPT', 'A field that holds a code that is a concept in the information model'),
-- 14 ????
(15, 'Relationship', 0, 1, 'RLTNSHP', 'A concept only used in the relationship links between one concept and another, a specialised form of attribute'),
(16, 'Field', 0, 1, 'FLD', 'A concept that is a field of a record type (and each field has a value type relationship which points to a class that describes the type of value the field holds)'),
(17, 'Abstract field', 0, 1, 'ABSTRCTFLD', 'A concept that is a generic field which can be used to author real fields. For example an “effective date” is an abstract field but when used in an observation is “effective date (observation)” All actual fields are based on abstract fields in order to maintain the discipline of unique field names'),
(18, 'Folder', 0, 1, 'FLDR', 'Type of concept that is a folder for UI navigation'),
(19, 'Attribute', 0, 1, 'ATTRBT', 'The name of an attribute in the expressions (i.e. an attribute has a value that may be an attribute group)'),
-- RELATIONSHIP DEFINITIONS (class = 15)
(100, 'is a ', 0, 15, 'ISA', 'Means that the concept Child is a type of parent concept. This is used in queries on concepts that subsume child concept. '),
(101, 'copies fields', 0, 15, 'CPSFLDS', 'Means that the concept C copies the names of all fields from concept P and may have additional fields. For example:<P>Numeric observation -> inherits fields - > Observation<P>This relationship is used at authoring time but not at run time'),
(102, 'has child', 0, 15, 'HSCHLD', 'used to populate a tree like structure in a browser as is UI parent. Other than use in the UI this relationship has no inherent meaning. However, in the clinical browser nearly all UI branch children are linked via an Is a relationship or inherits fields relationship.<p>Version controlled so that different models can be implemented using the same underlying concepts.'),
(103, 'has parent', 0, 15, 'HSPRNT', 'referse of has child'),
(104, 'has reciprocal', 0, 15, 'HSRCPRCL', 'Links one relationship as a reciprocal of another relationship so that target concepts are linked to parents by the reverse relationship'),
(105, 'has subtype', 0, 15, 'HSSBTYPE', ' Reciprocal of is a'),
(106, 'has field', 0, 15, 'HSFLD', 'Field of a concept that is a record type'),
(107, 'has value type', 0, 15, 'HSVLTYP', 'Fields have values of a certain class e.g. date, numeric, codeable concept'),
(108, 'has preferred value set', 0, 15, 'HSPRFRRDVLST', 'Points to one or more value sets that a field should contain'),
(109, 'has linked record type', 0, 15, 'HSLNKDRCRDTYP', 'When a field links to a different record type (e.g. an address) the record concept it links to'),
(110, 'has linked field', 0, 15, 'HSLNKDFLD', 'When a field links directly to a field in another record type the field it links to (used together with the preferred value set (e.g. patient ethnicity links to an observation field containing codes from the ethnicity value set) this enables query mapping using this as a guide'),
(111, 'derived from field', 0, 15, 'DRVDFRMFLD', 'The abstract field that the field is copied from (e.g. observation effective date is derived from effective date)')
;

INSERT INTO information_model.table_identity
(table_name, next_id)
VALUES
('concept', 1000000000000);
