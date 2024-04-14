package com.example.pcmallproviderpayment.controller;

import com.example.pcmallcommon.model.Address;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallproviderpayment.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/searchDefaultAddress")
    public ResponseResult<Address> searchDefaultAddress(String uid) {
        return ResponseResult.ok(addressService.searchDefaultAddress(uid));
    }

    @PostMapping("/addAddress")
    public ResponseResult<String> addAddress(@RequestBody Address address) {
        addressService.addAddress(address);
        return ResponseResult.ok("地址添加成功！");
    }

    @PostMapping("/updateAddress")
    public ResponseResult<String> updateAddress(@RequestBody Address address) {
        addressService.updateAddress(address);
        return ResponseResult.ok("地址更新成功！");
    }

    @PostMapping("/deleteAddress")
    public ResponseResult<String> deleteAddress(@RequestBody Address address) {
        addressService.deleteAddress(address);
        return ResponseResult.ok("地址删除成功！");
    }

}
