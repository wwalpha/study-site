package com.alpha.tools;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DBQuery<T> {

	private String url;
	private String userName;
	private String password;

	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	private Class<T> clazz = null;
	private Map<String, Field> fieldMap = null;

	DBQuery(Class<T> clazz, String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.clazz = clazz;
	}

	/**
	 * select execute
	 * 
	 * @param strSQL
	 * @param params
	 * @return
	 */
	public List<T> select(String strSQL, Object params) {
		List<T> retList = new ArrayList<T>();

		try {
			this.conn = getConnection();
			this.stmt = conn.prepareStatement(strSQL);
			this.rs = stmt.executeQuery();
		} catch (SQLException e) {
			this.close();
			return retList;
		}

		initFieldMap();

		try {
			ResultSetMetaData metaData = rs.getMetaData();

			while (rs.next()) {

				T bean = setValue(rs, metaData);

				retList.add(bean);
			}
		} catch (Exception e) {
		} finally {
			this.close();
		}

		return retList;

	}

	private T setValue(ResultSet rs, ResultSetMetaData metaData) throws Exception {
		int columnCount = metaData.getColumnCount();
		T bean = clazz.newInstance();

		for (int i = 0; i < columnCount; i++) {
			String columnName = metaData.getColumnName(i).toUpperCase();

			if (!this.fieldMap.containsKey(columnName)) {
				continue;
			}

			Field field = this.fieldMap.get(columnName);

			field.setAccessible(true);
			field.set(bean, rs.getObject(i));
		}

		return bean;
	}

	/**
	 * 
	 */
	private void initFieldMap() {
		this.fieldMap = new HashMap<>();
		Field[] fields = this.clazz.getFields();

		for (Field field : fields) {
			this.fieldMap.put(field.getName().toUpperCase(), field);
		}
	}

	/**
	 * get connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(this.url, this.userName, this.password);
	}

	/**
	 * 
	 * @param conn
	 * @param stmt
	 * @param rs
	 */
	public void close() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		rs = null;
		stmt = null;
		conn = null;
	}
}
