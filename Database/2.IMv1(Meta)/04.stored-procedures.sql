DELIMITER $$
DROP PROCEDURE IF EXISTS proc_build_tct;
CREATE PROCEDURE proc_build_tct(property_iri VARCHAR(150))
BEGIN
    SET @lvl := 0;

    SELECT dbid INTO @property_id
    FROM concept
    WHERE id = property_iri;

    DELETE FROM concept_tct
    WHERE property = @property_id;

    -- Insert all concepts that have parents
    INSERT INTO concept_tct
    (source, property, target, level)
    SELECT o.dbid, @property_id, o.value, 0
    FROM concept_property_object o
    WHERE o.property = @property_id;

    SELECT ROW_COUNT() INTO @inserted;

    WHILE @inserted > 0 DO
            SELECT CONCAT('Level ', @lvl, ' - Inserted ', @inserted);

            SET @lvl = @lvl + 1;

            -- Insert parents of last tct entries
            REPLACE INTO concept_tct
            (source, property, target, level)
            SELECT t.source, @property_id, o.value, @lvl
            FROM concept_tct t
                     JOIN concept_property_object o ON o.dbid = t.target AND o.property = @property_id
            WHERE t.property = @property_id
              AND t.level = @lvl - 1;

            SELECT ROW_COUNT() INTO @inserted;
        END WHILE;
END$$
DELIMITER ;

-- CALL proc_build_tct('SN_116680003');
