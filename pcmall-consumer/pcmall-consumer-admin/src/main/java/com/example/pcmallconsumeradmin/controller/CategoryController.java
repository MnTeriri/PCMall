package com.example.pcmallconsumeradmin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/category")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {
    public CategoryController() {
        log.debug("创建Controller对象：CategoryController");
    }
}
