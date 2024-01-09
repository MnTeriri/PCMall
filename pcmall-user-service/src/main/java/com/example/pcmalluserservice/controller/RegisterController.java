package com.example.pcmalluserservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RegisterController {
    @RequestMapping("/api/register")
    public String register() {
        return "这是登录接口";
    }
}
