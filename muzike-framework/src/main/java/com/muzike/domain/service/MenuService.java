package com.muzike.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.MenuDto;
import com.muzike.domain.entity.Menu;
import com.muzike.domain.vo.MenuVo;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-11-20 10:27:26
 */
public interface MenuService extends IService<Menu> {

	List<String> selectPermsByUserId(Long id);

	List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

	List<Menu> selectMenuList(MenuDto menuDto);

	ResponseResult addMenu(Menu menu);

	ResponseResult selectMenuById(Long id);

	ResponseResult updateMenu(Menu menu);

	ResponseResult deleteMenu(Long id);

	ResponseResult treeSelect();

	ResponseResult getRoleMenuTree(Long id);
}

