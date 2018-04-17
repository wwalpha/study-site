package com.alpha.entity;
// Generated 2018/04/16 20:54:36 by Hibernate Tools 5.2.8.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WordHistory generated by hbm2java
 */
@Entity
@Table(name = "WORD_HISTORY")
public class WordHistory implements java.io.Serializable {

	private Integer id;
	private String userId;
	private String category;
	private String word;
	private String studyTime;
	private int times;

	public WordHistory() {
	}

	public WordHistory(String userId, String word, String studyTime, int times) {
		this.userId = userId;
		this.word = word;
		this.studyTime = studyTime;
		this.times = times;
	}
	public WordHistory(String userId, String category, String word, String studyTime, int times) {
		this.userId = userId;
		this.category = category;
		this.word = word;
		this.studyTime = studyTime;
		this.times = times;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "CATEGORY", length = 100)
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "WORD", nullable = false, length = 50)
	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Column(name = "STUDY_TIME", nullable = false, length = 11)
	public String getStudyTime() {
		return this.studyTime;
	}

	public void setStudyTime(String studyTime) {
		this.studyTime = studyTime;
	}

	@Column(name = "TIMES", nullable = false)
	public int getTimes() {
		return this.times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}