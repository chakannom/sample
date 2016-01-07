package com.chakannom.demo.exception.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chakannom.demo.exception.domain.DemoDTO;
import com.chakannom.demo.exception.exception.DemoException;

@RestController
@RequestMapping(value = "/ControllerAdvice")
public class DemoRestControllerAdviceController {
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public DemoDTO getDemoControllerAdvice() throws Exception {
		DemoDTO demoDto = new DemoDTO();
		if (demoDto.getId() == "Demo ID")
			throw new DemoException("ControllerAdvice");

		return demoDto;
	}
}
