package com.example.pcmalluserservice.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.utils.ImageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "验证码接口")
public class CaptchaController {

    private final HttpSession session;

    @Operation(summary = "获取验证码图片")
    @GetMapping("/api/captcha.jpg")
    public ResponseResult<String> getCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(135, 50, 5, 30);
        String code = captcha.getCode();//获取验证码字符串
        session.setAttribute("captchaCode", code);//验证码字符串放入到session
        log.debug("验证码是：{}", code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        captcha.write(out);
        return ResponseResult.ok(ImageUtils.encodeImageString(out.toByteArray()));
    }
}
