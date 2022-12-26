package com.muzike.handler.exception;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.enums.AppHttpCodeEnum;
import com.muzike.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/*
* 自定义异常
* */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(SystemException.class)
	public ResponseResult systemExceptionHandler(SystemException e){
		log.error("出现了异常!",e);
		return new ResponseResult(e.getCode(),e.getMsg());
	}
	@ExceptionHandler(Exception.class)
	public ResponseResult ExceptionHandler(Exception e){
		log.error("出现了异常!",e);
		return new ResponseResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
	}
}
