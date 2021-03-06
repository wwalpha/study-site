package com.alpha.entity;
// Generated 2018/04/16 20:54:36 by Hibernate Tools 5.2.8.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CalcQuestionId generated by hbm2java
 */
@Embeddable
public class CalcQuestionId implements java.io.Serializable {

	private int grade;
	private int month;
	private int no;

	public CalcQuestionId() {
	}

	public CalcQuestionId(int grade, int month, int no) {
		this.grade = grade;
		this.month = month;
		this.no = no;
	}

	@Column(name = "GRADE", nullable = false)
	public int getGrade() {
		return this.grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	@Column(name = "MONTH", nullable = false)
	public int getMonth() {
		return this.month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@Column(name = "NO", nullable = false)
	public int getNo() {
		return this.no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CalcQuestionId))
			return false;
		CalcQuestionId castOther = (CalcQuestionId) other;

		return (this.getGrade() == castOther.getGrade()) && (this.getMonth() == castOther.getMonth()) && (this.getNo() == castOther.getNo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getGrade();
		result = 37 * result + this.getMonth();
		result = 37 * result + this.getNo();
		return result;
	}

}
