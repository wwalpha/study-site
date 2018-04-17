package com.alpha.word.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alpha.entity.WordEntity;
import com.alpha.word.dao.IWordDAO;

@Service
public class WordService implements IWordService {

	@Autowired
	private IWordDAO dao;

	@Override
	public List<WordEntity> getList(String userId) {
		return dao.getList(userId);
	}

	@Override
	public long countReviews(String userId) {
		return dao.countReviews(userId);
	}

	@Override
	public List<WordEntity> getNewwordList(String userId) {
		List<WordEntity> newList = dao.getNewwordList(userId);

		// 対象0件
		if (newList.size() == 0L) {
			// 対象単語作成する
			dao.addNews(userId);

			// 再検索
			newList = dao.getNewwordList(userId);
		}

		return newList;
	}

	@Override
	public List<WordEntity> getReviewList(String userId) {
		List<WordEntity> reviewList = dao.getReviewList(userId);

		// 対象0件
		if (reviewList.size() == 0L) {
			// 対象単語作成する
			dao.addReviews(userId);
			// 再検索
			reviewList = dao.getReviewList(userId);
		}

		return reviewList;
	}

	@Override
	public List<WordEntity> getPlayList(String userId) {
		return dao.getPlayList(userId);
	}

	@Override
	public void save(WordEntity entity) {
		dao.update(entity);
	}

	@Override
	public void reset(String userId, int wordNo) {
		dao.reset(userId, wordNo);
	}
}
