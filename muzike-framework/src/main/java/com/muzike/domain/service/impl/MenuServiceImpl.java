package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.constants.SystemConstants;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.MenuDto;
import com.muzike.domain.entity.Menu;
import com.muzike.domain.mapper.MenuMapper;
import com.muzike.domain.service.MenuService;
import com.muzike.domain.vo.MenuTreeVo;
import com.muzike.domain.vo.MenuVo;
import com.muzike.domain.vo.RoleMenuTreeSelectVo;
import com.muzike.utils.SecurityUtil;
import com.muzike.utils.SystemConvert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-11-20 10:27:26
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

	//根据用户id查询权限
	@Override
	public List<String> selectPermsByUserId(Long id) {
		//如果用户是管理员，返回所有权限
		if(id == 1L){
			LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
			wrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
			wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
			List<Menu> menus = list(wrapper);
			List<String> perms = menus.stream()
					.map(Menu::getPerms)
					.collect(Collectors.toList());
			return perms;
		}
		//否则返回当前用户具有的权限
		return baseMapper.getPermsByUserId(id) ;
	}

	@Override
	public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
		MenuMapper menuMapper = getBaseMapper();
		//判断是否为管理员，是则返回所有符合要求的Menu
		List<MenuVo> menus = null;
		if(SecurityUtil.isAdmin()){
			menus = menuMapper.selectAllRouterMenu();
		}else{
			//不是则返回当前用户所具有的menu
			menus = menuMapper.selectRouterMenuTreeByUserId();
		}
		//构建tree
		//先找出第一层的菜单，然后去找他们的子菜单设置到children属性中（递归）
		List<MenuVo> menuTree = BuildMenuTree(menus,0L);
		return menuTree;
	}

	//根据关键字查询菜单
	@Override
	public List<Menu> selectMenuList(MenuDto menuDto) {
		LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(Objects.nonNull(menuDto.getMenuName()),Menu::getMenuName,menuDto.getMenuName());
		wrapper.like(Objects.nonNull(menuDto.getStatus()),Menu::getStatus,menuDto.getStatus());
		List<Menu> menus = list(wrapper);
		return menus;
	}

	//增加菜单
	@Override
	public ResponseResult addMenu(Menu menu) {
		save(menu);
		return ResponseResult.okResult();
	}

	//根据id查询菜单信息（回显）
	@Override
	public ResponseResult selectMenuById(Long id) {
		Menu menu = getById(id);
		return ResponseResult.okResult(menu);
	}

	@Override
	public ResponseResult updateMenu(Menu menu) {
		updateById(menu);
		return ResponseResult.okResult();
	}

	//删除菜单
	@Override
	public ResponseResult deleteMenu(Long id) {
		removeById(id);
		return ResponseResult.okResult();
	}

	//查询菜单树
	@Override
	public ResponseResult treeSelect() {
		List<Menu> menus = selectMenuList(new MenuDto());
		List<MenuTreeVo> menuTreeVos = SystemConvert.buildMenuSelectTree(menus);
		return ResponseResult.okResult(menuTreeVos);
	}

	//获取角色菜单树
	@Override
	public ResponseResult getRoleMenuTree(Long id) {
		//查询所有的菜单
		List<Menu> menus = selectMenuList(new MenuDto());
		//根据用户id查询用户具有的所有菜单的id
		List<Long> checkedKeys = getBaseMapper().selectMenuListByRoleId(id);
		//构建菜单树
		List<MenuTreeVo> menuTreeVos = SystemConvert.buildMenuSelectTree(menus);
		RoleMenuTreeSelectVo roleMenuTreeSelectVo = new RoleMenuTreeSelectVo(checkedKeys, menuTreeVos);
		return ResponseResult.okResult(roleMenuTreeSelectVo);

	}


	private List<MenuVo> BuildMenuTree(List<MenuVo> menus, Long parentId) {
		List<MenuVo> menuTree = menus.stream()
				.filter(menuVo -> menuVo.getParentId().equals(parentId))
				.map(menuVo -> menuVo.setChildren(getChildrenMenu(menuVo, menus)))
				.collect(Collectors.toList());

		return menuTree;
	}

	/**
	 * 从菜单中找到对应菜单的所有子菜单
	 * @param menuVo 要查询的菜单
	 * @param menus 菜单合集
	 * @return menuVo的子菜单
	 */
	private List<MenuVo> getChildrenMenu(MenuVo menuVo, List<MenuVo> menus) {
		List<MenuVo> children = menus.stream()
				.filter(m -> m.getParentId().equals(menuVo.getId()))
//				.map(m -> m.setChildren(getChildrenMenu(m,menus))) 递归操作，逐级查询子菜单
				.collect(Collectors.toList());
		return children;
	}
}

