package com.muzike.aspect;

import com.alibaba.fastjson.JSON;
import com.muzike.annotation.SystemLog;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 切面类
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

	@Pointcut("@annotation(com.muzike.annotation.SystemLog)")
	public void pointCut(){

	}

	@Around("pointCut()")
	public Object printLog(ProceedingJoinPoint pjp) throws Throwable {
		Object ret=null;
		//此处不用try catch处理是因为异常被catch捕获后，我们自定义的异常处理
		//就捕获不到异常了，所以直接将异常向上抛出
		try {
			handleBefore(pjp);
			ret = pjp.proceed();
			//将目标方法的返回值传入handleAfter进行处理
			handleAfter(ret);
		} finally {
			log.info("=========end========="+System.lineSeparator());
		}
		return ret;
	}
	//方法调用前
	private void handleBefore(ProceedingJoinPoint pjp) {
		log.info("===============start===============");
		//打印请求url
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		//打印url
		log.info("url:{}",request.getRequestURL());
		//打印业务名称
		log.info("BusinessName:{}",getBusinessName(pjp));
		//打印请求方式
		log.info("HTTP method:{}",request.getMethod());
		//打印类名
		log.info("ClassMethod:{}.{}",pjp.getSignature().getDeclaringTypeName()
				,((MethodSignature)pjp.getSignature()).getName());
		//打印请求ip
		log.info("IP:{}",request.getRemoteHost());
		//打印请求参数
		log.info("Request args:{}", JSON.toJSONString(pjp.getArgs()));
	}

	private String getBusinessName(ProceedingJoinPoint pjp) {
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
		return systemLog.businessName();
	}

	//调用完方法后打印响应数据
	private void handleAfter(Object ret) {
		log.info("response:{}", JSON.toJSONString(ret));
	}


}

