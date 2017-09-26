SELECT
  NUM1
  , NUM2
  , NUM3
  , NUM4
  , NUM5
  , OPT1
  , OPT2
  , OPT3
  , OPT4
  , SUCCESS 
  , ANSWER
  , ANSWER_POS AS ANSWERPOS
FROM
  CALC_HISTORY 
WHERE
  REGIST_TIME = (SELECT MAX(REGIST_TIME) FROM CALC_HISTORY) 
ORDER BY
  HISTORYNO