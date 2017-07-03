package com.alpha.tools;

import java.util.List;

public class DBUtils {

	public static final String SELECT_ALL = "";
	public static final String SELECT_USERS = "";

	public static final String SELECT_NEWWORD = "";
	public static final String UPDATE_NEWWORD = "";
	public static final String SELECT_REVIEW = "";
	public static final String UPDATE_REVIEW = "";

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

	public int update(Class clazz, String strSQL, Object... params) {
		// DBQuery query = new DBQuery(clazz, URL, USER_NAME, PASSWORD);

		return 0;
	}
}
