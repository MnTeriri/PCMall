package com.example.pcmallconsumeradmin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/TestController")
@PreAuthorize("hasRole('ADMIN')")
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "qrfasf";
    }
}
