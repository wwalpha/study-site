package com.alpha.words.app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.alpha.tools.Constants.WordType;
import com.alpha.words.bean.PlayListBean;
import com.alpha.words.bean.StatisticBean;
import com.alpha.words.bean.UpdateBean;
import com.alpha.words.bean.UserBean;
import com.alpha.words.bean.WordBean;

public class Utils {

	private static Utils utils;
	private static DBQuery query;
	private static Map<String, List<WordBean>> wordMap;
	private static Map<String, UserBean> userMap;

	private Utils() {
	}

	static {
		utils = new Utils();
		query = new DBQuery();
		wordMap = new HashMap<>();
		userMap = new HashMap<>();
	}

	/**
	 * get all users
	 * 
	 * @return
	 */
	public static List<String> getUsers() {
		List<String> users = query.getUsers();

		for (String user : users) {
			if (wordMap.containsKey(user)) {
				continue;
			}

			wordMap.put(user, new ArrayList<>());
		}

		return users;
	}

	/**
	 * get all users
	 * 
	 * @return
	 */
	public static UserBean getUserProps(String userName) {
		UserBean userBean = query.getUserProps(userName);
		List<String> ctgList = query.getCategories(userName);

		if (ctgList.size() != 0) {
			// category names
			userBean.setCtgNames(ctgList);
		} else {
			userBean.setCtgNames(new ArrayList<>());
		}

		// clear cache
		wordMap.get(userName).clear();
		userMap.put(userName, userBean);

		return userBean;
	}

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public static List<StatisticBean> getStatistic(String userName) {
		return query.getStatistic(userName);
	}

	/**
	 * 
	 * @return
	 */
	public static List<WordBean> getNextList(String userName, String type, String categories) {
		UserBean userBean = userMap.get(userName);

		if (StringUtils.equals(userBean.getCategories(), categories) || StringUtils.equals(userBean.getType(), type)) {
			userBean.setCategories(categories);
			userBean.setType(type);

			wordMap.get(userName).clear();
		}

		// New words
		if (StringUtils.equals(WordType.New, type) || StringUtils.equals(WordType.NewSingle, type)) {
			return utils.pattern1(userName, categories);
		}

		// Review words
		if (StringUtils.equals(WordType.Review, type)) {
			return utils.pattern2(userName, categories);
		}

		// Favorite words
		if (StringUtils.equals(WordType.Favorite, type)) {
			return utils.pattern3(userName, categories);
		}

		return new ArrayList<>();
	}

	/**
	 * Get New Words
	 * 
	 * @param userName
	 * @param pattern
	 * @param categories
	 * @return
	 */
	private List<WordBean> pattern1(String userName, String categories) {
		List<WordBean> cacheList = wordMap.get(userName);

		// Has cache
		if (cacheList.size() != 0) {
			return getRandomList(cacheList, getPageOffset(userName));
		}

		// select new words
		List<WordBean> wordsList = query.getNewWords(userName, categories);

		// new
		long count = wordsList.stream().filter(p -> p.getTimes() == 0).count();

		// new words from review
		if (count == 0L) {
			wordsList = wordsList.stream().filter(p -> p.getTimes() == 9999).collect(Collectors.toList());
		}

		// cache
		wordMap.put(userName, wordsList);

		return getRandomList(wordsList, 7);
	}

	/**
	 * Get Review Words
	 * 
	 * @param userName
	 * @param pattern
	 * @param categories
	 * @return
	 */
	private List<WordBean> pattern2(String userName, String categories) {
		int count = query.cntReviews(userName);

		// 当日制限超えた
		if (count > getDayLimit(userName)) {
			return new ArrayList<>();
		}

		List<WordBean> wordsList = query.getReviewWords(userName, categories);

		int offset = getPageOffset(userName);

		// under one page size
		if (wordsList.size() < offset) {
			return wordsList;
		}

		int pos = 1;

		for (WordBean bean : wordsList) {
			int rate = bean.getRate();

			if (rate != 0) {
				bean.setStartPos(pos);
				bean.setEndPos(pos + rate - 1);
			} else {
				bean.setStartPos(0);
				bean.setEndPos(0);
			}

			pos += rate;
		}

		List<WordBean> retList = new ArrayList<>();
		Set<String> set = new HashSet<>();

		// find a word
		while (retList.size() != offset) {
			WordBean newWord = utils.getNextWord(wordsList);

			if (set.contains(newWord.getWord())) {
				continue;
			}

			set.add(newWord.getWord());

			retList.add(newWord);
		}

		return retList;
	}

	/**
	 * Get Favorite words
	 * 
	 * @param userName
	 * @param categories
	 * @return
	 */
	private List<WordBean> pattern3(String userName, String categories) {
		List<WordBean> wordsList = query.getFavoriteWords(userName, categories);

		return getRandomList(wordsList, getPageOffset(userName));
	}

	/**
	 * Update the study status
	 * 
	 * @param userName
	 * @param list
	 */
	public static void save(String userName, List<UpdateBean> list) {
		List<UpdateBean> newList = utils.distinct(list);

		for (final UpdateBean bean : newList) {
			query.updateWord(userName, bean);
			
			query.insertHistory(userName, bean);
		}
	}

	/**
	 * delete replicate words
	 * 
	 * @param updateList
	 * @return
	 */
	private List<UpdateBean> distinct(List<UpdateBean> updateList) {
		Map<String, UpdateBean> map = new HashMap<>();

		for (UpdateBean bean : updateList) {
			if (map.containsKey(bean.getWord())) {
				UpdateBean updateBean = map.get(bean.getWord());

				updateBean.setChecked(updateBean.isChecked() ? true : bean.isChecked());
				updateBean.setFavorite(updateBean.isFavorite() ? true : bean.isFavorite());

				continue;
			}

			map.put(bean.getWord(), bean);
		}

		return new ArrayList<>(map.values());
	}

	/**
	 * Today's words for play sound
	 * 
	 * @param userName
	 * @return
	 */
	public static List<PlayListBean> getPlayList(String userName) {
		List<WordBean> allList = query.getTodayList(userName);

		if (allList.size() == 0) {
			return new ArrayList<PlayListBean>();
		}

		return allList.stream().map(m -> {
			PlayListBean bean = new PlayListBean();
			bean.setSource(m.getSound());
			bean.setWord(m.getWord());

			return bean;
		}).collect(Collectors.toList());
	}

	/**
	 * get next word of type 2
	 * 
	 * @param allList
	 * @return
	 */
	private WordBean getNextWord(List<WordBean> userList) {
		// get min end pos of same user
		int minPos = 1;
		// get min end pos of same user
		int maxPos = userList.stream().max(Comparator.comparingInt(s -> s.getEndPos())).get().getEndPos();

		while (true) {
			final int nextInt = new Random().nextInt(maxPos - minPos) + minPos;

			List<WordBean> resultList = userList.stream()
					.filter(bean -> bean.getStartPos() <= nextInt && nextInt <= bean.getEndPos())
					.collect(Collectors.toList());

			if (resultList.size() == 0) {
				continue;
			}

			return resultList.get(0);
		}
	}

	private static <T> T getRandom(List<T> list) {
		if (list.size() == 1) {
			return list.get(0);
		}

		Random r = new Random();
		int nextInt = r.nextInt(list.size() - 1);

		return list.get(nextInt);
	}

	/**
	 * delete random object from origin list
	 * 
	 * @param list
	 * @param max
	 * @return
	 */
	private static <T> List<T> getRandomList(List<T> list, int max) {
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

	/**
	 * Page Offset Number
	 * 
	 * @param userName
	 * @return
	 */
	private int getPageOffset(String userName) {
		UserBean userInfo = userMap.get(userName);

		if (userInfo == null) {
			return 7;
		}

		return userInfo.getPageOffset();
	}

	/**
	 * Page Offset Number
	 * 
	 * @param userName
	 * @return
	 */
	private int getDayLimit(String userName) {
		UserBean userInfo = userMap.get(userName);

		if (userInfo == null) {
			return 200;
		}

		return userInfo.getDayLimit();
	}
}