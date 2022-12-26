package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.MenuDto;
import com.muzike.domain.entity.Menu;
import com.muzike.domain.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	//查询所有的菜单
	@GetMapping("/list")
	public ResponseResult list(MenuDto menuDto){
		List<Menu> menus = menuService.selectMenuList(menuDto);
		return ResponseResult.okResult(menus);
	}
	//新增菜单
	@PostMapping("")
	public ResponseResult add(@RequestBody Menu menu){
		return menuService.addMenu(menu);
	}
	//修改菜单（先查询，后修改）
	@GetMapping("/{id}")
	public ResponseResult selectMenu(@PathVariable Long id){
		return menuService.selectMenuById(id);
	}
	@PutMapping("")
	public ResponseResult updateMenu(@RequestBody Menu menu){
		return menuService.updateMenu(menu);
	}
	//删除菜单
	@DeleteMapping("/{id}")
	public ResponseResult deleteMenu(@PathVariable Long id){
		return menuService.deleteMenu(id);
	}
	//展示树状菜单
	@GetMapping("/treeselect")
	public ResponseResult treeSelect(){
		return menuService.treeSelect();
	}
	//用于修改角色信息时回显对应角色已具有的菜单权限
	@GetMapping("/roleMenuTreeselect/{id}")
	public ResponseResult getRoleMenuTree(@PathVariable Long id){
		return menuService.getRoleMenuTree(id);
	}
}
