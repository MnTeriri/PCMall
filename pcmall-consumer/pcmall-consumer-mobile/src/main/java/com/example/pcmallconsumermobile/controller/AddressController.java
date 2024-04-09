package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.model.Address;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumermobile.client.AddressClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/address")
@PreAuthorize("hasRole('USER')")
public class AddressController {
    @Autowired
    private AddressClient addressClient;

    public AddressController() {
        log.debug("创建AddressController对象：AddressController");
    }

    @PostMapping("/searchAddressList")
    public ResponseResult<List<Address>> searchAddressList(String uid) {
        return addressClient.searchAddressList(uid);
    }

    @PostMapping("/addAddress")
    public ResponseResult<String> addAddress(Address address) {
        return null;
    }

    @PostMapping("/updateAddress")
    public ResponseResult<String> updateAddress(Address address) {
        return null;
    }

    @PostMapping("/deleteAddress")
    public ResponseResult<String> deleteAddress(Address address) {
        return null;
    }
}
