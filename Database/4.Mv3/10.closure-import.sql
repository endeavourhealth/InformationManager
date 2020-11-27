USE im3;

TRUNCATE TABLE concept_tct;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\closure.csv'
    INTO TABLE concept_tct
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    (source, target, level)
;
