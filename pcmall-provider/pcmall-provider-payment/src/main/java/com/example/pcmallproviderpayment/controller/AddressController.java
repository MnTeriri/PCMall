package com.example.pcmallproviderpayment.controller;

import com.example.pcmallcommon.model.Address;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallproviderpayment.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    public AddressController() {
        log.debug("创建Controller对象：AddressController");
    }

    @PostMapping("/searchAddressList")
    public ResponseResult<List<Address>> searchAddressList(String uid) {
        return ResponseResult.ok(addressService.searchAddressList(uid));
    }
}
