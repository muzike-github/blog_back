package com.muzike.filter;

import com.alibaba.fastjson.JSON;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.LoginUser;
import com.muzike.domain.enums.AppHttpCodeEnum;
import com.muzike.utils.JwtUtil;
import com.muzike.utils.RedisCache;
import com.muzike.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private RedisCache redisCache;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//获取token信息
		String token = request.getHeader("token");
		if(!StringUtils.hasText(token)){
			//如果没有token值，要么该接口不需要token，要么用户异常访问
			//我们都可以放行，不需要token直接放行，异常访问的话后面的拦截器会拦截
			filterChain.doFilter(request,response);
			return;
		}
		//解析userid
		Claims claims = null;
		try {
			claims = JwtUtil.parseJWT(token);
		} catch (Exception e) {
			e.printStackTrace();
			//token异常,此处不能直接抛出异常，因为这是filter，我们的自定义异常捕捉controller中的异常
			ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_lOGIN);
			//将响应码转为json串并写入响应体中
			WebUtils.renderString(response, JSON.toJSONString(result));
			return;
		}
		//从redis中取出信息
		String userid = claims.getSubject();
		LoginUser loginUser = redisCache.getCacheObject("blogLogin:" + userid);
		//如果从redis中获取不到用户信息,说明token过期，需重新登录
		if (Objects.isNull(loginUser)){
			ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_lOGIN);
			//将响应码转为json串并写入响应体中
			WebUtils.renderString(response, JSON.toJSONString(result));
			return;
		}
		//将权限信息一起封装后存入contextHolder
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(loginUser,null,null);
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		filterChain.doFilter(request,response);
	}
}
