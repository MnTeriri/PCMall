package com.example.pcmalluserservice.controller;

import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.response.ResponseStatus;
import com.example.pcmalluserservice.service.IUserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RegisterController {
    @Autowired
    private HttpSession session;
    @Autowired
    private IUserService userService;

    @RequestMapping("/api/register")
    public ResponseResult<String> register(String uid, String password, String code) {
        //判定验证码
//        String captchaCode = (String) session.getAttribute("captchaCode");
//        if (!captchaCode.equals(code)) {
//            throw new SystemException(ResponseStatus.CAPTCHA_ERROR);
//        }
        return userService.register(uid, password);
    }
}
