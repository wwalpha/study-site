package com.alpha.word.service;

import java.util.List;

import com.alpha.entity.WordEntity;

public interface IWordService {

	List<WordEntity> getList(String userId);

	List<WordEntity> getNewwordList(String userId);

	List<WordEntity> getReviewList(String userId);

	List<WordEntity> getPlayList(String userId);

	long countReviews(String userId);

	void save(WordEntity entity);

	void reset(String userId, int wordNo);
}
