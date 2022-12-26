package com.muzike.handler.security;

import com.alibaba.fastjson.JSON;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.enums.AppHttpCodeEnum;
import com.muzike.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
* 处理认证登录失败
* */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		ResponseResult result;
		if(authException instanceof BadCredentialsException){
			result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR,authException.getMessage());
		}else if(authException instanceof InsufficientAuthenticationException){
			result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_lOGIN);
		}else {
			result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"你小子有问题");
		}
		WebUtils.renderString(response, JSON.toJSONString(result));
	}
}
