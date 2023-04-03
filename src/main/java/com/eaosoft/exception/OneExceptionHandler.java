package com.eaosoft.exception;

import com.alibaba.fastjson.JSONObject;
import com.eaosoft.railway.utils.RespValue;
import com.eaosoft.railway.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Exception handler
 *
 * @author ZhouWenTao
 */
@RestControllerAdvice
public class OneExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(OneException.class)
	public RespValue handleRRException(OneException e){
		RespValue respValue=new RespValue();
		respValue.setCode(e.getCode());
		respValue.setInfo(e.getMessage());
		respValue.setData(null);
		return respValue;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public RespValue handlerNoFoundException(Exception e) {
		RespValue respValue=new RespValue();
		logger.error(e.getMessage(), e);
		Result<Object> error = Result.error(404, "url not found");
		respValue.setCode(500);
		respValue.setInfo("url not found");
		respValue.setData(null);
		return respValue;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public RespValue handleDuplicateKeyException(DuplicateKeyException e){
		RespValue respValue=new RespValue();
		logger.error(e.getMessage(), e);
		respValue.setCode(500);
		respValue.setInfo("data already exists");
		respValue.setData(null);
		return respValue;
	}


	@ExceptionHandler(Exception.class)
	public RespValue handleException(Exception e){
		logger.error(e.getMessage(), e);
		Result<Object> error = Result.error(e.getMessage());
		RespValue respValue=new RespValue();
		respValue.setCode(500);
		respValue.setInfo(e.getMessage());
		respValue.setData(null);
		return respValue;
	}
}
