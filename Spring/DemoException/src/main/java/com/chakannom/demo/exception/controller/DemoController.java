package com.chakannom.demo.exception.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chakannom.demo.exception.domain.DemoDTO;
import com.chakannom.demo.exception.exception.DemoExceptionHandlerException;
import com.chakannom.demo.exception.exception.ErrorDetail;
import com.chakannom.demo.exception.exception.ErrorDetail.Error;

@RestController
public class DemoController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public DemoDTO getDemo() throws Exception {
		DemoDTO demoDto = new DemoDTO();
		if (demoDto.getId() == "Demo ID")
			throw new DemoExceptionHandlerException("ID");
		
		return demoDto;
	}
	
	@ExceptionHandler(DemoExceptionHandlerException.class)
	public ErrorDetail demoExceptionHandler(HttpServletRequest request, Exception exception) {
		ErrorDetail errorDetail = new ErrorDetail();
		Error error = errorDetail.new Error();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exception.getLocalizedMessage());
		error.setRequestUrl(request.getRequestURL().toString());
		errorDetail.setError(error);
		return errorDetail;
	}
	
}
