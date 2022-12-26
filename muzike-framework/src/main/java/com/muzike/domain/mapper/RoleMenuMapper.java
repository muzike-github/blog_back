package com.muzike.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muzike.domain.entity.RoleMenu;
import org.springframework.stereotype.Component;

/**
 * 角色和菜单关联表(SysRoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-20 11:19:19
 */
@Component
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}

