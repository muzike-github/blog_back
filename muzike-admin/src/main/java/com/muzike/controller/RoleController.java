package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddRoleDto;
import com.muzike.domain.dto.RoleStatusDto;
import com.muzike.domain.dto.UpdateRoleDto;
import com.muzike.domain.entity.Role;
import com.muzike.domain.service.RoleService;
import com.muzike.domain.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	//查询所有角色
	@GetMapping("/list")
	public ResponseResult listUser(Integer pageNum, Integer pageSize,
	                               String roleName, String status){
		return roleService.listUser(pageNum,pageSize,roleName,status);
	}
	//改变角色状态
	@PutMapping("/changeStatus")
	public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto){

		return roleService.changeStatus(roleStatusDto);
	}
	//新增角色
	@PostMapping("")
	public ResponseResult addRole(@RequestBody AddRoleDto roleDto){
		return roleService.addRole(roleDto);
	}
	//修改角色
	//信息回显
	@GetMapping("/{id}")
	public ResponseResult getRoleInfo(@PathVariable Long id){
		RoleVo roleVo = roleService.getRoleInfo(id);
		return ResponseResult.okResult(roleVo);
	}
	//修改提交
	@PutMapping("")
	public ResponseResult updateRoleInfo(@RequestBody UpdateRoleDto updateRoleDto){
		return roleService.updateRoleInfo(updateRoleDto);
	}
	//删除角色
	@DeleteMapping("/{id}")
	public ResponseResult deleteRole(@PathVariable Long id){
		return roleService.deleteRole(id);
	}
	//查询所有状态正常的角色
	@GetMapping("/listAllRole")
	public ResponseResult listAllRole(){
		List<Role> roles = roleService.listAllRole();
		return ResponseResult.okResult(roles);
	}

}
