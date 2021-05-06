USE im6;

TRUNCATE TABLE tct;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\closure.txt'
    INTO TABLE tct
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    (ancestor, descendant, level, type)
;
