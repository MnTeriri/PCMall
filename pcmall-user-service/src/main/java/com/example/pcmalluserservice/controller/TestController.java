package com.example.pcmalluserservice.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Parameters({
            @Parameter(name = "token", description = "登录token", required = true, in = ParameterIn.HEADER),
    })
    @GetMapping("/api/test")
    @PreAuthorize("hasRole('TTTTT')")
    public String test() {
        return "akshfkjahsfjasf";
    }
}
