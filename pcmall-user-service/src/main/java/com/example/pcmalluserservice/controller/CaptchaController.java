package com.example.pcmalluserservice.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.example.pcmallcommon.utils.ImageUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@Slf4j
@RestController
public class CaptchaController {
    @Autowired
    private HttpSession session;

    @RequestMapping("/api/captcha.jpg")
    public String getCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 40, 5, 30);
        String code = captcha.getCode();//获取验证码字符串
        session.setAttribute("captchaCode", code);//验证码字符串放入到session
        log.debug("验证码是：{}", code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        captcha.write(out);
        return ImageUtils.encodeImageString(out.toByteArray());
    }
}
