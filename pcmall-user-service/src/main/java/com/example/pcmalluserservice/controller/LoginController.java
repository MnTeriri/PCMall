package com.example.pcmalluserservice.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private HttpSession session;

    @RequestMapping("/api/login")
    public String login() {
        return "这是登录接口" + session.getAttribute("captchaCode");
    }
}
