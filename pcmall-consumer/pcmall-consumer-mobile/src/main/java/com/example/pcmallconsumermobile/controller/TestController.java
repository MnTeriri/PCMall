package com.example.pcmallconsumermobile.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/TestController")
public class TestController {
    @Autowired
    private HttpServletRequest request;
    @GetMapping("/test")
    public String test() {
        System.out.println(request.getHeader("token"));
        return "qrfasf";
    }
}
