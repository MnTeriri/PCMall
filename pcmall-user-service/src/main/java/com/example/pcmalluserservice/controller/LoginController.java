package com.example.pcmalluserservice.controller;

import com.alibaba.fastjson2.JSON;
import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.response.ResponseStatus;
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

    @PostMapping(value = "/api/login")
    public ResponseResult<User> login(String uid, String password, String code) {
        //判定验证码
//        String captchaCode = (String) session.getAttribute("captchaCode");
//        if (!captchaCode.equals(code)) {
//            return ResponseResult.error(ResponseStatus.CAPTCHA_ERROR);
//        }
        return userService.login(uid, password);
    }
}
