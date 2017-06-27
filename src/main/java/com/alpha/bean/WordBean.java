package com.alpha.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WordBean {
	private int index;
	private String userName;
	private String word;
	private String pronounce;
	private String vocabulary;
	private int nextTime;
	private int times;
	private int startPos;
	private int endPos;
	private boolean favorite;
	private String sound;

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the pronounce
	 */
	public String getPronounce() {
		return pronounce;
	}

	/**
	 * @param pronounce
	 *            the pronounce to set
	 */
	public void setPronounce(String pronounce) {
		this.pronounce = pronounce;
	}

	/**
	 * @return the vocabulary
	 */
	public String getVocabulary() {
		return vocabulary;
	}

	/**
	 * @param vocabulary
	 *            the vocabulary to set
	 */
	public void setVocabulary(String vocabulary) {
		this.vocabulary = vocabulary;
	}

	/**
	 * @return the nextTime
	 */
	public int getNextTime() {
		return nextTime;
	}

	/**
	 * @param nextTime
	 *            the nextTime to set
	 */
	public void setNextTime(int nextTime) {
		this.nextTime = nextTime;
	}

	/**
	 * @return the times
	 */
	public int getTimes() {
		return times;
	}

	/**
	 * @param times
	 *            the times to set
	 */
	public void setTimes(int times) {
		this.times = times;
	}

	public int getRate() {
		if (this.times == 0) {
			return 0;
		}

		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");

		DateTime a = null;

		try {
			a = dtf.parseDateTime(String.valueOf(this.nextTime));
		} catch (Exception e) {
			a = DateTime.now();
			this.nextTime = Integer.parseInt(a.toString("yyyyMMdd"));
		}

		DateTime b = new DateTime(Calendar.getInstance().getTime());

		int days = Days.daysBetween(a, b).getDays() + 1;

		return days * 10 * times;
	}

	/**
	 * @return the startPos
	 */
	public int getStartPos() {
		return startPos;
	}

	/**
	 * @param startPos
	 *            the startPos to set
	 */
	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	/**
	 * @return the endPos
	 */
	public int getEndPos() {
		return endPos;
	}

	/**
	 * @param endPos
	 *            the endPos to set
	 */
	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the favorite
	 */
	public boolean isFavorite() {
		return favorite;
	}

	/**
	 * @param favorite
	 *            the favorite to set
	 */
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public String toString() {
		List<String> retList = new ArrayList<String>();

		retList.add(userName);
		retList.add(word);
		retList.add(pronounce);
		retList.add(vocabulary);
		retList.add(StringUtils.leftPad(String.valueOf(nextTime), 8, "0"));
		retList.add(String.valueOf(times));
		retList.add(Boolean.toString(favorite));
		retList.add(sound);

		return StringUtils.join(retList, "|");
	}

	/**
	 * @return the sound
	 */
	public String getSound() {
		return sound;
	}

	/**
	 * @param sound
	 *            the sound to set
	 */
	public void setSound(String sound) {
		this.sound = sound;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
}
