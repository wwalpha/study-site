package com.alpha.calculate.app;

import java.util.ArrayList;
import java.util.List;

import com.alpha.calculate.bean.CalculateBean;
import com.alpha.calculate.bean.ScoreBean;
import com.alpha.tools.DBUtils;

public class DBQuery {

	public static final String SELECT_CALC_HISTORY = "SELECT LEFT_NUM, RIGHT_NUM, OPERATOR, RESULT_NUM, ANSWER, SUCCESS FROM CALC_HISTORY WHERE REGIST_TIME = (SELECT MAX(REGIST_TIME) FROM CALC_HISTORY) ORDER BY HISTORYNO";
	
	public static final String SELECT_ADD_SINGLE_ALL = "SELECT NUM1, NUM2, NUM3, NUM4, NUM5, TIMES, OPT1, OPT2, OPT3, OPT4 FROM CALCULATE WHERE OPT1 = '+' AND OPT2 = '=' AND NUM1 < 10 AND NUM2 < 10 ";
	public static final String SELECT_MINUS_SINGLE_ALL = "SELECT NUM1, NUM2, NUM3, NUM4, NUM5, TIMES, OPT1, OPT2, OPT3, OPT4 FROM CALCULATE WHERE OPT1 = '-' AND OPT2 = '=' AND NUM1 < 10 AND NUM2 < 10 ";

	public static final String UPDATE_CALC_TIMES = "UPDATE CALCULATE SET TIMES = TIMES + 1 WHERE NUM1 = ? AND NUM2 = ? AND OPT1 = ? ";
	public static final String INSERT_CALC_HISTORY = "INSERT INTO CALC_HISTORY(HISTORYNO, NUM1, NUM2, NUM3, NUM4, NUM5, OPT1, OPT2, OPT3, OPT4, ANSWER, SUCCESS, TIMES, REGIST_TIME) SELECT NEXTVAL('CALCSEQ'), NUM1, NUM2, NUM3, NUM4, NUM5, OPT1, OPT2, OPT3, OPT4, ?, CASE WHEN ? = NUM3 THEN '1' ELSE '0' END, TIMES, ? FROM CALCULATE WHERE NUM1 = ? AND NUM2 = ? AND OPT1 = ? ";

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
		params.add(calcInfo.getNum1());
		params.add(calcInfo.getNum2());
		params.add(calcInfo.getOpt1());

		return DBUtils.update(UPDATE_CALC_TIMES, params.toArray());
	}

	public int updateHistory(CalculateBean calcInfo) {
		List<Object> params = new ArrayList<Object>();

		params.add(calcInfo.getNum3());
		params.add(calcInfo.getNum3());
		params.add(calcInfo.getStartTime());
		params.add(calcInfo.getNum1());
		params.add(calcInfo.getNum2());
		params.add(calcInfo.getOpt1());

		return DBUtils.update(INSERT_CALC_HISTORY, params.toArray());
	}
}
