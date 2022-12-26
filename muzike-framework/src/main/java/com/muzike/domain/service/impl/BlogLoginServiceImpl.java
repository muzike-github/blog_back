package com.muzike.domain.service.impl;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.LoginUser;
import com.muzike.domain.entity.User;
import com.muzike.domain.service.BlogLoginService;
import com.muzike.domain.vo.BlogUserLoginVo;
import com.muzike.domain.vo.AdminUserInfoVo;
import com.muzike.utils.BeanCopyUtils;
import com.muzike.utils.JwtUtil;
import com.muzike.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
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
		redisCache.setCacheObject("blogLogin:"+userid,loginUser);
		//将token和用户信息封装后返回
		AdminUserInfoVo adminUserInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), AdminUserInfoVo.class);
		BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(token, adminUserInfoVo);
		return ResponseResult.okResult(blogUserLoginVo);
	}

	@Override
	public ResponseResult logout() {
		//获取token解析token值
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		LoginUser loginUser = (LoginUser) authentication.getPrincipal();
		Long userid=loginUser.getUser().getId();
		//删除redis中的值
		redisCache.deleteObject("blogLogin:"+userid);
		return ResponseResult.okResult();
	}
}
