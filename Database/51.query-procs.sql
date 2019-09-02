DROP PROCEDURE IF EXISTS getConceptsByPropertyValue;
DELIMITER $$
CREATE PROCEDURE getConceptsByPropertyValue(IN expression JSON, OUT tbl VARCHAR(64)) BEGIN
	DECLARE exist VARCHAR(10);

	SET tbl := SHA1(expression);
    CALL sys.TABLE_EXISTS(database(), tbl, exist);
    IF exist = '' THEN
		SET @sql := CONCAT('CREATE TEMPORARY TABLE `', tbl, '` (dbid INT NOT NULL PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8');
		PREPARE stmt FROM @sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		
		SELECT dbid INTO @is_a FROM concept WHERE id = 'SN_116680003';
		SET @propsub := JSON_EXTRACT(expression, '$.subsumption');
		SET @prop := JSON_EXTRACT(expression, '$.property');
		SET @valsub := JSON_EXTRACT(expression, '$.concept_value.subsumption');
		SET @val := JSON_EXTRACT(expression, '$.concept_value.concept');
		
		SET @sql := CONCAT("INSERT INTO `",tbl,
		"` SELECT DISTINCT c.dbid FROM concept_property_object c",
		" JOIN concept_tct pt ON pt.source = c.property AND pt.property = ", @is_a,
		IF(@propsub = "true", "", " AND pt.level = -1"),
		" JOIN concept p ON p.dbid = pt.target AND p.id IN ", REPLACE(REPLACE(@prop, "[", "("), "]", ")"),  
		" JOIN concept_tct vt ON vt.source = c.value AND vt.property = ", @is_a,
		IF(@valsub="true", "", " AND vt.level = -1"),
		" JOIN concept v ON v.dbid = vt.target AND v.id IN ", REPLACE(REPLACE(@val, "[", "("), "]", ")"));
        
		PREPARE stmt FROM @sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
    END IF;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS getConceptsByPropertyValueGroup;
DELIMITER $$
CREATE PROCEDURE getConceptsByPropertyValueGroup(IN expression JSON, OUT tbl VARCHAR(64)) BEGIN
	DECLARE exist, op VARCHAR(10);
    DECLARE grpsql TEXT;
    DECLARE props, prop JSON;
    DECLARE i INT DEFAULT 0;

	SET tbl := SHA1(expression);
    CALL sys.TABLE_EXISTS(database(), tbl, exist);
    IF exist = '' THEN
		SET @sql := CONCAT('CREATE TEMPORARY TABLE `', tbl, '` (dbid INT NOT NULL PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8');
		PREPARE stmt FROM @sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
        
        SET props := JSON_EXTRACT(expression, '$.property');
        
        SET op := JSON_EXTRACT(expression, '$.operator');
        
        WHILE i < JSON_LENGTH(props) DO
			SELECT JSON_EXTRACT(props, CONCAT('$[',i,']')) INTO prop;
            CALL getConceptsByPropertyValue(prop, @proptbl);
            
            IF i = 0 THEN
				SET grpsql := CONCAT('INSERT INTO `', tbl, '` SELECT g0.dbid FROM `', @proptbl,'` g0');
            ELSE
				IF op = '"AND"' THEN
					SET grpsql := CONCAT(grpsql, ' JOIN `', @proptbl, '` g', i, ' ON g',i ,'.dbid = g0.dbid');
                ELSE
					SET grpsql := CONCAT(grpsql, ' UNION SELECT dbid FROM `', @proptbl, '`');
                END IF;
            END IF;
            
			SELECT i+1 INTO i;
        END WHILE;
        
        SET @sql = grpsql;
		PREPARE stmt FROM @sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		
    END IF;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS calcExpressionConstraint;

DELIMITER $$
CREATE PROCEDURE calcExpressionConstraint(IN expression JSON, OUT tbl VARCHAR(64)) BEGIN
	DECLARE exist VARCHAR(10);

	SET tbl := SHA1(expression);
    CALL sys.TABLE_EXISTS(database(), tbl, exist);
    IF exist = '' THEN
   		SET @sql := CONCAT('CREATE TEMPORARY TABLE `', tbl, '` (dbid INT NOT NULL PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8');
		PREPARE stmt FROM @sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
    
		SET @propcons := JSON_EXTRACT(expression, '$.property');
        IF @propcons IS NULL THEN
			SET @propcons := JSON_EXTRACT(expression, '$.property_group');
			CALL getConceptsByPropertyValueGroup(@propcons, @propconstbl);
        ELSE
			CALL getConceptsByPropertyValue(@propcons, @propconstbl);
        END IF;
        
   		SELECT dbid INTO @is_a FROM concept WHERE id = 'SN_116680003';
		SET @consub := JSON_EXTRACT(expression, '$.subsumption');
		SET @con := JSON_EXTRACT(expression, '$.concept');
        
        SET @sql := CONCAT("INSERT INTO `", tbl, "`"
            " SELECT c.*",
            " FROM `", @propconstbl, "` c"
            " JOIN concept_tct t ON t.source = c.dbid AND t.property = ", @is_a,
       		-- IF(@consub, " AND t.level <> -1", ""),
            " JOIN concept v ON v.dbid = t.target AND v.id IN ", REPLACE(REPLACE(@con, "[", "("), "]", ")")
        );
        
		PREPARE stmt FROM @sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	END IF;
END$$
DELIMITER ;


DROP TABLE IF EXISTS `0105311e009b4de07e7be8a124b07a275ccbb893`;
DROP TABLE IF EXISTS `16edcbf1d971cd8af192fc71478ec5bfb9136f8d`;
DROP TABLE IF EXISTS `29f20a0534c01a180446888097296bc137960405`;
DROP TABLE IF EXISTS `2cd50e100b1c014d3789ee39fc2bab0e3e32cbfe`;
DROP TABLE IF EXISTS `6572053159ac9eaace6dfb8ddd9fddad6d6ac304`;
DROP TABLE IF EXISTS `7ea53e850e3049d698e37b03792c333c594106ee`;
DROP TABLE IF EXISTS `6f8e7bebc9694338766a15701ac720c067a4b79d`;
DROP TABLE IF EXISTS `e82cbfb1d53d0b1bdd1dd95994b15e03f6720d72`;

/*
SET @expression := '
{
	"subsumption": true,
	"concept": ["SN_10363601000001109"],
	"property": {
		"subsumption": true,
		"property": ["SN_127489000", "SN_10362801000001104"],
		"concept_value": {
			"subsumption": true,
			"concept": ["SN_372813008"]
		}
	}
}';
*/

SET @expression := '
{
	 "subsumption": true,
	 "concept": [
		"SN_373873005"
	],
	 "property_group": {
		 "operator": "AND",
		 "property": [
			{
				 "subsumption": true,
				 "property": ["SN_127489000","SN_10362801000001104"],
				 "concept_value": {
					 "subsumption": true,
					 "concept": ["SN_767714007"]
				}
			},
			{
				 "subsumption": true,
				 "property": ["SN_10362901000001105","SN_411116001"],
				 "concept_value": {
					 "subsumption": true,
					 "concept": ["SN_385268001"]
				}
			}
		]
	}
}
';

CALL calcExpressionConstraint(@expression, @tbl);

SELECT @tbl;

-- SELECT * FROM `16edcbf1d971cd8af192fc71478ec5bfb9136f8d`;
-- SELECT * FROM `2cd50e100b1c014d3789ee39fc2bab0e3e32cbfe`;
SELECT * FROM `6572053159ac9eaace6dfb8ddd9fddad6d6ac304` r JOIN concept c ON c.dbid = r.dbid;
-- SELECT * FROM `29f20a0534c01a180446888097296bc137960405`;
SELECT * FROM `328c570377a366e3b553782f7980e0a259bbbb25` r JOIN concept c ON c.dbid = r.dbid;
