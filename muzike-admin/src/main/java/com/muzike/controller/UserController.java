package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddUserDto;
import com.muzike.domain.dto.UpdateUserDto;
import com.muzike.domain.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

/**
 * 后台管理用户
 */
@RestController
@RequestMapping("/system/user")
public class UserController {
	@Autowired
	private UserService userService;
	//查询所有用户
	@GetMapping("/list")
	public ResponseResult listUser(@RequestParam Integer pageNum,
	                               @RequestParam Integer pageSize,
	                               @RequestParam(value="userName",required = false) String userName,
	                                String phonenumber,
	                                String status){
		return userService.listUser(pageNum,pageSize,userName,phonenumber,status);
	}
	//新增用户
	@PostMapping("")
	public ResponseResult addUser(@RequestBody AddUserDto userDto){
		return userService.addUser(userDto);
	}
	//删除用户
	@DeleteMapping("/{id}")
	public ResponseResult deleteUser(@PathVariable Long id){
		return userService.deleteUser(id);
	}
	//修改用户（先查找后更新）
	@GetMapping("/{id}")
	public ResponseResult getUserInfo(@PathVariable Long id){
		return userService.selectUserInfo(id);
	}
	@PutMapping("")
	public ResponseResult updateUser(@RequestBody UpdateUserDto userDto){
		return userService.updateUser(userDto);
	}
}
