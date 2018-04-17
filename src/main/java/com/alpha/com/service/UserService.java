package com.alpha.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alpha.com.dao.IUserDAO;
import com.alpha.entity.User;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserDAO dao;

	@Override
	public List<User> getUserList() {
		return dao.getUserList();
	}

	@Override
	public User getUserProps(String userId) {
		return dao.find(userId);
	}
}
