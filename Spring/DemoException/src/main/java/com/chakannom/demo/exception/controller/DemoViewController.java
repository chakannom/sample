package com.chakannom.demo.exception.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoViewController {
	
	@RequestMapping("/")
	public String getDemo() {
		return "index";
	}
}
