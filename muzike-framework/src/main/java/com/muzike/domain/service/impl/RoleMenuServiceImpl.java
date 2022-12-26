package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.domain.entity.RoleMenu;
import com.muzike.domain.mapper.RoleMenuMapper;
import com.muzike.domain.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(SysRoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-12-20 11:19:19
 */
@Service("RoleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

