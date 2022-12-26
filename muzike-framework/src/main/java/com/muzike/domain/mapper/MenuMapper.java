package com.muzike.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muzike.domain.entity.Menu;
import com.muzike.domain.vo.MenuVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-20 10:29:20
 */
@Component
public interface MenuMapper extends BaseMapper<Menu> {

	List<String> getPermsByUserId(Long id);

	List<MenuVo> selectAllRouterMenu();

	List<MenuVo> selectRouterMenuTreeByUserId();

	List<Long> selectMenuListByRoleId(Long id);
}

