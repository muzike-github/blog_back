package com.muzike.service.impl;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.LoginUser;
import com.muzike.domain.entity.User;
import com.muzike.service.AdminLoginService;
import com.muzike.utils.JwtUtil;
import com.muzike.utils.RedisCache;
import com.muzike.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private RedisCache redisCache;
	@Override
	public ResponseResult login(User user) {
		//封装用户信息，调用认证方法
		UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
		Authentication authenticate = authenticationManager.authenticate(authenticationToken);
		//判断是否认证通过
		if(Objects.isNull(authenticate)){
			throw new RuntimeException("用户名或密码错误");
		}
		//获取登录用户的信息(转化为loginUSer类型)
		LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
		String userid = loginUser.getUser().getId().toString();
		//根据userid生成token
		String token = JwtUtil.createJWT(userid);
		redisCache.setCacheObject("adminLogin:"+userid,loginUser);
		//将token值返回
		Map<String,String> map=new HashMap<>();
		map.put("token",token);
		return ResponseResult.okResult(map);
	}

	@Override
	public ResponseResult logout() {
		//获取当前登录用户的id
		Long userId = SecurityUtil.getUserId();
		//删除redis中的值
		redisCache.deleteObject("adminLogin:"+userId);
		return ResponseResult.okResult();
	}
}
