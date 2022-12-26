package com.muzike.domain.service;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.User;

public interface BlogLoginService {
	ResponseResult login(User user);

	ResponseResult logout();
}
