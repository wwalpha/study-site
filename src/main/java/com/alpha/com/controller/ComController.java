package com.alpha.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alpha.com.service.IUserService;
import com.alpha.entity.User;

@Controller
@CrossOrigin
@RequestMapping("users")
public class ComController {

	@Autowired
	private IUserService userService;

	@GetMapping("")
	public ResponseEntity<List<User>> getUserList() {
		return ResponseEntity.ok(userService.getUserList());
	}

	@GetMapping("{userId}/props")
	public ResponseEntity<User> getUserProps(@PathVariable String userId) {
		return ResponseEntity.ok(userService.getUserProps(userId));
	}

	// @RequestMapping("/{user}/statistic")
	// public ResponseEntity<List<StatisticBean>> statistic(@PathVariable String
	// user) {
	// return ResponseEntity.ok(Utils.getStatistic(user));
	// }

}
