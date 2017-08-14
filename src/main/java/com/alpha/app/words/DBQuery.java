package com.alpha.app.words;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.alpha.bean.UpdateBean;
import com.alpha.bean.UserBean;
import com.alpha.bean.WordBean;
import com.alpha.tools.DBUtils;

public class DBQuery {

	public List<String> getUsers() {
		return DBUtils.select(String.class, DBUtils.SELECT_USERS);
	}

	public UserBean getUserProps(String userName) {
		List<UserBean> retList = DBUtils.select(UserBean.class, DBUtils.SELECT_USER_PROPS, userName);

		return retList.get(0);
	}

	public int cntReviews(String userName) {
		return DBUtils.selectCount(DBUtils.SELECT_COUNT_REVIEWS, userName);
	}

	public List<String> getCategories(String userName) {
		return DBUtils.select(String.class, DBUtils.SELECT_USER_CTG, userName);
	}

	public List<WordBean> getNewWords(String userName, String categories) {
		return DBUtils.select(WordBean.class, getSelectWordSQL(categories), userName);
	}

	public List<WordBean> getReviewWords(String userName, String categories) {
		return DBUtils.select(WordBean.class, getSelectReviewSQL(categories), userName);
	}

	public List<WordBean> getFavoriteWords(String userName, String categories) {
		return DBUtils.select(WordBean.class, DBUtils.SELECT_FAVORITE, userName);
	}

	public int updateWord(String userName, UpdateBean bean) {
		String updateSQL = getUpdateSQL(bean);
		List<Object> params = getUpdateParams(userName, bean);

		return DBUtils.update(updateSQL, params.toArray());
	}

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public List<WordBean> getTodayList(String userName) {
		return DBUtils.select(WordBean.class, DBUtils.SELECT_PLAYLIST, userName);
	}

	private String getSelectWordSQL(String categories) {
		StringBuffer sb = new StringBuffer();

		sb.append(DBUtils.SELECT_NEWWORD);
		sb.append(getInSQL(categories));
		sb.append("ORDER BY TIMES, NEXT_TIME DESC LIMIT 49");

		return sb.toString();
	}

	private String getSelectReviewSQL(String categories) {
		StringBuffer sb = new StringBuffer();

		sb.append(DBUtils.SELECT_REVIEW);
		sb.append(getInSQL(categories));

		return sb.toString();
	}

	private String getInSQL(String categories) {
		// データなし
		String[] ctgs = StringUtils.split(categories, ",");

		if (ctgs == null || ctgs.length == 0) {
			return StringUtils.EMPTY;
		}

		StringBuffer sb = new StringBuffer();

		sb.append("AND CATEGORY IN (");
		for (String category : ctgs) {
			sb.append("'").append(category).append("', ");
		}
		sb.deleteCharAt(sb.length() - 2);

		sb.append(") ");

		return sb.toString();
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
			sb.append(",T1.NEXT_TIME = nextTime(?, ?, ?, T1.TIMES) ");
		}

		sb.append(",T1.FAVORITE = ? ");
		sb.append("WHERE T1.USER_ID = ? AND T1.WORD = ? ");

		if (StringUtils.isNotEmpty(bean.getCategory())) {
			sb.append(" AND T1.CATEGORY = ? ");
		}

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

		if (!bean.isChecked()) {
			// USER
			retList.add(userName);
			// CATEGORY
			retList.add(bean.getCategory());
			// WORD
			retList.add(bean.getWord());
		}

		// FAVORITE
		retList.add(BooleanUtils.toString(bean.isFavorite(), "1", "0"));
		// USER
		retList.add(userName);
		// WORD
		retList.add(bean.getWord());
		// CATEGORY
		if (StringUtils.isNotEmpty(bean.getCategory())) {
			retList.add(bean.getCategory());
		}

		return retList;
	}
}
