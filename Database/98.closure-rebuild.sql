SELECT 'Make sure you regenerate the CSV files using the closure builder tool!!!' AS 'NOTE!';

SELECT @axiomid := id FROM axiom WHERE token = 'SubClassOf';

DELETE FROM subtype_closure WHERE axiom = @axiomid;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\SubClassOf_closure.csv'
    INTO TABLE subtype_closure
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n';

SELECT @axiomid := id FROM axiom WHERE token = 'SubPropertyOf';

DELETE FROM subtype_closure WHERE axiom = @axiomid;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\SubPropertyOf_closure.csv'
    INTO TABLE subtype_closure
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n';
