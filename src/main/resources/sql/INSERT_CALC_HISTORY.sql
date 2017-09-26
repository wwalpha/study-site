INSERT 
INTO CALC_HISTORY( 
  HISTORYNO
  , NUM1
  , NUM2
  , NUM3
  , NUM4
  , NUM5
  , OPT1
  , OPT2
  , OPT3
  , OPT4
  , ANSWER
  , ANSWER_POS
  , SUCCESS
  , TIMES
  , REGIST_TIME
) 
SELECT
  NEXTVAL('CALCSEQ')
  , NUM1
  , NUM2
  , NUM3
  , NUM4
  , NUM5
  , OPT1
  , OPT2
  , OPT3
  , OPT4
  , ? 
  , ? 
  , CASE 
      WHEN ? = 0 THEN CASE WHEN NUM1 = ? THEN '1' ELSE '0' END
      WHEN ? = 1 THEN CASE WHEN NUM2 = ? THEN '1' ELSE '0' END
      WHEN ? = 2 THEN CASE WHEN NUM3 = ? THEN '1' ELSE '0' END
      WHEN ? = 3 THEN CASE WHEN NUM4 = ? THEN '1' ELSE '0' END
      WHEN ? = 4 THEN CASE WHEN NUM5 = ? THEN '1' ELSE '0' END
    END AS SUCCESS
  , TIMES
  , ? 
FROM
  CALCULATE
