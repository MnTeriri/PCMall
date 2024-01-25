package com.example.pcmalluserservice.controller;

import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmalluserservice.service.IUserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private HttpSession session;
    @Autowired
    private IUserService userService;

//    @RequestMapping(value = "/api/login", method = {RequestMethod.GET, RequestMethod.POST})
//    public String login() {
//        return "这是登录接口,token:" + userService.login("000000000", "123456");
//    }

    @RequestMapping(value = "/api/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseResult<User> login(String uid, String password) {
        //判定验证码

        return userService.login(uid, password);
    }
}
