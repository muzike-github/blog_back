package com.muzike.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.muzike.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-16 15:35:17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

