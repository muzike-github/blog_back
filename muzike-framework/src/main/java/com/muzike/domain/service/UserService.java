package com.muzike.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddUserDto;
import com.muzike.domain.dto.UpdateUserDto;
import com.muzike.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-09-16 15:35:17
 */
public interface UserService extends IService<User> {

	ResponseResult getUserInfo();

	ResponseResult updateUserInfo(User user);

	ResponseResult register(User user);

	ResponseResult listUser(Integer pageNum, Integer pageSize,
	                        String userName,String phonenumber, String status);

	ResponseResult addUser(AddUserDto userDto);

	ResponseResult deleteUser(Long id);

	ResponseResult selectUserInfo(Long id);

	ResponseResult updateUser(UpdateUserDto userDto);
}

