package com.example.pcmalluserservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/api/test")
    @PreAuthorize("hasRole('TTTTT')")
    public String test() {
        return "akshfkjahsfjasf";
    }
}
