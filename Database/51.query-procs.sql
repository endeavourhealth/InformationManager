DROP PROCEDURE IF EXISTS getConceptsByPropertyValueSql;
DELIMITER $$
CREATE PROCEDURE getConceptsByPropertyValueSql(IN expression JSON, OUT tbl TEXT) BEGIN
	SELECT 'Property value', expression;
	SELECT 'Expression_Value', JSON_EXTRACT(expression, '$.expression_value[0]');

	SELECT dbid INTO @is_a FROM concept WHERE id = 'SN_116680003';
	SET @propsub := JSON_EXTRACT(expression, '$.subsumption');
	SET @prop := JSON_EXTRACT(expression, '$.property');
	
	IF (JSON_EXTRACT(expression, '$.concept_value') IS NOT NULL) THEN
		SET @valsub := JSON_EXTRACT(expression, '$.concept_value.subsumption');
		SET @val := JSON_EXTRACT(expression, '$.concept_value.concept');
		
		SET tbl := CONCAT('SELECT c.dbid, c.group FROM concept_property c',
			' JOIN concept_tct pt ON pt.source = c.property AND pt.property = ', @is_a,
			IF(@propsub = 'true', '', ' AND pt.level = -1'),
			' JOIN concept p ON p.dbid = pt.target AND p.id IN ', REPLACE(REPLACE(@prop, '[', '('), ']', ')'),  
			' JOIN concept_tct vt ON vt.source = c.value AND vt.property = ', @is_a,
			IF(@valsub='true', '', ' AND vt.level = -1'),
			' JOIN concept v ON v.dbid = vt.target AND v.id IN ', REPLACE(REPLACE(@val, '[', '('), ']', ')')
		);
	ELSEIF (JSON_EXTRACT(expression, '$.expression_value[0]') IS NOT NULL) THEN
		SET @valsub := JSON_EXTRACT(expression, '$.expression_value[0].subsumption');
		SET @val := JSON_EXTRACT(expression, '$.expression_value[0].concept');
		SET @propcons := JSON_EXTRACT(expression, '$.expression_value[0].property.property');
		SET @valcons := JSON_EXTRACT(expression, '$.expression_value[0].property.value_concept');
		
		SET tbl := CONCAT('SELECT c.dbid, c.group FROM concept_property c',
			' JOIN concept_tct pt ON pt.source = c.property AND pt.property = ', @is_a,
			IF(@propsub = 'true', '', ' AND pt.level = -1'),
			' JOIN concept p ON p.dbid = pt.target AND p.id IN ', REPLACE(REPLACE(@prop, '[', '('), ']', ')'),  
			' JOIN concept_property ppo ON ppo.dbid = c.value',
			' JOIN concept_tct ppt ON ppt.source = ppo.property AND ppt.property = ', @is_a,
			' JOIN concept ppc ON ppc.dbid = ppt.target AND ppc.id IN ', REPLACE(REPLACE(@propcons, '[', '('), ']', ')'),
			' JOIN concept_tct pvt ON pvt.source = ppo.value AND pvt.property = ', @is_a,
			' JOIN concept pvc ON pvc.dbid = pvt.target and pvc.id IN ', REPLACE(REPLACE(@valcons, '[', '('), ']', ')')
			
			-- ' JOIN concept_tct vt ON vt.source = c.value AND vt.property = ', @is_a,
			-- IF(@valsub='true', '', ' AND vt.level = -1'),
			-- ' JOIN concept v ON v.dbid = vt.target AND v.id IN ', REPLACE(REPLACE(@val, '[', '('), ']', ')')
		);
	ELSE
		SELECT 'NO PROPERTY VALUE SPECIFIED!';
	END IF;
    SELECT tbl;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS getConceptsByPropertyValueGroupSql;
DELIMITER $$
CREATE PROCEDURE getConceptsByPropertyValueGroupSql(IN expression JSON, OUT grpsql TEXT) BEGIN
	DECLARE op VARCHAR(10);
    DECLARE props, prop JSON;
    DECLARE i INT DEFAULT 0;

	SELECT 'Property value group', expression;

	SET props := JSON_EXTRACT(expression, '$.property');
	SET op := JSON_EXTRACT(expression, '$.operator');
	
	WHILE i < JSON_LENGTH(props) DO
		SELECT JSON_EXTRACT(props, CONCAT('$[',i,']')) INTO prop;
		CALL getConceptsByPropertyValueSql(prop, @proptbl);
		
		IF i = 0 THEN
			SET grpsql := CONCAT('SELECT g0.dbid FROM (', @proptbl,') g0');
		ELSE
			IF op = ''AND'' THEN
				SET grpsql := CONCAT(grpsql, ' JOIN (', @proptbl, ') g', i, ' ON g',i ,'.dbid = g0.dbid');
			ELSE
				SET grpsql := CONCAT(grpsql, ' UNION SELECT g',i,'.dbid FROM (', @proptbl, ') g',i);
			END IF;
		END IF;
		
		SELECT i+1 INTO i;
	END WHILE;
    
    SELECT grpsql;
    
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS calcExpressionConstraint;
DELIMITER $$
CREATE PROCEDURE calcExpressionConstraint(IN expression JSON, OUT tbl VARCHAR(64)) BEGIN
	DECLARE exist VARCHAR(10);

	SET tbl := SHA1(expression);
    CALL sys.TABLE_EXISTS(database(), tbl, exist);
    IF exist = '' THEN
		IF (@debug) THEN SELECT tbl; END IF;
   		SET @sql := CONCAT('CREATE TEMPORARY TABLE `', tbl, '` (dbid INT NOT NULL PRIMARY KEY) ENGINE=InnoDB DEFAULT CHARSET=utf8');
		PREPARE stmt FROM @sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
    
		SET @propcons := JSON_EXTRACT(expression, '$.property');
        IF (@debug) THEN SELECT @propcons; END IF;
        IF (@propcons IS NULL) THEN
			SET @propcons := JSON_EXTRACT(expression, '$.property_group');
			CALL getConceptsByPropertyValueGroupSql(@propcons, @propconstbl);
        ELSE
			CALL getConceptsByPropertyValueSql(@propcons, @propconstbl);
        END IF;
        
   		SELECT dbid INTO @is_a FROM concept WHERE id = 'SN_116680003';
		SET @consub := JSON_EXTRACT(expression, '$.subsumption');
		SET @con := JSON_EXTRACT(expression, '$.concept');
        
        SET @sql := CONCAT('INSERT INTO `', tbl, '`'
            ' SELECT DISTINCT r.source',
            ' FROM (', @propconstbl, ') c'
            ' JOIN concept_tct t ON t.source = c.dbid AND t.property = ', @is_a,
       		IF(@consub = 'true', '', ' AND t.level <> -1'),
            ' JOIN concept v ON v.dbid = t.target AND v.id IN ', REPLACE(REPLACE(@con, '[', '('), ']', ')'),
            ' JOIN concept_tct r ON r.target = c.dbid AND r.property = ', @is_a
        );
        
        SELECT @sql;
        
		PREPARE stmt FROM @sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	END IF;
END$$
DELIMITER ;

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

DROP TABLE IF EXISTS `2cd50e100b1c014d3789ee39fc2bab0e3e32cbfe`;

CALL calcExpressionConstraint(@expression, @tbl);
SELECT @tbl;

SELECT * FROM `2cd50e100b1c014d3789ee39fc2bab0e3e32cbfe` r
JOIN concept c ON c.dbid = r.dbid;


SET @expression := '
{
	 "subsumption": true,
	 "concept": ["SN_373873005"],
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

DROP TABLE IF EXISTS `328c570377a366e3b553782f7980e0a259bbbb25`;

CALL calcExpressionConstraint(@expression, @tbl);
SELECT @tbl;

SELECT * FROM `328c570377a366e3b553782f7980e0a259bbbb25` r
JOIN concept c ON c.dbid = r.dbid;



-- SELECT << SN_71388002 | Procedure
-- WHERE << SN_405813007 | Procedure Site = ( SELECT << 280553001 | Abdominal wall WHERE << SN_272741003 | Laterality = << SN_7771000 | LEFT)
-- AND << SN_363700003 | Direct morphology = << SN_414403008 | Herniated structure


SET @expression := '
{
     "subsumption": true,
     "concept": ["SN_177853001"],
     "property" : {
         "subsumption": true,
         "property": ["SN_405813007"],
         "expression_value": [
            {
                 "subsumption": true,
                 "concept": ["SN_280553001"],
                 "property": {
                     "property": ["SN_272741003"],
                     "value_concept": ["SN_7771000"]
                }
            }
        ]
    }
}
';

DROP TABLE IF EXISTS `61d88bfcaa9d8efff6e6d1c37b64926299fe9a04`;

CALL calcExpressionConstraint(@expression, @tbl);
SELECT @tbl;

SELECT * FROM `61d88bfcaa9d8efff6e6d1c37b64926299fe9a04` r
JOIN concept c ON c.dbid = r.dbid;
*/
