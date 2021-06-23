TRUNCATE entity_search;

LOCK TABLES entity_search WRITE, entity READ, term_code READ;

INSERT IGNORE INTO entity_search(term, entity_dbid)
SELECT code, dbid
FROM entity
WHERE code IS NOT NULL;

INSERT IGNORE INTO entity_search(term, entity_dbid)
SELECT term, entity
FROM term_code
WHERE term IS NOT NULL;

UNLOCK TABLES;

