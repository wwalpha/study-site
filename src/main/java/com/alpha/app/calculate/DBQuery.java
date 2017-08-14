package com.alpha.app.calculate;

import java.util.ArrayList;
import java.util.List;

import com.alpha.bean.CalculateBean;
import com.alpha.bean.ScoreBean;
import com.alpha.tools.DBUtils;

public class DBQuery {

	public List<CalculateBean> addSingle() {
		return DBUtils.select(CalculateBean.class, DBUtils.SELECT_ADD_SINGLE_ALL);
	}
	
	public List<ScoreBean> getScores() {
		return DBUtils.select(ScoreBean.class, DBUtils.SELECT_CALC_HISTORY);
	}

	public int updateResult(CalculateBean calcInfo) {
		// update times
		List<Object> params = new ArrayList<Object>();
		params.add(calcInfo.getLeftNum());
		params.add(calcInfo.getRightNum());
		params.add(calcInfo.getOperator());

		return DBUtils.update(DBUtils.UPDATE_CALC_TIMES, params.toArray());
	}

	public int updateHistory(CalculateBean calcInfo) {
		List<Object> params = new ArrayList<Object>();

		params.add(calcInfo.getResultNum());
		params.add(calcInfo.getResultNum());
		params.add(calcInfo.getLeftNum());
		params.add(calcInfo.getRightNum());
		params.add(calcInfo.getOperator());

		return DBUtils.update(DBUtils.INSERT_CALC_HISTORY, params.toArray());
	}
}
