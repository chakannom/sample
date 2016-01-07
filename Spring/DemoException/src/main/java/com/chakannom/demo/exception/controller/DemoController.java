package com.chakannom.demo.exception.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chakannom.demo.exception.domain.DemoDTO;

@RestController
public class DemoController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public DemoDTO getDemo() throws Exception {
		DemoDTO demoDto = new DemoDTO();
		if (demoDto.getId() == "Demo ID")
			throw new Exception();
		
		return demoDto;
	}
	
}
