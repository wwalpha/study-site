package com.alpha.com.service;

import java.util.List;

import com.alpha.entity.User;

public interface IUserService {
	List<User> getUserList();

	User getUserProps(String userId);
}
