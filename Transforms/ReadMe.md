
# Source data
Obtain and restore (e.g. via table SQL) from the Vision look up and TPP look up tables which represent their (partial) supplier maps
between Read 2 or CTV3 and Snomed

vision_read2_to_snomed_map

vision_read2_to_snomed_map

tpp_ctv3_lookup_2

tpp_ctv3_to_snomed

Create a root folder of choice.

Place the core Discovery ontology in sub folder Discovery

Place the noneCore Discovery ontology in the sub folder Discovery (containing legacy term codes)

Place the latest Snomed-CT releases (International, UK Clinical and UK Drug) as subfolders without change of name

Place the EMISCodes.csv file in the subfolder EMIS. This is the EMIS/Read2/Local code file with EMIS Snomed map.

Place the OPCS4-9-0-0  NHS provided files in their relevant sub folders without change of name

Place the OPCSChapters.txt file in the same directory. This contains the alhabetical chapter names and terms

Place the ICD1- NHS Trud provided ICD10-5-0  files in the relevant subfolders without change of name

N.B. The opcs 4 and ICD10 complex maps are part of the relevant RF2 Snomed release.

#Initial Population of the ontology from external sources
Note this will take a number of hours and populates the ontology and creates transitive closure

Run the AllImporter with the following arguments

1. Root folder

2. Import folder for Transitive closure table

Environment variables for establishing DB connection

Also note that the importers may also be run independently but should be done in strict order which is:

a) DiscoveryCoreImporter

b) SnomedImporter

c) R2EMISVisionImporter

CTV3TPPImporter,OPCSImporter, ICD1OImporter- may be run in any order after that

### Transitive closure (independent of the aboce)

Run the `ClosureBuilder` application with parameters `<MySQL uploads Folder>` to generate the transitive closure file for the "Is a" relationship.


