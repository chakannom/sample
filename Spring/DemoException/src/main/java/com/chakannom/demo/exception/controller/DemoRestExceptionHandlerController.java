package com.chakannom.demo.exception.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chakannom.demo.exception.domain.DemoDTO;
import com.chakannom.demo.exception.exception.DemoException;
import com.chakannom.demo.exception.exception.ErrorDetail;
import com.chakannom.demo.exception.exception.ErrorDetail.Error;

@RestController
@RequestMapping(value = "/ExceptionHandler")
public class DemoRestExceptionHandlerController {

	@RequestMapping(value = "", method = RequestMethod.GET)
	public DemoDTO getDemoExceptionHandler() throws Exception {
		DemoDTO demoDto = new DemoDTO();
		if (demoDto.getId() == "Demo ID")
			throw new DemoException("ExceptionHandler");

		return demoDto;
	}
	
	@ExceptionHandler(DemoException.class)
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
