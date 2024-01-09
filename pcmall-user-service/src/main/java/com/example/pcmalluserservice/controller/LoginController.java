package com.example.pcmalluserservice.controller;

import com.example.pcmalluserservice.service.IUserService;
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
    @Autowired
    private IUserService userService;

    @RequestMapping("/api/login")
    public String login() {
        userService.login("12312", "qawhdalks");
        return "这是登录接口" + session.getAttribute("captchaCode");
    }
}
