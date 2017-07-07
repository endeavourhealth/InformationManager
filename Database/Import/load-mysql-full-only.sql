/* loads the SNOMED CT 'Full' release - replace filenames with relevant locations of base SNOMED CT release files*/

/* Filenames may need to change depending on the release you wish to upload, currently set to January 2015 release */

load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Terminology/sct2_Concept_Full_GB1000000_20170401.txt' 
	into table concept_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;

load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Terminology/sct2_Description_Full-en-GB_GB1000000_20170401.txt' 
	into table description_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;
/*
load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Terminology/sct2_TextDefinition_Full-en-GB_GB1000000_20170401.txt' 
	into table textdefinition_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;
*/
load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Terminology/sct2_Relationship_Full_GB1000000_20170401.txt' 
	into table relationship_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;

load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Terminology/sct2_StatedRelationship_Full_GB1000000_20170401.txt' 
	into table stated_relationship_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;

load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Refset/Language/der2_cRefset_LanguageFull-en-GB_GB1000000_20170401.txt' 
	into table langrefset_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;

load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Refset/Content/der2_cRefset_AssociationReferenceFull_GB1000000_20170401.txt' 
	into table associationrefset_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;

load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Refset/Content/der2_cRefset_AttributeValueFull_GB1000000_20170401.txt' 
	into table attributevaluerefset_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;

load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Refset/Map/der2_sRefset_SimpleMapFull_GB1000000_20170401.txt' 
	into table simplemaprefset_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;

load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Refset/Content/der2_Refset_SimpleFull_GB1000000_20170401.txt' 
	into table simplerefset_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;

load data local 
	infile 'D:/My Files/Business/Bear Claw/Endeavour/Information Model/SnomedCT_UKClinicalRF2_Production_20170401T000001/Full/Refset/Map/der2_iisssccRefset_ExtendedMapFull_GB1000000_20170401.txt' 
	into table complexmaprefset_f
	columns terminated by '\t' 
	lines terminated by '\r\n' 
	ignore 1 lines;
























