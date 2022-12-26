package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.LoginUser;
import com.muzike.domain.entity.User;
import com.muzike.domain.service.MenuService;
import com.muzike.domain.service.RoleService;
import com.muzike.domain.vo.MenuVo;
import com.muzike.domain.vo.RoutersVo;
import com.muzike.domain.vo.AdminUserInfoVo;
import com.muzike.domain.vo.UserInfoVo;
import com.muzike.service.AdminLoginService;
import com.muzike.utils.BeanCopyUtils;
import com.muzike.utils.RedisCache;
import com.muzike.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class AdminLoginController {

	@Autowired
	private AdminLoginService adminLoginService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RedisCache redisCache;
	@PostMapping("/user/login")
	public ResponseResult login(@RequestBody User user){
		if(Objects.isNull(user.getUserName())){
			throw new RuntimeException("用户名不能为空");
		}
		return adminLoginService.login(user);
	}

	//查询登录者的相关信息
	@GetMapping("/getInfo")
	public ResponseResult<AdminUserInfoVo> getInfo(){
		//获取当前登录用户的信息
		LoginUser loginUser = SecurityUtil.getLoginUser();
		//根据用户id查询权限信息
		List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
		//根据用户id查询角色信息
		List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
		//获取用户信息
		User user = loginUser.getUser();
		UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
		//封装返回
		AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
		return ResponseResult.okResult(adminUserInfoVo);

	}

	//查询登录者拥有的菜单权限
	@GetMapping("/getRouters")
	public ResponseResult<RoutersVo> getRouters(){
		Long userId = SecurityUtil.getUserId();
		//查询menu,结果是tree的形式
		List<MenuVo> menus = menuService.selectRouterMenuTreeByUserId(userId);
		//封装数据返回
		return ResponseResult.okResult(new RoutersVo(menus));
	}
	//退出登录
	@PostMapping("/user/logout")
	public ResponseResult logout(){
		return adminLoginService.logout();
	}
}
