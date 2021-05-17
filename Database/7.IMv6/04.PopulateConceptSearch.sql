
LOCK TABLES concept_search WRITE, concept READ, term_code READ;

INSERT IGNORE INTO concept_search(term, concept_dbid)
SELECT code, dbid FROM concept;

INSERT IGNORE INTO concept_search(term, concept_dbid)
SELECT term, concept FROM term_code;

UNLOCK TABLES;

