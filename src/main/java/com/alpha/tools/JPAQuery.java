package com.alpha.tools;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JPAQuery<T> {

	private CriteriaBuilder cb;
	private Class<T> clazz;
	private Root<T> root;
	private CriteriaQuery<T> query;
	private List<Predicate> whereList;

	public JPAQuery(CriteriaBuilder cb, Class<T> clazz) {
		this.cb = cb;
		this.clazz = clazz;
		this.query = cb.createQuery(clazz);
		this.root = this.query.from(clazz);

		this.whereList = new ArrayList<>();
	}

	public CriteriaQuery<T> getQuery() {

		if (whereList.size() != 0) {
			query.where(whereList.toArray(new Predicate[whereList.size()]));
		}

		return this.query;
	}

	public void addWhere() {

	}

	public void addSelect() {

	}
}
