package com.alpha.tools;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

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

	DBQuery(String url, String userName, String password) {
		this(null, url, userName, password);
	}

	/**
	 * select execute
	 * 
	 * @param strSQL
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> select(String strSQL, Object... params) {
		List<T> retList = new ArrayList<T>();

		System.out.println(strSQL);
		try {
			this.conn = getConnection();
			this.stmt = conn.prepareStatement(strSQL);

			this.setParameters(params);

			this.rs = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			this.close();
			return retList;
		}

		initFieldMap();

		try {
			ResultSetMetaData metaData = rs.getMetaData();

			while (rs.next()) {
				if (clazz.equals(String.class)) {
					retList.add((T) rs.getString(1));
				} else if (clazz.equals(Integer.class)) {
					retList.add((T) Integer.valueOf(rs.getInt(1)));
				} else {
					T bean = setValue(rs, metaData);

					retList.add(bean);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}

		return retList;
	}

	/**
	 * 
	 * @param strSQL
	 * @param params
	 * @return
	 */
	public int update(String strSQL, Object... params) {
		try {
			System.out.println(strSQL);
			this.conn = getConnection();
			this.stmt = conn.prepareStatement(strSQL);
		} catch (SQLException e) {
			this.close();
			return 0;
		}

		try {
			setParameters(params);

			return this.stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * execute batch sql
	 * 
	 * @param sqlList
	 * @return
	 */
	public int[] execBatch(List<String> sqlList) {
		try {
			this.conn = getConnection();
		} catch (SQLException e) {
			this.close();
			return new int[] { 0 };
		}

		try {
			Statement stmt = this.conn.createStatement();

			for (String strSQL : sqlList) {
				stmt.addBatch(strSQL);
			}

			return stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new int[] { 0 };
	}

	private T setValue(ResultSet rs, ResultSetMetaData metaData) throws Exception {
		int columnCount = metaData.getColumnCount();
		T bean = clazz.newInstance();

		for (int i = 1; i <= columnCount; i++) {
			String columnName = metaData.getColumnLabel(i).toUpperCase();

			if (!this.fieldMap.containsKey(columnName)) {
				continue;
			}

			Field field = this.fieldMap.get(columnName);

			field.setAccessible(true);

			if (field.getType().equals(String.class)) {
				field.set(bean, rs.getObject(i));
			} else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
				field.setBoolean(bean, BooleanUtils.toBoolean(Integer.parseInt(rs.getString(i)), 1, 0));
			} else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
				field.setInt(bean, Integer.parseInt(rs.getString(i)));
			}
		}

		return bean;
	}

	/**
	 * 
	 */
	private void initFieldMap() {
		this.fieldMap = new HashMap<>();
		Field[] fields = this.clazz.getDeclaredFields();

		for (Field field : fields) {
			this.fieldMap.put(field.getName().toUpperCase(), field);
		}
	}

	/**
	 * 
	 * @param stmt
	 * @param params
	 * @throws SQLException
	 */
	private void setParameters(Object... params) throws SQLException {
		for (int i = 0; i < params.length; ++i) {
			stmt.setString(i + 1, String.valueOf(params[i]));
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

	public void testConnection() {
		try {
			this.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("success");
	}
}
