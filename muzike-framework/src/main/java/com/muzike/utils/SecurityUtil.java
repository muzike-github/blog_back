package com.muzike.utils;

import com.muzike.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *从token中获取相关信息
 */
public class SecurityUtil {

	public static LoginUser getLoginUser(){
		//此处需要处理token过期
		System.out.println(getAuthentication().getPrincipal());
		return (LoginUser) getAuthentication().getPrincipal();
	}

	public static Authentication getAuthentication(){
		return SecurityContextHolder.getContext().getAuthentication();
	}
	public static Long getUserId(){
		return getLoginUser().getUser().getId();
	}
	public static Boolean isAdmin(){
		Long userId = getUserId();
		return userId!=null&&userId.equals(1L);
	}
}
