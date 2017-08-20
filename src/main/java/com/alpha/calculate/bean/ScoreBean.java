package com.alpha.calculate.bean;

public class ScoreBean {
	private int leftNum;
	private String operator;
	private int rightNum;
	private Integer answer;
	private String success;

	/**
	 * @return the leftNum
	 */
	public int getLeftNum() {
		return leftNum;
	}

	/**
	 * @param leftNum
	 *            the leftNum to set
	 */
	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the rightNum
	 */
	public int getRightNum() {
		return rightNum;
	}

	/**
	 * @param rightNum
	 *            the rightNum to set
	 */
	public void setRightNum(int rightNum) {
		this.rightNum = rightNum;
	}

	/**
	 * @return the answer
	 */
	public Integer getAnswer() {
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(Integer answer) {
		this.answer = answer;
	}

	/**
	 * @return the success
	 */
	public String getSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

}
