package com.alpha.bean;

import java.util.List;

public class UserBean {
	private List<String> ctgNames;
	private int pageOffset;
	private int dayLimit;

	/**
	 * @return the ctgNames
	 */
	public List<String> getCtgNames() {
		return ctgNames;
	}

	/**
	 * @param ctgNames
	 *            the ctgNames to set
	 */
	public void setCtgNames(List<String> ctgNames) {
		this.ctgNames = ctgNames;
	}

	/**
	 * @return the pageOffset
	 */
	public int getPageOffset() {
		return pageOffset;
	}

	/**
	 * @param pageOffset
	 *            the pageOffset to set
	 */
	public void setPageOffset(int pageOffset) {
		this.pageOffset = pageOffset;
	}

	/**
	 * @return the dayLimit
	 */
	public int getDayLimit() {
		return dayLimit;
	}

	/**
	 * @param dayLimit
	 *            the dayLimit to set
	 */
	public void setDayLimit(int dayLimit) {
		this.dayLimit = dayLimit;
	}
}
