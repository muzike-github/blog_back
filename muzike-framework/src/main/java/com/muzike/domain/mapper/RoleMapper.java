package com.muzike.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muzike.domain.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-20 10:34:35
 */
@Component
public interface RoleMapper extends BaseMapper<Role> {

	List<String> getRoleKeyByUserId(Long id);
	List<Long> selectRoleIdByUserId(Long userId);
}

