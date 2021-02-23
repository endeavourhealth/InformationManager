#Vanilla database creation
Instructions for generating a vanilla database from scratch.

## Source data
Place the appropriate source files into the MySQL\Uploads folder and modify the scripts in `1.Source-data` accordingly.
Run each script in order to populate the source database with source data.

## Information Model database

### Initial database creation
Run the following scripts, in order: -
* `01.schema.sql` - Drop/create the database schema
* `02.tables.sql` - Drop/create the core information model tables

### Discovery data - document import
Run the DocumentImport application against the following files (datafiles/json directory), in order
* `CoreOntologyClassified.json`
* `NoneCoreOntology.json`

### External/3rd party data
Run the Transforms/Snomed Import on the latest snomed data files (Clinical, Drug History substitution & DataMigration)
Run the following scripts, in order: -
* `03.read2-import.sql` - Read 2
* `04.ctv3-import` - Read CTV3
* `05.emis-import` - EMIS local codes file
* `06.tpp-import` - TPP local codes file
* `07.barts-import` - Barts local codes file
* `08.icd10+opcs4-import` - ICD10 & OPCS4 mapping files
* `09.icd10-import` - ICD10 concepts
* `10.opcs4-import` - OPCS4 concepts


### Transitive closure
Run the `ClosureBuilder` application with parameters `<MySQL uploads Folder>` to generate the transitive closure file for the "Is a" relationship.
Run the `10.closure-import.sql` script.
