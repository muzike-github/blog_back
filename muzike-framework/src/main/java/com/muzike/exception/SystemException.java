package com.muzike.exception;

import com.muzike.domain.enums.AppHttpCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemException extends RuntimeException{
	private int code;
	private String msg;

	public SystemException(AppHttpCodeEnum httpCodeEnum){
		super(httpCodeEnum.getMsg());
		this.code=httpCodeEnum.getCode();
		this.msg=httpCodeEnum.getMsg();
	}
}
