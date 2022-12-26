package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddRoleDto;
import com.muzike.domain.dto.RoleStatusDto;
import com.muzike.domain.dto.UpdateRoleDto;
import com.muzike.domain.entity.Role;
import com.muzike.domain.entity.RoleMenu;
import com.muzike.domain.mapper.RoleMapper;
import com.muzike.domain.mapper.RoleMenuMapper;
import com.muzike.domain.service.RoleMenuService;
import com.muzike.domain.service.RoleService;
import com.muzike.domain.vo.PageVo;
import com.muzike.domain.vo.RoleVo;
import com.muzike.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-11-20 10:34:35
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	@Autowired
	private RoleMenuService roleMenuService;
	//根据用户id查询用户的角色
	@Override
	public List<String> selectRoleKeyByUserId(Long id) {
		//如果是管理员，返回admin关键字
		if(id==1L){
			ArrayList<String> roleKeys = new ArrayList<>();
			roleKeys.add("admin");
			return roleKeys;
		}
		//否则查询用户所具有的角色关键字
		return getBaseMapper().getRoleKeyByUserId(id) ;
	}

	@Override
	public ResponseResult listUser(Integer pageNum, Integer pageSize, String roleName, String status) {
		LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(Objects.nonNull(roleName),Role::getRoleName,roleName);
		wrapper.like(Objects.nonNull(status),Role::getStatus,status);
		Page<Role> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page(page,wrapper);
		PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	@Override
	public ResponseResult changeStatus(RoleStatusDto roleStatusDto) {
		Role role = getById(roleStatusDto.getRoleId());
		role.setStatus(roleStatusDto.getStatus());
		updateById(role);
		return ResponseResult.okResult();
	}

	@Override
	@Transactional
	public ResponseResult addRole(AddRoleDto roleDto) {
		Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
		save(role);
		if(roleDto.getMenuIds()!=null && !roleDto.getMenuIds().isEmpty()){
			//将角色和菜单的对应关系也存储起来
			List<Long> menuIds = roleDto.getMenuIds();
			List<RoleMenu> roleMenuList = menuIds.stream()
					.map(menuId -> new RoleMenu(role.getId(), menuId))
					.collect(Collectors.toList());
			roleMenuService.saveBatch(roleMenuList);
		}
		return ResponseResult.okResult();
	}
	//根据id获取对应角色的信息
	@Override
	public RoleVo getRoleInfo(Long id) {
		Role role = getById(id);
		RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
		return roleVo;
	}
	//更新角色的权限信息
	@Override
	public ResponseResult updateRoleInfo(UpdateRoleDto roleDto) {
		//更新角色信息
		Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
		updateById(role);
		//更新角色对应的菜单权限
		//先删除用户对应的所有菜单，再重新插入权限
		/**
		 * 此处，直接调用方法删除是逻辑删除(导致下方插入报错)，使用lambda表达式删除是物理删除
		 */
		LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(RoleMenu::getRoleId,role.getId());
		roleMenuService.remove(wrapper);
		//roleMenuService.removeById(role.getId());
		//重新插入
		List<RoleMenu> roleMenuList = roleDto.getMenuIds().stream()
				.map(menuId -> new RoleMenu(role.getId(), menuId))
				.collect(Collectors.toList());
		roleMenuService.saveBatch(roleMenuList);
		return ResponseResult.okResult();
	}
	//删除角色
	@Override
	public ResponseResult deleteRole(Long id) {
		removeById(id);
		return ResponseResult.okResult();
	}
	//查询所有状态正常的角色
	@Override
	public List<Role> listAllRole() {
		LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Role::getStatus,"0");
		List<Role> roles = list(wrapper);
		return roles;
	}

}

