package com.alpha.calculate.app.firstgrade;

import com.alpha.calculate.app.Grade;
import com.alpha.calculate.bean.CalculateBean;
import com.alpha.calculate.db.firstgrade.JuneQuery;

public class FirstGradeService extends Grade {

	@Override
	public CalculateBean next(String type) {
		switch(Integer.parseInt(type)) {
			case 601:
				return getJune01();
		}
		
		return null;
	}


	private CalculateBean getJune01() {
		JuneQuery query = new JuneQuery();
		
		CalculateBean numBean = getRandom(query.getNum01());
		CalculateBean textBean = getRandom(query.getText01());

		String question = String.format(textBean.getQuestion(), numBean.getNum1(), numBean.getNum2());
		numBean.setQuestion(question);
		
		return numBean;
	}
}
