package com.muzike.controller;

import com.muzike.annotation.SystemLog;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.User;
import com.muzike.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 普通用户
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	//查询用户信息
	@GetMapping("/userInfo")
	public ResponseResult getUserInfo(){
		return userService.getUserInfo();
	}
	//更新用户信息
	@PutMapping("/userInfo")
	@SystemLog(businessName = "更新用户信息")
	public ResponseResult updateUserInfo(@RequestBody User user){
		return userService.updateUserInfo(user);
	}
	//注册
	@PostMapping("/register")
	public ResponseResult register(@RequestBody User user){
		return userService.register(user);
	}

}
