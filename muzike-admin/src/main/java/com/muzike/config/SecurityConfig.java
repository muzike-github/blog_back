package com.muzike.config;

import com.muzike.filter.JwtAuthenticationTokenFilter;
import com.muzike.handler.security.AccessDeniedHandlerImpl;
import com.muzike.handler.security.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
	@Autowired
	private AuthenticationEntryPointImpl authenticationEntryPoint;
	@Autowired
	private AccessDeniedHandlerImpl accessDeniedHandler;
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				//关闭csrf
				.csrf().disable()
				//不通过Session获取SecurityContext
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				// 对于登录接口 允许匿名访问
//				.antMatchers("/login").anonymous()
//				//对于退出接口，必须携带token才能访问（认证）
//				.antMatchers("/logout").authenticated()
//				.antMatchers("/user/userInfo").authenticated()
//				.antMatchers("/upload").authenticated()
				// 除上面外的所有请求全部不需要认证即可访问
				.anyRequest().permitAll();
		//关闭springSecurity默认的退出功能
		http.logout().disable();
		//配置自定义异常处理(认证和授权失败)
		http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler);
		//把jwtAuthenticationTokenFilter添加到SpringSecurity的过滤器链中令其生效
		http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		//允许跨域
		http.cors();
	}
}
