package com.alpha.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.bean.PlayListBean;
import com.alpha.bean.UpdateBean;
import com.alpha.bean.WordBean;
import com.alpha.tools.DBUtils;

public class WordUtils {

	private static WordUtils utils;

	private WordUtils() {
	}

	static {
		utils = new WordUtils();
	}

	public enum WordType {
		New, Review, Favorite,
	}

	private List<WordBean> getAllList(String userName) {
		return getAllList(userName, null);
	}

	/**
	 * all files information of user
	 * 
	 * @param userName
	 * @return
	 */
	private List<WordBean> getAllList(String userName, String type) {
		List<WordBean> retList = getWordList(userName, type);

		if (StringUtils.isNotEmpty(type)) {
			int pos = 1;

			for (WordBean bean : retList) {
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
		}

		return retList;
	}

	/**
	 * word list by type
	 * 
	 * @param userName
	 * @param type
	 * @return
	 */
	private List<WordBean> getWordList(String userName, String type) {

		if (StringUtils.isEmpty(type)) {
			return DBUtils.select(WordBean.class, DBUtils.SELECT_ALL);
		}

		if (StringUtils.equals("1", type) || StringUtils.equals("4", type)) {
			return DBUtils.select(WordBean.class, DBUtils.SELECT_NEWWORD, userName);
		}

		if (StringUtils.equals("2", type)) {
			return DBUtils.select(WordBean.class, DBUtils.SELECT_REVIEW, userName);
		}

		if (StringUtils.equals("3", type)) {
			return DBUtils.select(WordBean.class, DBUtils.SELECT_ALL);
		}

		return new ArrayList<WordBean>();
	}

	/**
	 * 
	 * @param userName
	 * @return
	 */
	private List<WordBean> getTodayList(String userName) {
		return DBUtils.select(WordBean.class, DBUtils.SELECT_PLAYLIST, userName);
	}

	/**
	 * get all users
	 * 
	 * @return
	 */
	public static List<String> getUsers() {
		return DBUtils.select(String.class, DBUtils.SELECT_USERS);
	}

	/**
	 * 
	 * @return
	 */
	public static List<WordBean> getNextList(String userName, String type) {
		List<WordBean> retList = new ArrayList<>();
		Set<String> set = new HashSet<>();

		List<WordBean> userList = utils.getUserList(userName, type);

		// not found the user's data
		if (userList.size() == 0) {
			return retList;
		}

		Integer offset = 7;// Integer.valueOf(utils.getValue(userName,
							// PAGE_OFFSET));

		// new words
		if (Arrays.asList(new String[] { "1", "4" }).contains(type)) {
			int maxInt = userList.size();

			for (;;) {
				Random r = new Random();
				int nextInt = r.nextInt(maxInt - 1);

				if (!retList.contains(userList.get(nextInt))) {
					retList.add(userList.get(nextInt));
				}

				if (retList.size() == offset) {
					break;
				}
			}
		}

		// review words
		if (StringUtils.equals("2", type)) {
			while (retList.size() < offset) {
				WordBean newWord = utils.getNextWord(userList);

				if (set.contains(newWord.getWord())) {
					continue;
				}

				set.add(newWord.getWord());

				retList.add(newWord);
			}
		}

		if (StringUtils.equals("3", type)) {
			if (userList.size() > offset) {
				retList.addAll(userList.subList(0, offset - 1));
			} else {
				retList.addAll(userList);
			}
		}

		return retList;
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
			String strSQL = utils.getUpdateSQL(bean);
			List<Object> params = utils.getUpdateParams(userName, bean);

			// execute update
			DBUtils.update(strSQL, params.toArray());
		}
	}

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

	public static String download(String userName) {
		List<WordBean> userList = utils.getAllList(userName);

		List<String> allLines = new ArrayList<>();

		userList.stream().forEach(b -> allLines.add(b.toString()));

		return StringUtils.join(allLines, "◆");
	}

	public static boolean upload(String userName, MultipartFile file) {
		// String path = utils.getDBPath(userName);
		//
		// File dbFile = new File(path + file.getOriginalFilename());
		//
		// try {
		// file.transferTo(dbFile);
		// } catch (IllegalStateException | IOException e) {
		// }

		return true;
	}

	/**
	 * update user's setting file
	 * 
	 * @param user
	 * @param file
	 */
	// public static boolean updateSettings(MultipartFile file) {
	// if (!file.getOriginalFilename().endsWith(".properties")) {
	// return false;
	// }
	//
	// String path = utils.getContext().getRealPath(USER_PATH);
	//
	// File settingFile = new File(path + file.getOriginalFilename());
	//
	// try {
	// file.transferTo(settingFile);
	// } catch (IllegalStateException | IOException e) {
	// }
	//
	// int endIdx = file.getOriginalFilename().lastIndexOf(".properties");
	// String user = file.getOriginalFilename().substring(0, endIdx);
	//
	// // reinit user's informations
	// utils.initSettings(user);
	//
	// return true;
	// }

	/**
	 * Today's words for play sound
	 * 
	 * @param userName
	 * @return
	 */
	public static List<PlayListBean> getPlayList(String userName) {
		List<WordBean> allList = utils.getTodayList(userName);

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
	 * get steam by type
	 * 
	 * @param userName
	 * @param type
	 * @return
	 */
	private List<WordBean> getUserList(String userName, String type) {
		List<WordBean> allList = utils.getAllList(userName, type);

		if (StringUtils.isEmpty(type)) {
			return allList;
		}

		if (Arrays.asList(new String[] { "1", "4" }).contains(type)) {
			// new
			long count = allList.stream().filter(p -> p.getTimes() == 0).count();

			// new words from review
			if (count == 0L) {
				allList = allList.stream().filter(p -> p.getTimes() == 9999).collect(Collectors.toList());
			}
		}

		return allList;
	}

	/**
	 * Update the times column
	 * 
	 * @param target
	 * @param bean
	 */
	// private int updateTimes(WordBean target, UpdateBean bean) {
	// if (bean.isChecked()) {
	// return 0;
	// }
	//
	// return target.getTimes() + 1;
	// }

	/**
	 * Update the Next Time Column
	 * 
	 * @param target
	 * @param stream
	 */
	// private void updateNextTime(String userName, WordBean target,
	// List<WordBean> list) {
	// int times = target.getTimes();
	//
	// // new word don's have next time
	// if (times == 0) {
	// MutableDateTime now = MutableDateTime.now();
	// now.addDays(1);
	//
	// target.setNextTime(Integer.parseInt(now.toString("yyyyMMdd")));
	// return;
	// }
	//
	// int interval = INTERVAL[times - 1];
	//
	// Calendar sysTime = Calendar.getInstance();
	// // add interval days
	// sysTime.add(Calendar.DAY_OF_MONTH, interval);
	//
	// DateTime dt = new DateTime(sysTime.getTime());
	// int nextTime = Integer.parseInt(dt.toString("yyyyMMdd"));
	//
	// // if the times less than 3, set the next time
	// if (target.getTimes() < 3) {
	// target.setNextTime(nextTime);
	//
	// return;
	// }
	//
	// while (true) {
	// final int time = nextTime;
	//
	// // count the same day's words
	// long count = list.stream().filter(w -> w.getNextTime() == time).count();
	// Integer dayLimit = Integer.valueOf(utils.getValue(userName, DAY_LIMIT));
	//
	// // over the limit of days
	// if (count == dayLimit) {
	// nextTime++;
	//
	// continue;
	// }
	//
	// target.setNextTime(nextTime);
	//
	// return;
	// }
	// }

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

	/**
	 * get update sql
	 * 
	 * @param bean
	 * @return
	 */
	private String getUpdateSQL(UpdateBean bean) {
		StringBuffer sb = new StringBuffer();

		sb.append("UPDATE WORDS T1 SET ");

		// 回数リセット
		if (bean.isChecked()) {
			sb.append("T1.TIMES = 0 ");
			sb.append(",T1.NEXT_TIME = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y%m%d') ");
		} else {
			sb.append("T1.TIMES = T1.TIMES + 1 ");
			sb.append(",T1.STUDY_TIME = DATE_FORMAT(NOW(), '%Y%m%d') ");
			sb.append(
					",T1.NEXT_TIME = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL (SELECT DAY_DELAY FROM TIMES T2 WHERE T1.USER_ID = T2.USER_ID AND T2.TIMES = T1.TIMES) DAY), '%Y%m%d') ");
		}

		sb.append(",T1.FAVORITE = ? ");
		sb.append("WHERE T1.USER_ID = ? AND T1.WORD = ?");

		return sb.toString();
	}

	/**
	 * get update parameters
	 * 
	 * @param userName
	 * @param bean
	 * @return
	 */
	private List<Object> getUpdateParams(String userName, UpdateBean bean) {
		List<Object> retList = new ArrayList<Object>();

		// FAVORITE
		retList.add(BooleanUtils.toString(bean.isFavorite(), "1", "0"));
		// USER
		retList.add(userName);
		// WORD
		retList.add(bean.getWord());

		return retList;
	}
}