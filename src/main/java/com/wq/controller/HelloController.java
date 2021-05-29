package com.wq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
@RequestMapping("/Test")
public class HelloController {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("/thymeleafTest")
    public String thymeleafTest(Model model){
        model.addAttribute("msg","hello thymeleaf");
        model.addAttribute("utext","<h1>hello thymeleaf</h1>");
        model.addAttribute("list", Arrays.asList("num1","num2"));
        return "thymeleafTest";
    }
}
