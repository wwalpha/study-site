package com.alpha.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.alpha.bean.WordBean;

public class DBUtils {

	public static final String SELECT_ALL = "";
	public static final String SELECT_USERS = "SELECT USER_ID FROM USERS";

	public static final String SELECT_NEWWORD = "SELECT DISTINCT WORD, PRONOUNCE, VOCABULARY, NEXT_TIME AS NEXTTIME, STUDY_TIME AS STUDYTIME, TIMES, FAVORITE, SOUND FROM WORDS WHERE USER_ID = ? AND NEXT_TIME <= CAST(DATE_FORMAT(NOW(),'%Y%m%d') AS UNSIGNED) AND (TIMES = 0 OR TIMES = 9999) ORDER BY TIMES, NEXT_TIME DESC LIMIT 49";
	public static final String SELECT_REVIEW = "SELECT DISTINCT WORD, PRONOUNCE, VOCABULARY, NEXT_TIME AS NEXTTIME, STUDY_TIME AS STUDYTIME, TIMES, FAVORITE, SOUND FROM WORDS WHERE USER_ID = ? AND NEXT_TIME <= CAST(DATE_FORMAT(NOW(),'%Y%m%d') AS UNSIGNED) AND TIMES <> 0 ";
	public static final String UPDATE_WORDS = "UPDATE WORDS SET TIMES = TIMES + 1, STUDY_TIME = ?, NEXT_TIME = ?, FAVORITE = ? WHERE USER_ID = ? AND WORD = ?";

	public static final String SELECT_SETTINGS = "";
	public static final String UPDATE_SETTINGS = "";

	private DBUtils() {
	}

	private static final String USER_NAME = "wwalpha";
	private static final String PASSWORD = "session10";
	private static final String URL = "session10";

	/**
	 * SELECT EXECUTE
	 * 
	 * @param strSQL
	 * @param params
	 * @return
	 */
	public static <T> List<T> select(Class<T> clazz, String strSQL, Object... params) {
		DBQuery<T> query = new DBQuery<T>(clazz, URL, USER_NAME, PASSWORD);

		List<T> retList = query.select(strSQL, params);
		query.close();

		return retList;
	}

	public static <T> int update(String strSQL, Object... params) {
		DBQuery<T> query = new DBQuery<T>(URL, USER_NAME, PASSWORD);

		int result = query.update(strSQL, params);
		query.close();

		return result;
	}

	public static void main(String[] args) {
		File f = new File("");

		List<String> allLines = FileUtils.readLines(f, "UTF-8");
		List<WordBean> retList = new ArrayList<>();

		int index = 1;

		for (String line : allLines) {
			if (StringUtils.isEmpty(line)) {
				continue;
			}

			String[] datas = line.split("\\|");

			if (datas.length < 6) {
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

				if (datas.length >= 9) {
					bean.setStudyTime(Integer.parseInt(datas[8]));
				}
			} catch (NumberFormatException e) {
				// not use error date
				continue;
			}

			bean.setFavorite(Boolean.parseBoolean(datas[6]));
			bean.setIndex(index++);

			if (datas.length >= 8) {
				bean.setSound(datas[7]);
			}

			retList.add(bean);
		}

		
	}
}
