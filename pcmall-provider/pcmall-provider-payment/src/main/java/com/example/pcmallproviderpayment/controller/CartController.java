package com.example.pcmallproviderpayment.controller;

import com.example.pcmallproviderpayment.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    public CartController() {
        log.debug("创建Controller对象：CartController");
    }
}
