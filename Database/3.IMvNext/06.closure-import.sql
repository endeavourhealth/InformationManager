USE im_next2;

TRUNCATE TABLE concept_tct;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\sn116680003_closure.csv'
    INTO TABLE concept_tct
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    (source, property, target, level)
;
