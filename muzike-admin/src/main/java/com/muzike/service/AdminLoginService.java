package com.muzike.service;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.User;

public interface AdminLoginService {
	ResponseResult login(User user);

	ResponseResult logout();
}
