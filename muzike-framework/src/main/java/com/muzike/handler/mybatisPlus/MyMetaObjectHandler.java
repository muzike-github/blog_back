package com.muzike.handler.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.muzike.utils.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 配置mybatisPlus公共字段自动填充
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		Long userId=null;
		try {
			userId= SecurityUtil.getUserId();
		}catch (Exception e){
			e.printStackTrace();
			userId=-1L;//未从token中获取到表示这是注册过程中的填充
		}
		this.setFieldValByName("createTime",new Date(),metaObject);
		this.setFieldValByName("createBy",userId,metaObject);
		this.setFieldValByName("updateTime",new Date(),metaObject);
		this.setFieldValByName("updateBy",userId,metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.setFieldValByName("updateTime",new Date(),metaObject);
		this.setFieldValByName(" ",SecurityUtil.getUserId(),metaObject);
	}
}
