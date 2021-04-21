USE IM6;

LOCK TABLES concept_search WRITE, concept READ, concept_term READ;

INSERT INTO concept_search(term, concept_dbid)
SELECT iri, dbid FROM concept;

INSERT INTO concept_search(term, concept_dbid)
SELECT code, dbid FROM concept;

INSERT INTO concept_search(term, concept_dbid)
SELECT name, dbid FROM concept;

INSERT INTO concept_search(term, concept_dbid)
SELECT term, concept FROM concept_term;

UNLOCK TABLES;
