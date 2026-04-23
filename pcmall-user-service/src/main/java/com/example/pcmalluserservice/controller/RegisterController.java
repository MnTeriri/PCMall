package com.example.pcmalluserservice.controller;

import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmalluserservice.service.IUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "注册接口")
public class RegisterController {

    private final HttpSession session;

    private final IUserService userService;

    @Parameters({
            @Parameter(name = "uid", description = "用户ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "password", description = "密码", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "code", description = "验证码", required = true, in = ParameterIn.QUERY),
    })
    @PostMapping("/api/register")
    public ResponseResult<String> register(String uid, String password, String code) {
        //判定验证码
//        String captchaCode = (String) session.getAttribute("captchaCode");
//        if (!captchaCode.equals(code)) {
//            throw new SystemException(ResponseCode.CAPTCHA_ERROR);
//        }
        return userService.register(uid, password);
    }
}
