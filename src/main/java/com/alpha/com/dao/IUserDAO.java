package com.alpha.com.dao;

import java.util.List;

import com.alpha.entity.User;

public interface IUserDAO {

	List<User> getUserList();

	User find(String userId);
}
