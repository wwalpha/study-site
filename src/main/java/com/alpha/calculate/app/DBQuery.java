package com.alpha.calculate.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alpha.calculate.bean.CalculateBean;
import com.alpha.calculate.bean.ScoreBean;
import com.alpha.tools.Constants.CalcOptions;

import com.alpha.tools.DBUtils;
import com.alpha.tools.XFileUtils;

public class DBQuery {

	public static final String SELECT_ADD_SINGLE_ALL = "SELECT NUM1, NUM2, NUM3, NUM4, NUM5, TIMES, OPT1, OPT2, OPT3, OPT4 FROM CALCULATE WHERE OPT1 = '+' AND OPT2 = '=' AND NUM1 < 10 AND NUM2 < 10 ";
	public static final String SELECT_MINUS_SINGLE_ALL = "SELECT NUM1, NUM2, NUM3, NUM4, NUM5, TIMES, OPT1, OPT2, OPT3, OPT4 FROM CALCULATE WHERE OPT1 = '-' AND OPT2 = '=' AND NUM1 < 10 AND NUM2 < 10 ";

	public static final String UPDATE_CALC_TIMES = "UPDATE CALCULATE SET TIMES = TIMES + 1 WHERE NUM1 = ? AND NUM2 = ? AND OPT1 = ? ";

	public static final String SELECT_NEXT = "SELECT NUM1, NUM2, NUM3, NUM4, NUM5, TIMES, OPT1, OPT2, OPT3, OPT4 FROM CALCULATE ";

	public List<CalculateBean> next(String options) {
		return DBUtils.select(CalculateBean.class, SELECT_NEXT + createWhere(options));
	}

	private String createWhere(String options) {
		Map<String, List<String>> conditions = new HashMap<>();
		
		List<String> whereSQL = new ArrayList<>();
		whereSQL.add("OPT2 = '=' ");
		
		conditions.put("OPT", new ArrayList<>());
		conditions.put("OPT2", new ArrayList<>());
		conditions.put("OPT3", new ArrayList<>());
		conditions.put("OPT4", new ArrayList<>());
		conditions.put("NUM1", new ArrayList<>());
		conditions.put("NUM2", new ArrayList<>());

		Arrays.stream(options.split(",")).forEach(row -> {
			if (StringUtils.equals(row, CalcOptions.ADD)) {
				conditions.get("OPT").add("'+'");
			}
			if (StringUtils.equals(row, CalcOptions.MINUS)) {
				conditions.get("OPT").add("'-'");
			}
			if (StringUtils.equals(row, CalcOptions.MULTI)) {
				conditions.get("OPT").add("'*'");
			}
			if (StringUtils.equals(row, CalcOptions.DIVISION)) {
				conditions.get("OPT").add("'/'");
			}
			if (StringUtils.equals(row, CalcOptions.BUG)) {
				whereSQL.add("NUM3 < 10");
			}
		});

		if (conditions.get("OPT").size() != 0) {
			whereSQL.add("OPT1 IN (" + StringUtils.join(conditions.get("OPT"), ",") + ") ");
		}

		whereSQL.add("NUM1 < 10");
		whereSQL.add("NUM2 < 10");

		return "WHERE " + StringUtils.join(whereSQL, " AND ");
	}

	public List<CalculateBean> addSingle() {
		return DBUtils.select(CalculateBean.class, SELECT_ADD_SINGLE_ALL);
	}

	public List<CalculateBean> minusSingle() {
		return DBUtils.select(CalculateBean.class, SELECT_MINUS_SINGLE_ALL);
	}

	public List<ScoreBean> getScores() {
		return DBUtils.select(ScoreBean.class, XFileUtils.getSQL("SELECT_CALC_HISTORY"));
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
		String strSQL = XFileUtils.getSQL("INSERT_CALC_HISTORY");
		List<String> whereSQL = new ArrayList<>();

		if (calcInfo.getNum1() != null) {
			whereSQL.add("NUM1 = " + String.valueOf(calcInfo.getNum1()));
		}
		if (calcInfo.getNum2() != null) {
			whereSQL.add("NUM2 = " + String.valueOf(calcInfo.getNum2()));
		}
		if (calcInfo.getNum3() != null) {
			whereSQL.add("NUM3 = " + String.valueOf(calcInfo.getNum3()));
		}
		if (calcInfo.getNum4() != null) {
			whereSQL.add("NUM4 = " + String.valueOf(calcInfo.getNum4()));
		}
		if (calcInfo.getNum5() != null) {
			whereSQL.add("NUM5 = " + String.valueOf(calcInfo.getNum5()));
		}
		if (StringUtils.isNotEmpty(calcInfo.getOpt1())) {
			whereSQL.add("OPT1 = '" + calcInfo.getOpt1() + "'");
		}
		if (StringUtils.isNotEmpty(calcInfo.getOpt2())) {
			whereSQL.add("OPT2 = '" + calcInfo.getOpt2() + "'");
		}
		if (StringUtils.isNotEmpty(calcInfo.getOpt3())) {
			whereSQL.add("OPT3 = '" + calcInfo.getOpt3() + "'");
		}
		if (StringUtils.isNotEmpty(calcInfo.getOpt4())) {
			whereSQL.add("OPT4 = '" + calcInfo.getOpt4() + "'");
		}

		String newSQL = strSQL + " WHERE " + StringUtils.join(whereSQL, " AND ");

		List<Object> params = new ArrayList<Object>();
		params.add(calcInfo.getAnswer());
		params.add(calcInfo.getAnswerPos());
		params.add(calcInfo.getAnswerPos());
		params.add(calcInfo.getAnswer());
		params.add(calcInfo.getAnswerPos());
		params.add(calcInfo.getAnswer());
		params.add(calcInfo.getAnswerPos());
		params.add(calcInfo.getAnswer());
		params.add(calcInfo.getAnswerPos());
		params.add(calcInfo.getAnswer());
		params.add(calcInfo.getAnswerPos());
		params.add(calcInfo.getAnswer());
		params.add(calcInfo.getStartTime());

		int count = DBUtils.update(newSQL, params.toArray());
		
		return count;
	}
}
