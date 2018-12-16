package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Check {
@RequestMapping("/check.htm")
public ModelAndView check()
{
	ModelAndView mv = new ModelAndView();
	mv.setViewName("index.jsp");
	return mv;
}
}
