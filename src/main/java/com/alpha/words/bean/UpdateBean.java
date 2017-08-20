package com.alpha.words.bean;

public class UpdateBean {
	private String word;
	private String category;
	private boolean checked;
	private boolean favorite;

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
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked
	 *            the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
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

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}
