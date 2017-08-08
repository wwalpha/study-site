DROP FUNCTION IF EXISTS nextTime;
CREATE FUNCTION nextTime(inUserId VARCHAR(50), inCategory VARCHAR(50), inWord VARCHAR(100), inTimes INT)
    RETURNS DATETIME
    LANGUAGE SQL
    DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    COMMENT ''
BEGIN

	SELECT DAY_LIMIT INTO @dayLimit FROM USERS WHERE USER_ID = inUserId;
	SELECT DAY_DELAY INTO @dayDelay FROM TIMES WHERE USER_ID = inUserId AND TIMES = inTimes;
   
	SET @dayCount = 0;

	NEXT: LOOP
		SELECT
       	    COUNT(*)
      	INTO
       	    @wordCount
       	FROM
       	    WORDS
       	WHERE
       	    USER_ID = inUserId
       	    AND (CATEGORY IS NULL OR CATEGORY = inCategory)
       	    AND WORD = inWord
		    AND NEXT_TIME = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL (@dayDelay + @dayCount) DAY), '%Y%m%d')
       	INTO 
       
       	IF @wordCount < @dayLimit THEN
			LEAVE NEXT;
       	END IF;
       
       	SET @dayCount = @dayCount + 1; 
   END LOOP;
   
   RETURN DATE_FORMAT(DATE_ADD(NOW(), INTERVAL(@dayDelay + @dayCount) DAY), '%Y%m%d');
END
