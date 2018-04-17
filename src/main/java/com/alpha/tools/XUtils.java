package com.alpha.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XUtils {

	/**
	 * delete random object from origin list
	 * 
	 * @param list
	 * @param max
	 * @return
	 */
	public static <T> List<T> getRandomList(List<T> list, int max) {
		List<T> retList = new ArrayList<>();

		if (max <= 0) {
			return retList;
		}

		int maxValue = list.size() > max ? max : list.size();

		while (retList.size() != maxValue) {
			T t = getRandom(list);

			retList.add(t);

			list.remove(t);
		}

		return retList;
	}

	private static <T> T getRandom(List<T> list) {
		if (list.size() == 1) {
			return list.get(0);
		}

		Random r = new Random();
		int nextInt = r.nextInt(list.size() - 1);

		return list.get(nextInt);
	}
}
