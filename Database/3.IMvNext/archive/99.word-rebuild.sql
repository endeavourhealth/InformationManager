SELECT 'Make sure you regenerate the CSV files using the word builder tool!!!' AS 'NOTE!';

-- Clean and populate word and concept_word tables
TRUNCATE TABLE concept_word;
TRUNCATE TABLE word;

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\words.csv'
    INTO TABLE word
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n';

LOAD DATA LOCAL INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\concept_words.csv'
    INTO TABLE concept_word
    FIELDS TERMINATED BY '\t'
    LINES TERMINATED BY '\r\n';

-- Add word maintenance triggers
CREATE TRIGGER concept_word_trigger_ins AFTER INSERT ON concept
    FOR EACH ROW
BEGIN
    SET @i = 1;
    SET @name = REPLACE(REPLACE(NEW.name, '(', ''), ')', '');   -- TODO: Use REGEXP_REPLACE in MySQL v8

    IF (NEW.code IS NOT NULL) THEN
        SET @name = CONCAT(@name, ' ', NEW.code);
    END IF;

    WHILE (LENGTH(@name) > 0 AND @i < 10) DO
            SET @word = SUBSTRING_INDEX(@name, ' ', 1);

            IF (LENGTH(@word) > 2 AND @word NOT IN ('and', 'for', 'the')) THEN
                INSERT INTO word (word) VALUE (@word) ON DUPLICATE KEY UPDATE useCount = useCount + 1;
                SELECT dbid INTO @wordId FROM word WHERE word = @word;
                INSERT INTO concept_word (word, position, concept) VALUES (@wordId, @i, NEW.id);
            END IF;

            SET @i = @i + 1;
            SET @name = SUBSTR(@name, LENGTH(@word) + 2);
        END WHILE;
END;

CREATE TRIGGER concept_word_trigger_upd AFTER UPDATE ON concept
    FOR EACH ROW
this_proc: BEGIN

    IF (NEW.name = OLD.name AND NEW.code = OLD.code) THEN
        LEAVE this_proc;
    END IF;

    UPDATE word
        INNER JOIN concept_word ON word.dbid = concept_word.word
    SET useCount = useCount - 1
    WHERE concept = OLD.id;

    DELETE FROM concept_word
    WHERE concept = OLD.id;

    SET @i = 1;
    SET @name = REPLACE(REPLACE(NEW.name, '(', ''), ')', '');   -- TODO: Use REGEXP_REPLACE in MySQL v8

    IF (NEW.code IS NOT NULL) THEN
        SET @name = CONCAT(@name, ' ', NEW.code);
    END IF;

    WHILE (LENGTH(@name) > 0 AND @i <10) DO
            SET @word = SUBSTRING_INDEX(@name, ' ', 1);
            IF (LENGTH(@word) > 2 AND @word NOT IN ('and', 'for', 'the')) THEN
                INSERT INTO word (word) VALUES (@word) ON DUPLICATE KEY UPDATE useCount = useCount + 1;
                SET @wordId = (SELECT dbid FROM word WHERE word = @word);

                INSERT INTO concept_word (word, position, concept) VALUES (@wordId, @i, OLD.id);
            END IF;

            SET @i = @i + 1;
            SET @name = SUBSTR(@name, LENGTH(@word) + 2);
        END WHILE;
END;
