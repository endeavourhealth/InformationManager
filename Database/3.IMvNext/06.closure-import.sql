USE im_next;

TRUNCATE TABLE concept_tct;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\SN_116680003_closure.csv'
    INTO TABLE concept_tct
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    (source, property, target, level)
;
