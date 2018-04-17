package com.alpha.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-04-16T20:55:15.470+0900")
@StaticMetamodel(WordEntity.class)
public class WordEntity_ {
	public static volatile SingularAttribute<WordEntity, WordEntityId> id;
	public static volatile SingularAttribute<WordEntity, Character> target;
	public static volatile SingularAttribute<WordEntity, String> category;
	public static volatile SingularAttribute<WordEntity, String> word;
	public static volatile SingularAttribute<WordEntity, String> pronounce;
	public static volatile SingularAttribute<WordEntity, String> vocabulary;
	public static volatile SingularAttribute<WordEntity, String> nextTime;
	public static volatile SingularAttribute<WordEntity, String> studyTime;
	public static volatile SingularAttribute<WordEntity, Integer> times;
	public static volatile SingularAttribute<WordEntity, Character> favorite;
	public static volatile SingularAttribute<WordEntity, String> sound;
}
