package com.alpha.calculate.app;

import java.util.List;
import java.util.Random;

import com.alpha.calculate.bean.CalculateBean;

public abstract class Grade {

	public abstract CalculateBean next(String type);
	
	protected <T> T getRandom(List<T> list) {
		if (list.size() == 1) {
			return list.get(0);
		}

		Random r = new Random();
		int nextInt = r.nextInt(list.size() - 1);

		return list.get(nextInt);
	}
}
