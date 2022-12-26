package com.muzike.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddRoleDto;
import com.muzike.domain.dto.RoleStatusDto;
import com.muzike.domain.dto.UpdateRoleDto;
import com.muzike.domain.entity.Role;
import com.muzike.domain.vo.RoleVo;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-11-20 10:34:35
 */

public interface RoleService extends IService<Role> {

	List<String> selectRoleKeyByUserId(Long id);

	ResponseResult listUser(Integer pageNum, Integer pageSize, String roleName, String status);

	ResponseResult changeStatus(RoleStatusDto roleStatusDto);

	ResponseResult addRole(AddRoleDto roleDto);

	RoleVo getRoleInfo(Long id);

	ResponseResult updateRoleInfo(UpdateRoleDto updateRoleDto);

	ResponseResult deleteRole(Long id);

	List<Role> listAllRole();
}

