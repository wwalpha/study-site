package com.alpha.app.calculate;

import java.util.ArrayList;
import java.util.List;

import com.alpha.bean.CalculateBean;
import com.alpha.bean.ScoreBean;
import com.alpha.tools.DBUtils;

public class DBQuery {

	public static final String SELECT_CALC_HISTORY = "SELECT LEFT_NUM, RIGHT_NUM, OPERATOR, RESULT_NUM, ANSWER, SUCCESS FROM CALC_HISTORY WHERE REGIST_TIME = (SELECT MAX(REGIST_TIME) FROM CALC_HISTORY) ORDER BY HISTORYNO";
	
	public static final String SELECT_ADD_SINGLE_ALL = "SELECT LEFT_NUM AS LEFTNUM, RIGHT_NUM AS RIGHTNUM, TIMES, OPERATOR FROM CALCULATE WHERE OPERATOR = '+' AND LEFT_NUM < 10 AND RIGHT_NUM < 10 ";
	public static final String SELECT_MINUS_SINGLE_ALL = "SELECT LEFT_NUM AS LEFTNUM, RIGHT_NUM AS RIGHTNUM, TIMES, OPERATOR FROM CALCULATE WHERE OPERATOR = '-' AND LEFT_NUM < 10 AND RIGHT_NUM < 10 ";

	public static final String UPDATE_CALC_TIMES = "UPDATE CALCULATE SET TIMES = TIMES + 1 WHERE LEFT_NUM = ? AND RIGHT_NUM = ? AND OPERATOR = ? ";
	public static final String INSERT_CALC_HISTORY = "INSERT INTO CALC_HISTORY(HISTORYNO, LEFT_NUM, RIGHT_NUM, OPERATOR, RESULT_NUM, ANSWER, SUCCESS, TIMES, REGIST_TIME) SELECT NEXTVAL('CALCSEQ'), LEFT_NUM, RIGHT_NUM, OPERATOR, RESULT_NUM, ?, CASE WHEN ? = RESULT_NUM THEN '1' ELSE '0' END, TIMES, ? FROM CALCULATE WHERE LEFT_NUM = ? AND RIGHT_NUM = ? AND OPERATOR = ? ";

	public List<CalculateBean> addSingle() {
		return DBUtils.select(CalculateBean.class, SELECT_ADD_SINGLE_ALL);
	}
	
	public List<CalculateBean> minusSingle() {
		return DBUtils.select(CalculateBean.class, SELECT_MINUS_SINGLE_ALL);
	}
	
	public List<ScoreBean> getScores() {
		return DBUtils.select(ScoreBean.class, SELECT_CALC_HISTORY);
	}

	public int updateResult(CalculateBean calcInfo) {
		// update times
		List<Object> params = new ArrayList<Object>();
		params.add(calcInfo.getLeftNum());
		params.add(calcInfo.getRightNum());
		params.add(calcInfo.getOperator());

		return DBUtils.update(UPDATE_CALC_TIMES, params.toArray());
	}

	public int updateHistory(CalculateBean calcInfo) {
		List<Object> params = new ArrayList<Object>();

		params.add(calcInfo.getResultNum());
		params.add(calcInfo.getResultNum());
		params.add(calcInfo.getStartTime());
		params.add(calcInfo.getLeftNum());
		params.add(calcInfo.getRightNum());
		params.add(calcInfo.getOperator());

		return DBUtils.update(INSERT_CALC_HISTORY, params.toArray());
	}
}
