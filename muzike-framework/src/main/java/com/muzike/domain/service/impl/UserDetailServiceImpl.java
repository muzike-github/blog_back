package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.muzike.constants.SystemConstants;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.LoginUser;
import com.muzike.domain.entity.User;
import com.muzike.domain.mapper.MenuMapper;
import com.muzike.domain.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private MenuMapper menuMapper;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//根据用户名查询数据库
		LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getUserName,username);
		User user = userMapper.selectOne(queryWrapper);
		//判断是否有该用户,抛出异常
		if(Objects.isNull(user)){
			throw new RuntimeException("用户不存在");
		}
		//对后台管理员用户进行相应的权限查询
		//返回用户信息和相应的权限信息
		if(user.getType().equals(SystemConstants.ADMIN)){
			List<String> perms = menuMapper.getPermsByUserId(user.getId());
			return new LoginUser(user,perms);
		}
		//如果是非管理员用户，权限为空
		return new LoginUser(user,null);
	}
}
