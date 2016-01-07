package com.chakannom.demo.exception.exception.controlleradvice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chakannom.demo.exception.exception.DemoException;
import com.chakannom.demo.exception.exception.ErrorDetail;
import com.chakannom.demo.exception.exception.ErrorDetail.Error;

@ControllerAdvice
public class DemoControllerAdviceException {
	
	public DemoControllerAdviceException() { }

	@ExceptionHandler(DemoException.class)
	public @ResponseBody ErrorDetail demoExceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
		ErrorDetail errorDetail = new ErrorDetail();
		Error error = errorDetail.new Error();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exception.getLocalizedMessage());
		error.setRequestUrl(request.getRequestURL().toString());
		errorDetail.setError(error);
		return errorDetail;
	}
}
