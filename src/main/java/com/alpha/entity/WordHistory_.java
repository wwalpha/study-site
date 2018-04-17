package com.alpha.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-04-15T21:28:49.908+0900")
@StaticMetamodel(WordHistory.class)
public class WordHistory_ {
	public static volatile SingularAttribute<WordHistory, Integer> id;
	public static volatile SingularAttribute<WordHistory, String> userId;
	public static volatile SingularAttribute<WordHistory, String> category;
	public static volatile SingularAttribute<WordHistory, String> word;
	public static volatile SingularAttribute<WordHistory, String> studyTime;
	public static volatile SingularAttribute<WordHistory, Integer> times;
}
