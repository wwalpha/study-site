package com.alpha.words.bean;

public class StatisticBean {
	private String studyTime;
	private int newCount;
	private int reviewCount;

	/**
	 * @return the newCount
	 */
	public int getNewCount() {
		return newCount;
	}

	/**
	 * @param newCount
	 *            the newCount to set
	 */
	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}

	/**
	 * @return the reviewCount
	 */
	public int getReviewCount() {
		return reviewCount;
	}

	/**
	 * @param reviewCount
	 *            the reviewCount to set
	 */
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	/**
	 * @return the studyTime
	 */
	public String getStudyTime() {
		return studyTime;
	}

	/**
	 * @param studyTime the studyTime to set
	 */
	public void setStudyTime(String studyTime) {
		this.studyTime = studyTime;
	}

}
