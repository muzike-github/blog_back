package com.muzike.domain.enums;


import org.apache.poi.ss.formula.functions.T;

public enum AppHttpCodeEnum {
	SUCCESS(200,"操作成功"),
	NEED_lOGIN(401,"需要登录"),
	NO_OPERATOR_AUTH(403,"没有操作权限"),
	SYSTEM_ERROR(500,"系统错误"),
	USERNAME_EXIST(501,"用户名已存在"),
	PHONENUMBER_EXIST(502,"电话已存在"),
	EMAIL_EXIST(503,"邮箱存在"),
	REQUIRE_USERNAME(504,"需要用户名"),
	LOGIN_ERROR(505,"用户名或密码错误"),
	COMMENT_NOT_NULL(506,"评论不能为空"),
	FILE_TYPE_ERROR(507,"只能上传png,jpg和jpeg文件"),
	USERNAME_NOTNULL(508,"用户名不能为空"),
	NICKNAME_NOTNULL(509,"昵称不能为空"),
	EMAIL_NOTNULL(509,"邮箱不能为空"),
	PASSWORD_NOTNULL(509,"密码不能为空"),
	PHONENUMBER_NOTNULL(510,"手机号不能为空")
	;
	int code;
	String msg;

	AppHttpCodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
