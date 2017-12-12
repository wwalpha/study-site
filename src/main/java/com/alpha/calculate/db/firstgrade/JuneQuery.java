package com.alpha.calculate.db.firstgrade;

import java.util.List;

import com.alpha.calculate.bean.CalculateBean;
import com.alpha.tools.DBUtils;
import com.alpha.tools.XFileUtils;

public class JuneQuery implements FirstGrade {

	/**
	 * 増えるといつくの数字を取得する
	 * @return
	 */
	public List<CalculateBean> getNum01() {
		return DBUtils.select(CalculateBean.class, XFileUtils.getSQL(PREFIX + "SELECT_JUNE01_NUM"));
	}

	/**
	 * 増えるといつくの文言を取得する
	 * @return
	 */
	public List<CalculateBean> getText01() {
		return DBUtils.select(CalculateBean.class, XFileUtils.getSQL(PREFIX + "SELECT_JUNE01_TEXT"));
	}
	
}
