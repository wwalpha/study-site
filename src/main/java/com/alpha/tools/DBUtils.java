package com.alpha.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.alpha.words.bean.WordBean;

public class DBUtils {

	public static final String SELECT_ALL = "SELECT USER_ID AS USERNAME, CATEGORY, WORD, PRONOUNCE, VOCABULARY, NEXT_TIME AS NEXTTIME, STUDY_TIME AS STUDYTIME, TIMES, FAVORITE, SOUND FROM WORDS WHERE USER_ID = ? ";
	public static final String SELECT_FAVORITE = "SELECT USER_ID AS USERNAME, CATEGORY, WORD, PRONOUNCE, VOCABULARY, NEXT_TIME AS NEXTTIME, STUDY_TIME AS STUDYTIME, TIMES, FAVORITE, SOUND FROM WORDS WHERE USER_ID = ? AND FAVORITE = '1' ";
	public static final String SELECT_USER_PROPS = "SELECT PAGE_OFFSET, DAY_LIMIT FROM USERS WHERE USER_ID = ? ";
	public static final String SELECT_USER_CTG = "SELECT DISTINCT CATEGORY FROM WORDS WHERE USER_ID = ? AND CATEGORY IS NOT NULL ";

	public static final String SELECT_NEWWORD = "SELECT DISTINCT USER_ID AS USERNAME, WORD, CATEGORY, PRONOUNCE, VOCABULARY, NEXT_TIME AS NEXTTIME, STUDY_TIME AS STUDYTIME, TIMES, FAVORITE, SOUND FROM WORDS WHERE USER_ID = ? AND NEXT_TIME <= DATE_FORMAT(NOW(),'%Y%m%d') AND (TIMES = 0 OR TIMES = 9999) ";
	public static final String SELECT_REVIEW = "SELECT DISTINCT USER_ID AS USERNAME, WORD, CATEGORY, PRONOUNCE, VOCABULARY, NEXT_TIME AS NEXTTIME, STUDY_TIME AS STUDYTIME, TIMES, FAVORITE, SOUND FROM WORDS WHERE USER_ID = ? AND NEXT_TIME <= DATE_FORMAT(NOW(),'%Y%m%d') AND TIMES <> 0 ";
	public static final String SELECT_PLAYLIST = "SELECT WORD, SOUND FROM WORDS WHERE USER_ID = ? AND STUDY_TIME = DATE_FORMAT(NOW(),'%Y%m%d') AND SOUND IS NOT NULL ORDER BY WORD_NO";
	public static final String SELECT_COUNT_REVIEWS = "SELECT COUNT(USER_ID) FROM WORDS WHERE USER_ID = ? AND STUDY_TIME = DATE_FORMAT(NOW(),'%Y%m%d') AND TIMES != 0 ";

	public static final String UPDATE_WORDS = "UPDATE WORDS SET TIMES = TIMES + 1, STUDY_TIME = ?, NEXT_TIME = ?, FAVORITE = ? WHERE USER_ID = ? AND WORD = ?";


	private DBUtils() {
	}

	/**
	 * SELECT EXECUTE
	 * 
	 * @param strSQL
	 * @param params
	 * @return
	 */
	public static <T> List<T> select(Class<T> clazz, String strSQL, Object... params) {
		DBQuery<T> query = new DBQuery<T>(clazz);

		return query.select(strSQL, params);
	}

	@SuppressWarnings("rawtypes")
	public static int selectCount(String strSQL, Object... params) {
		DBQuery query = new DBQuery();

		return query.selectCount(strSQL, params);
	}

	@SuppressWarnings("rawtypes")
	public static int update(String strSQL, Object... params) {
		DBQuery query = new DBQuery();

		return query.update(strSQL, params);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int[] execBatch(List<String> sqlList) {
		DBQuery query = new DBQuery();

		try {
			return query.execBatch(sqlList);
		} catch (Exception e) {
		} finally {
			query.close();
		}

		return new int[] {};
	}

	public static void main(String[] args) throws Exception {
		// DBQuery query = new DBQuery(String.class, URL, USER_NAME, PASSWORD);
		//
		// query.testConnection();

		File dir = new File("D:/Java/words");

		for (File file : dir.listFiles()) {
			List<String> allLines = FileUtils.readLines(file, "UTF-8");
			List<WordBean> retList = new ArrayList<>();
			String fileName = file.getName().replaceAll(".txt", "");
			System.out.println(fileName);
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

				if (StringUtils.indexOf(bean.getWord(), "'") != -1) {
					bean.setWord(bean.getWord().replaceAll("'", "\\\\'"));
				}

				retList.add(bean);
			}

			List<String> sqlList = new ArrayList<String>();

			for (WordBean bean : retList) {
				StringBuffer sb = new StringBuffer();

				sb.append("INSERT WORDS (");
				sb.append("  USER_ID");
				sb.append("  ,WORD_NO");
				sb.append("  ,CATEGORY");
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
				sb.append("  ,'").append(fileName).append("' ");
				sb.append("  ,'").append(bean.getWord()).append("' ");
				sb.append("  ,'").append(bean.getPronounce()).append("' ");
				sb.append("  ,'").append(bean.getVocabulary()).append("' ");
				sb.append("  ,'").append(bean.getNextTime()).append("' ");
				sb.append("  ,'").append(bean.getStudyTime()).append("' ");
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

			// DBUtils.execBatch(sqlList);
		}
	}
}
