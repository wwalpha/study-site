package com.alpha.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

public class WordUtils {

	private static final Integer OFFSET = 7;
	private static final int[] INTERVAL = new int[] { 1, 1, 1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 30, 60, 90 };
	private static final Integer DAY_LIMIT = 150;
	private static WordUtils utils;

	private WordUtils() {
	}

	static {
		utils = new WordUtils();
	}

	public enum WordType {
		New, Review, Favorite,
	}

	private List<WordBean> getAllList() {
		return getAllList(null);
	}

	private List<WordBean> getAllList(String userName) {
		List<WordBean> retList = new ArrayList<>();
		List<String> allLines = getAllLines();

		int pos = 1;
		int index = 1;

		for (String line : allLines) {
			if (StringUtils.isEmpty(line)) {
				continue;
			}

			String[] datas = line.split("\\|");

			if (datas.length < 6) {
				continue;
			}

			// only read datas of same username
			if (StringUtils.isNotEmpty(userName) && !StringUtils.equals(userName, datas[0])) {
				continue;
			}

			WordBean bean = new WordBean();
			bean.setUserName(datas[0]);
			bean.setWord(datas[1]);
			bean.setPronounce(datas[2]);
			bean.setVocabulary(datas[3]);

			try {
				bean.setNextTime(Integer.parseInt(datas[4]));
				bean.setTimes(Integer.parseInt(datas[5]));
			} catch (NumberFormatException e) {
				// not use error date
				continue;
			}

			bean.setFavorite(Boolean.parseBoolean(datas[6]));
			bean.setIndex(index++);

			if (datas.length != 7) {
				bean.setSound(datas[7]);
			}

			int rate = bean.getRate();

			if (rate != 0) {
				bean.setStartPos(pos);
				bean.setEndPos(pos + rate - 1);
			} else {
				bean.setStartPos(0);
				bean.setEndPos(0);
			}

			pos += rate;

			retList.add(bean);
		}

		return retList;
	}

	/**
	 * 
	 * @return
	 */
	private List<String> getAllLines() {
		try {
			return FileUtils.readLines(getFile(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * get servlet context
	 * 
	 * @return
	 */
	private File getFile() {
		// ServletContext context = ((ServletRequestAttributes)
		// RequestContextHolder.getRequestAttributes()).getRequest()
		// .getServletContext();
		//
		// return new File(context.getRealPath("/WEB-INF/classes/wordDB.txt"));
		return new File("C:\\work\\wordDB.txt");
	}

	public static List<String> getUsers() {
		return utils.getAllList().stream().map(mapper -> mapper.getUserName()).distinct().collect(Collectors.toList());
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

		// new words
		if (StringUtils.equals("1", type)) {
			int maxInt = userList.size() > 49 ? 49 : userList.size();

			for (;;) {
				Random r = new Random();
				int nextInt = r.nextInt(maxInt - 1);

				if (!retList.contains(userList.get(nextInt))) {
					retList.add(userList.get(nextInt));
				}

				if (retList.size() == 7) {
					break;
				}
			}
		}

		// review words
		if (StringUtils.equals("2", type)) {
			while (retList.size() < OFFSET) {
				WordBean newWord = utils.getNextWord(userList);

				if (set.contains(newWord.getWord())) {
					continue;
				}

				set.add(newWord.getWord());

				retList.add(newWord);
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
		List<WordBean> allList = utils.getAllList();

		for (final UpdateBean bean : list) {
			// find the word in the file by same user
			WordBean[] result = allList.stream().filter(b -> StringUtils.equals(bean.getWord(), b.getWord())
					&& StringUtils.equals(userName, b.getUserName())).toArray(size -> new WordBean[size]);

			// if can not find user, skip
			if (result.length == 0) {
				continue;
			}

			WordBean target = result[0];

			// update times
			utils.updateTimes(target, bean);
			// update next time
			utils.updateNextTime(target, allList.stream());
		}

		// save data to file
		utils.saveFile(allList);
	}

	public static String download(String userName) {
		List<WordBean> userList = utils.getAllList(userName);

		List<String> allLines = new ArrayList<>();

		userList.stream().forEach(b -> allLines.add(b.toString()));

		return StringUtils.join(allLines, "◆");
	}

	public static String upload(String userName, String allText) {
		List<WordBean> userList = utils.getAllList(userName);

		List<String> allLines = new ArrayList<>();

		userList.stream().forEach(b -> allLines.add(b.toString()));

		return "{file: \"" + StringUtils.join(allLines, "\n") + "\"}";
	}

	/**
	 * get steam by type
	 * 
	 * @param userName
	 * @param type
	 * @return
	 */
	private List<WordBean> getUserList(String userName, String type) {
		List<WordBean> allList = utils.getAllList(userName);

		if (StringUtils.isEmpty(type)) {
			return allList;
		}

		Stream<WordBean> stream = null;

		switch (type) {
		case "1":
			// new
			long count = allList.stream().filter(p -> p.getTimes() == 0).count();

			// new words from review
			if (count == 0L) {
				stream = allList.stream().filter(p -> p.getTimes() == 9999);
				break;
			}

			// new words
			stream = getNewwords(allList);

			break;
		case "2":
			// review
			stream = allList.stream().filter(p -> p.getTimes() != 0);

			break;
		case "3":
			// favorite
			stream = allList.stream().filter(p -> p.isFavorite());

			break;
		default:
			stream = allList.stream().filter(p -> false);
			break;
		}

		return stream.collect(Collectors.toList());
	}

	private Stream<WordBean> getNewwords(List<WordBean> allList) {
		final int now = Integer.valueOf(DateTime.now().toString("yyyyMMdd"));

		List<WordBean> newList = allList.stream().filter(p -> p.getTimes() == 0 && p.getNextTime() <= now).collect(Collectors.toList());
				
		long count = newList.stream().map(m -> m.getNextTime()).distinct().count();

		// no news
		if (count == 0) {
			return new ArrayList<WordBean>().stream();
		}
		
		// one day
		if (count == 1) {
			return newList.stream();
		}

		int lastTime = newList.stream().map(m -> m.getNextTime()).distinct().max((a, b) -> a.compareTo(b)).get()
				.intValue();

		while (true) {
			final int nextTime = lastTime;
			count = newList.stream().filter(p -> p.getNextTime() >= nextTime).count();

			if (count < 49) {
				lastTime -= 1;

				continue;
			}

			List<WordBean> retList = newList.stream().filter(p -> p.getNextTime() >= nextTime)
					.sorted(Comparator.comparingInt(k -> k.getIndex())).collect(Collectors.toList());

			return retList.stream();
		}
	}

	/**
	 * Update the times column
	 * 
	 * @param target
	 * @param bean
	 */
	private void updateTimes(WordBean target, UpdateBean bean) {
		if (bean.isChecked()) {
			target.setTimes(0);

			return;
		}

		target.setTimes(target.getTimes() + 1);
	}

	/**
	 * Update the Next Time Column
	 * 
	 * @param target
	 * @param stream
	 */
	private void updateNextTime(WordBean target, Stream<WordBean> stream) {
		int times = target.getTimes();

		// new word don's have next time
		if (times == 0) {
			MutableDateTime now = MutableDateTime.now();
			now.addDays(1);
			
			target.setNextTime(Integer.parseInt(now.toString("yyyyMMdd")));
			return;
		}

		int interval = INTERVAL[times - 1];

		Calendar sysTime = Calendar.getInstance();
		// add interval days
		sysTime.add(Calendar.DAY_OF_MONTH, interval);

		DateTime dt = new DateTime(sysTime.getTime());
		int nextTime = Integer.parseInt(dt.toString("yyyyMMdd"));

		// if the times less than 3, set the next time
		if (target.getTimes() < 3) {
			target.setNextTime(nextTime);

			return;
		}

		while (true) {
			final int time = nextTime;

			// count the same day's words
			long count = stream.filter(w -> w.getNextTime() == time).count();

			// over the limit of days
			if (count == DAY_LIMIT) {
				nextTime++;

				continue;
			}

			target.setNextTime(nextTime);

			return;
		}
	}

	/**
	 * save the data to file
	 * 
	 * @param stream
	 */
	private void saveFile(List<WordBean> allList) {
		List<String> allLines = new ArrayList<>();

		allList.stream().forEach(b -> allLines.add(b.toString()));

		try {
			FileUtils.writeLines(getFile(), "UTF-8", allLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static void main(String[] args) {
		// List<WordBean> list = utils.getAllList();
		//
		// list.forEach(bean -> {
		// bean.setUserName("Alpha");
		// });
		//
		// utils.saveFile(list.stream());
	}
}