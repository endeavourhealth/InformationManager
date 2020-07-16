# Information Model Manager
![Version](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild/badges/InformationManager/version.svg)
![Build Status](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild/badges/InformationManager/build.svg)
![Unit Tests](https://s3.eu-west-2.amazonaws.com/endeavour-codebuild/badges/InformationManager/unit-test.svg)

## Build Angular Frontend

Checkout from GIT

`cd FrontEnd`

`npm install` (you only have to do this once)

`npm run start`

Navigate your browser to `http://localhost:4200`

The proxy is configured to expect the API to be running on localhost:8000

## Vanilla database build
1. Use the scripts in `1.Source-data` to build a meta database of the core/base ontologies (SNOMED, READ2, CTV3, etc)
2. Use the scripts in `3.IMvNext` to create the schema/tables
3. Import the core concepts from `IMFULL.json` using the DocumentImport utility
4. Populate the remainder of the concepts IMSource (1) database, using the `3.IMvNext/99.population-from-im_source.sql`
5. Import the inferred data from `IMFULL_isa.txt` using the RuntimeImporter utility
6. Rebuild the transitive closure table for "Is a" using the ClosureBuilder utility
7. Import the closure file using `3.IMvNext/06.closure-import.sql` (remember to copy/move the closure file to the MySQL upload folder!)
