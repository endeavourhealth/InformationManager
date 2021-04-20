USE IM6;

INSERT INTO concept_search(term, concept_dbid)
SELECT iri, dbid FROM concept;

INSERT INTO concept_search(term, concept_dbid)
SELECT code, dbid FROM concept;

INSERT INTO concept_search(term, concept_dbid)
SELECT name, dbid FROM concept;

INSERT INTO concept_search(term, concept_dbid)
SELECT term, dbid FROM concept_term;