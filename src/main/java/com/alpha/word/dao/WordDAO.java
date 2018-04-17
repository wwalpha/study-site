package com.alpha.word.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.entity.WordEntity;
import com.alpha.entity.WordEntityId_;
import com.alpha.entity.WordEntity_;

@Transactional
@Repository
public class WordDAO implements IWordDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<WordEntity> getList(String userId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<WordEntity> query = cb.createQuery(WordEntity.class);
		Root<WordEntity> root = query.from(WordEntity.class);

		query.where(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.userId), userId));

		List<WordEntity> retList = entityManager.createQuery(query).getResultList();
		return retList;
	}

	@Override
	public long countReviews(String userId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<WordEntity> root = query.from(WordEntity.class);

		query.select(cb.count(query.from(WordEntity.class)));

		List<Predicate> where = new ArrayList<>();
		// userid = :userid
		where.add(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.userId), userId));
		// studyTime = DATE_FORMAT(NOW(), '%Y%m%d')
		where.add(cb.equal(root.get(WordEntity_.studyTime), cb.function("DATE_FORMAT", String.class, cb.function("NOW", Timestamp.class), cb.literal("%Y%m%d"))));
		// times <> 0
		where.add(cb.notEqual(root.get(WordEntity_.times), 0));

		query.where(where.toArray(new Predicate[where.size()]));

		return entityManager.createQuery(query).getSingleResult();
	}

	@Override
	public List<WordEntity> getNewwordList(String userId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<WordEntity> query = cb.createQuery(WordEntity.class);
		Root<WordEntity> root = query.from(WordEntity.class);

		query.distinct(true);

		List<Predicate> where = new ArrayList<>();
		// userid = :userid
		where.add(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.userId), userId));
		// nextTime <= DATE_FORMAT(NOW(), '%Y%m%d')
		where.add(cb.lessThanOrEqualTo(root.get(WordEntity_.nextTime),
				cb.function("DATE_FORMAT", String.class, cb.function("NOW", Timestamp.class), cb.literal("%Y%m%d"))));
		// times = 0 or times = 9999
		where.add(cb.or(cb.equal(root.get(WordEntity_.times), 0), cb.equal(root.get(WordEntity_.times), 9999)));
		// target = 1
		where.add(cb.equal(root.get(WordEntity_.target), Character.valueOf('1')));

		query.where(where.toArray(new Predicate[where.size()]));

		TypedQuery<WordEntity> typedQuery = entityManager.createQuery(query);
		typedQuery.setMaxResults(49);

		return typedQuery.getResultList();
	}

	@Override
	public List<WordEntity> getReviewList(String userId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<WordEntity> query = cb.createQuery(WordEntity.class);
		Root<WordEntity> root = query.from(WordEntity.class);

		query.distinct(true);

		List<Predicate> where = new ArrayList<>();
		// userid = :userid
		where.add(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.userId), userId));
		// nextTime <= DATE_FORMAT(NOW(), '%Y%m%d')
		where.add(cb.lessThanOrEqualTo(root.get(WordEntity_.nextTime),
				cb.function("DATE_FORMAT", String.class, cb.function("NOW", Timestamp.class), cb.literal("%Y%m%d"))));
		// times <> 0
		where.add(cb.notEqual(root.get(WordEntity_.times), 0));
		// target = 1
		where.add(cb.equal(root.get(WordEntity_.target), Character.valueOf('1')));

		query.where(where.toArray(new Predicate[where.size()]));

		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public List<WordEntity> getPlayList(String userId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<WordEntity> query = cb.createQuery(WordEntity.class);
		Root<WordEntity> root = query.from(WordEntity.class);

		List<Predicate> where = new ArrayList<>();
		// userid <> :userid
		where.add(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.userId), userId));
		// studyTime = DATE_FORMAT(NOW(), '%Y%m%d')
		where.add(cb.equal(root.get(WordEntity_.studyTime), cb.function("DATE_FORMAT", String.class, cb.function("NOW", Timestamp.class), cb.literal("%Y%m%d"))));
		// sound is not null
		where.add(cb.isNotNull(root.get(WordEntity_.sound)));

		query.where(where.toArray(new Predicate[where.size()]));

		// order by wordNo
		query.orderBy(cb.asc(root.get(WordEntity_.id).get(WordEntityId_.wordNo)));

		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public int update(WordEntity entity) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<WordEntity> update = cb.createCriteriaUpdate(WordEntity.class);
		Root<WordEntity> root = update.from(WordEntity.class);

		update.set(root.get(WordEntity_.times), cb.sum(root.get(WordEntity_.times), 1));
		update.set(root.get(WordEntity_.studyTime), cb.function("DATE_FORMAT", String.class, cb.function("NOW", Timestamp.class), cb.literal("%Y%m%d")));
		update.set(root.get(WordEntity_.nextTime), cb.function("nextTime", String.class, cb.literal(entity.getId().getUserId()), cb.literal(""),
				cb.literal(entity.getWord()), root.get(WordEntity_.times)));
		update.set(root.get(WordEntity_.favorite), entity.getFavorite());
		update.set(root.get(WordEntity_.target), Character.valueOf('0'));

		List<Predicate> where = new ArrayList<>();
		// userid = :userid
		where.add(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.userId), entity.getId().getUserId()));
		// studyTime = DATE_FORMAT(NOW(), '%Y%m%d')
		where.add(cb.equal(root.get(WordEntity_.word), entity.getWord()));

		update.where(where.toArray(new Predicate[where.size()]));

		return entityManager.createQuery(update).executeUpdate();
	}

	@Override
	public void reset(String userId, int wordNo) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<WordEntity> update = cb.createCriteriaUpdate(WordEntity.class);
		Root<WordEntity> root = update.from(WordEntity.class);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);

		update.set(root.get(WordEntity_.times), 0);
		update.set(root.get(WordEntity_.nextTime), cb.function("DATE_FORMAT", String.class, cb.literal(calendar.getTime()), cb.literal("%Y%m%d")));
		update.set(root.get(WordEntity_.target), Character.valueOf('0'));

		List<Predicate> where = new ArrayList<>();
		// userid = :userid
		where.add(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.userId), userId));
		// word = :word
		where.add(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.wordNo), wordNo));

		update.where(where.toArray(new Predicate[where.size()]));

		entityManager.createQuery(update).executeUpdate();
	}

	@Override
	public void addNews(String userId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<WordEntity> query = cb.createQuery(WordEntity.class);
		Root<WordEntity> root = query.from(WordEntity.class);

		query.distinct(true);

		List<Predicate> where = new ArrayList<>();
		// userid = :userid
		where.add(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.userId), userId));
		// nextTime <= DATE_FORMAT(NOW(), '%Y%m%d')
		where.add(cb.lessThanOrEqualTo(root.get(WordEntity_.nextTime),
				cb.function("DATE_FORMAT", String.class, cb.function("NOW", Timestamp.class), cb.literal("%Y%m%d"))));
		// times = 0 or times = 9999
		where.add(cb.or(cb.equal(root.get(WordEntity_.times), 0), cb.equal(root.get(WordEntity_.times), 9999)));
		// target <> 1
		where.add(cb.notEqual(root.get(WordEntity_.target), Character.valueOf('1')));

		query.where(where.toArray(new Predicate[where.size()]));

		TypedQuery<WordEntity> typedQuery = entityManager.createQuery(query);
		typedQuery.setMaxResults(49);

		List<WordEntity> retList = typedQuery.getResultList();

		for (WordEntity wordEntity : retList) {
			wordEntity.setTarget(Character.valueOf('1'));
		}

		entityManager.flush();

	}

	@Override
	public void addReviews(String userId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<WordEntity> query = cb.createQuery(WordEntity.class);
		Root<WordEntity> root = query.from(WordEntity.class);

		query.distinct(true);

		List<Predicate> where = new ArrayList<>();
		// userid = :userid
		where.add(cb.equal(root.get(WordEntity_.id).get(WordEntityId_.userId), userId));
		// nextTime <= DATE_FORMAT(NOW(), '%Y%m%d')
		where.add(cb.lessThanOrEqualTo(root.get(WordEntity_.nextTime),
				cb.function("DATE_FORMAT", String.class, cb.function("NOW", Timestamp.class), cb.literal("%Y%m%d"))));
		// times <> 0
		where.add(cb.notEqual(root.get(WordEntity_.times), 0));

		query.where(where.toArray(new Predicate[where.size()]));

		TypedQuery<WordEntity> typedQuery = entityManager.createQuery(query);
		typedQuery.setMaxResults(49);

		List<WordEntity> retList = typedQuery.getResultList();

		for (WordEntity wordEntity : retList) {
			wordEntity.setTarget(Character.valueOf('1'));
		}

		entityManager.flush();
	}
}
