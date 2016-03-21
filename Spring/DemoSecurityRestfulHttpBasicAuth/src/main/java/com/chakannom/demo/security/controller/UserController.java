package com.chakannom.demo.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chakannom.demo.security.domain.UserVo;
import com.chakannom.demo.security.service.LoginService;

@RestController
public class UserController {
	@Autowired
	private LoginService loginService;
	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	public List<UserVo> getUserVoList() {
		List<UserVo> userList = new ArrayList<UserVo>();
		userList = loginService.getUserList();
		return userList;
	}
}
