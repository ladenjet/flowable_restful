package com.genpact.flowable.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.genpact.flowable.entity.Result;

/**
 * @author sxia
 * @Description: TODO(异常处理器)
 * @date 2017-6-23 15:07
 */
@RestControllerAdvice
public class FlowableExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 自定义异常
	 */
	@ExceptionHandler(FlowableException.class)
	public Result handleAppException(FlowableException e){
		logger.error(e.getMessage(), e);
		return Result.error(e.getMessage());
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Result handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return Result.error("数据库中已存在该记录");
	}
	
	@ExceptionHandler(MultipartException.class)
	public Result handleMultipartException(MultipartException e){
		logger.error(e.getMessage(), e);
		return Result.error("文件大小超过限制");
	}

//	@ExceptionHandler(AuthorizationException.class)
//	public Result handleAuthorizationException(AuthorizationException e){
//		logger.error(e.getMessage(), e);
//		return Result.error("没有权限，请联系管理员授权");
//	}
//	
//	@ExceptionHandler(UnauthorizedException.class)
//	public Result handleAuthorizationException(UnauthorizedException e){
//		logger.error(e.getMessage(), e);
//		return Result.error("没有权限，请联系管理员授权");
//	}

	@ExceptionHandler(Exception.class)
	public Result handleException(Exception e){
		logger.error(e.getMessage(), e);
		return Result.error();
	}

}
