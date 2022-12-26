package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.User;
import com.muzike.domain.enums.AppHttpCodeEnum;
import com.muzike.domain.service.BlogLoginService;
import com.muzike.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {
	@Autowired
	private BlogLoginService blogLoginService;
	@PostMapping("/login")
	public ResponseResult login(@RequestBody User user){
		if(!StringUtils.hasText(user.getUserName())){
			throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
		}
		return blogLoginService.login(user);
	}
	@PostMapping("/logout")
	public ResponseResult logout(){
		return blogLoginService.logout();
	}
}
