package com.muzike.utils;

import com.muzike.domain.entity.Menu;
import com.muzike.domain.vo.MenuTreeVo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SystemConvert {

	//构建菜单树
	public static List<MenuTreeVo> buildMenuSelectTree(List<Menu> menus){
		//类转化并赋值
		List<MenuTreeVo> menuTreeVos = menus.stream()
				.map(m -> new MenuTreeVo(m.getId(), m.getMenuName(), m.getParentId(), null))
				.collect(Collectors.toList());
		//为子菜单赋值
		List<MenuTreeVo> options = menuTreeVos.stream()
				//找到根节点（根菜单）
				.filter(o -> o.getParentId().equals(0L))
				//从根菜单开始，设置子菜单
				.map(o -> o.setChildren(getChildrenList(menuTreeVos, o)))
				.collect(Collectors.toList());
		return options;
	}


	/**
	 * 找出当前根节点 option的所有子菜单
	 * @param list  菜单集合
	 * @param option 根节点
	 * @return 菜单合集
	 */
	public static List<MenuTreeVo> getChildrenList(List<MenuTreeVo> list,MenuTreeVo option){
		List<MenuTreeVo> options = list.stream()
				//过滤出父菜单id==当前根节点option的菜单
				.filter(o -> Objects.equals(o.getParentId(), option.getId()))
				//递归，找出当前菜单的所有子菜单
				.map(o -> o.setChildren(getChildrenList(list, o)))
				.collect(Collectors.toList());
		return options;
	}
}
