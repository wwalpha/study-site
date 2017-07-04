package com.alpha.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
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

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
	}

	private DBUtils() {
	}

	private static final String USER_NAME = "root";
	private static final String PASSWORD = "";
	private static final String URL = "jdbc:mysql://localhost:3306/alpha?useUnicode=true&characterEncoding=utf8";

	/**
	 * SELECT EXECUTE
	 * 
	 * @param strSQL
	 * @param params
	 * @return
	 */
	public static <T> List<T> select(Class<T> clazz, String strSQL, Object... params) {
		DBQuery<T> query = new DBQuery<T>(clazz, URL, USER_NAME, PASSWORD);

		return query.select(strSQL, params);
	}

	@SuppressWarnings("rawtypes")
	public static int update(String strSQL, Object... params) {
		DBQuery query = new DBQuery(URL, USER_NAME, PASSWORD);

		try {
			return query.update(strSQL, params);
		} catch (Exception e) {
		} finally {
			query.close();
		}

		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int[] execBatch(List<String> sqlList) {
		DBQuery query = new DBQuery(URL, USER_NAME, PASSWORD);

		try {
			return query.execBatch(sqlList);
		} catch (Exception e) {
		} finally {
			query.close();
		}

		return new int[] {};
	}

	public static void main(String[] args) throws Exception {
		DBQuery query = new DBQuery(String.class, URL, USER_NAME, PASSWORD);

		query.testConnection();

		File f = new File("c:/work/wordDB.txt");

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

		List<String> sqlList = new ArrayList<String>();

		for (WordBean bean : retList) {
			StringBuffer sb = new StringBuffer();

			sb.append("INSERT WORDS (");
			sb.append("  USER_ID");
			sb.append("  ,WORD_NO");
			sb.append("  ,WORD");
			sb.append("  ,PRONOUNCE");
			sb.append("  ,VOCABULARY");
			sb.append("  ,NEXT_TIME");
			sb.append("  ,STUDY_TIME");
			sb.append("  ,TIMES");
			sb.append("  ,FAVORITE");
			sb.append("  ,SOUND");
			sb.append(") VALUES (");
			sb.append("  '").append(bean.getUserName()).append("' ");
			sb.append("  ,NEXTVAL('WORDSEQ') ");
			sb.append("  ,'").append(bean.getWord()).append("' ");
			sb.append("  ,'").append(bean.getPronounce()).append("' ");
			sb.append("  ,'").append(bean.getVocabulary()).append("' ");
			sb.append("  ,").append(bean.getNextTime());
			sb.append("  ,").append(bean.getStudyTime());
			sb.append("  ,").append(bean.getTimes());
			sb.append("  ,'").append(BooleanUtils.toString(bean.isFavorite(), "1", "0")).append("' ");

			if (StringUtils.isNotEmpty(bean.getSound())) {
				sb.append("  ,'").append(bean.getSound()).append("' ");
			} else {
				sb.append("  ,null");
			}
			sb.append(")");

			sqlList.add(sb.toString());
		}

		DBUtils.execBatch(sqlList);
	}
}
