#Vanilla database creation
Instructions for generating a vanilla database from scratch.

## Source data
Place the appropriate source files into the MySQL\Uploads folder and modify the scripts in `1.Source-data` accordingly.
Run each script in order to populate the source database with source data.

## Information Model database

### Initial database creation
Run the following scripts, in order, where appropriate: -
* `01.config.sql` - Populate database connection config (optional)
* `02.schema.sql` - Drop/create the database schema
* `03.tables.sql` - Drop/create the core information model tables
* `04.seed-data.sql` - Insert the initial/base/seed data

### Discovery data - document import
Run the DocumentImport application against the following files (datafiles/json directory), in order
* Asserted: -
    * `CoreOntology.json`
    * `NoneCoreOntology.json`
* Inferred: -
    * `CoreOntology-inferred.json`
    * `NoneCoreOntology-inferred.json`

### Additional data - source import (optional)
Run the `99.population-from-im_source.sql` script to import SNOMED, READ2, CTV3, ICD10, OPCS4, EMIS local, TPP local, Vision local and Barts local data.

### Transitive closure
Run the `ClosureBuilder` application with parameters `sn:116680003 ..\datafiles\tct` to generate the transitive closure file for the "Is a" relationship.
Copy the output file (`datafiles\tct\sn116680003_closure.csv`) into the MySQL\Uploads folder. 
Run the `06.closure-import.sql` script.

### Foreign keys (optional)
The foreign key creation is separate to aid performance during data load.
Run the `05.foreign-keys.sql` script.
