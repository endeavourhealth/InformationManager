USE im6;

TRUNCATE TABLE tct;

SET GLOBAL local_infile = 1;

LOAD DATA LOCAL INFILE 'H:\\ImportData\\closure.txt'
    INTO TABLE tct
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n'
    (ancestor, descendant, level, type)
;
