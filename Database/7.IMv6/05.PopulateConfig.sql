INSERT INTO config(name,config)
VALUES
       ('quickAccessIri','[":SemanticEntity", ":HealthRecord", ":VSET_DataModel", ":VSET_QueryValueSets"]'),
       ('quickAccessCandidatesIri','[":DiscoveryCommonDataModel", ":VSET_ValueSet", ":SemanticEntity"]'),
       ('schemeMap','{
              "SNOMED" : "http://endhealth.info/im#891101000252101",
              "READ2" : "http://endhealth.info/im#891141000252104",
              "CTV3" : "http://endhealth.info/im#891051000252101",
              "ICD10" : "http://endhealth.info/im#891021000252109",
              "OPCS4" : "http://endhealth.info/im#891041000252103",
              "EMIS_LOCAL" : "http://endhealth.info/im#891031000252107",
              "TPP_LOCAL" : "http://endhealth.info/im#631000252102",
              "BartsCerner" : "http://endhealth.info/im#891081000252108",
              "VISION" : "http://endhealth.info/im#1000131000252104"
       }');