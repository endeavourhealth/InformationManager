USE IM6;

LOCK TABLES concept_search WRITE, concept READ, term_code READ;

INSERT INTO concept_search(term, concept_dbid)
SELECT iri, dbid FROM concept;

INSERT INTO concept_search(term, concept_dbid)
SELECT code, dbid FROM concept;

INSERT INTO concept_search(term, concept_dbid)
SELECT name, dbid FROM concept;

INSERT INTO concept_search(term, concept_dbid)
SELECT term, concept FROM term_code;

UNLOCK TABLES;

