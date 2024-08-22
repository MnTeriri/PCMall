package com.example.pcmallprovideraddress.controller;

import com.example.pcmallcommon.model.Address;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovideraddress.service.IAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "address参数")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    public AddressController() {
        log.debug("创建Controller对象：{}", this);
    }

    @PostMapping("/searchAddressById")
    @Operation(summary = "查询地址信息")
    @Parameters({
            @Parameter(name = "id", description = "地址ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Address> searchAddressById(Integer id) {
        return ResponseResult.ok(addressService.searchAddressById(id));
    }

    @PostMapping("/searchAddressList")
    @Operation(summary = "查询所有地址")
    @Parameters({
            @Parameter(name = "uid", description = "用户UID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Address>> searchAddressList(String uid) {
        return ResponseResult.ok(addressService.searchAddressList(uid));
    }

    @PostMapping("/searchDefaultAddress")
    @Operation(summary = "查询默认地址")
    public ResponseResult<Address> searchDefaultAddress(String uid) {
        return ResponseResult.ok(addressService.searchDefaultAddress(uid));
    }

    @PostMapping("/addAddress")
    @Operation(summary = "添加地址")
    public ResponseResult<String> addAddress(@RequestBody Address address) {
        addressService.addAddress(address);
        return ResponseResult.ok("地址添加成功！");
    }

    @PostMapping("/updateAddress")
    @Operation(summary = "更新地址")
    public ResponseResult<String> updateAddress(@RequestBody Address address) {
        addressService.updateAddress(address);
        return ResponseResult.ok("地址更新成功！");
    }

    @PostMapping("/deleteAddress")
    @Operation(summary = "删除地址")
    public ResponseResult<String> deleteAddress(Integer id) {
        addressService.deleteAddress(id);
        return ResponseResult.ok("地址删除成功！");
    }

}
