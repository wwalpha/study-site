package com.alpha.word.dao;

import java.util.List;

import com.alpha.entity.WordEntity;

public interface IWordDAO {

	List<WordEntity> getList(String userId);

	List<WordEntity> getNewwordList(String userId);

	List<WordEntity> getReviewList(String userId);

	List<WordEntity> getPlayList(String userId);

	long countReviews(String userId);

	int update(WordEntity entity);

	void reset(String userId, int wordNo);

	void addNews(String userId);

	void addReviews(String userId);
}