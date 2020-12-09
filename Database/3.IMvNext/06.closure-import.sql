USE im_live;

TRUNCATE TABLE tct;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\closure_vs.csv'
    INTO TABLE tct
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    (source, target, level)
;
