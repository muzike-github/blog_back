package com.muzike.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeSelectVo {
	//当前角色已具有的菜单权限所对应的id
	private List<Long> checkedKeys;
	//所有菜单
	private List<MenuTreeVo> menus;
}
