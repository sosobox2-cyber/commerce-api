package com.cware.api.swagger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.framework.core.basic.AbstractController;

@Controller("com.cware.api.swagger.controller")
@RequestMapping(value = "/swagger")
public class SwaggerController extends AbstractController {
	
	@RequestMapping(value = "")
	public String swaggerIndex() {
		return "redirect:/swagger-ui.html";
	}
}