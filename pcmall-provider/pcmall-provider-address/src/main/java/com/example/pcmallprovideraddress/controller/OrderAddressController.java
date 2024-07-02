package com.example.pcmallprovideraddress.controller;

import com.example.pcmallcommon.model.Address;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovideraddress.service.IOrderAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orderAddress")
@Tag(name = "orderAddress参数")
public class OrderAddressController {
    @Autowired
    private IOrderAddressService orderAddressService;

    public OrderAddressController() {
        log.debug("创建Controller对象：{}", this);
    }

    @PostMapping("/searchOrderAddress")
    @Operation(summary = "查询订单地址")
    public ResponseResult<Address> searchOrderAddress(String oid) {
        return ResponseResult.ok(orderAddressService.searchOrderAddress(oid));
    }

    @PostMapping("/addOrderAddress")
    @Operation(summary = "添加订单地址")
    public ResponseResult<String> addOrderAddress(String oid, Address address) {
        orderAddressService.addOrderAddress(oid, address);
        return ResponseResult.ok("订单地址添加成功！");
    }

    @PostMapping("/deleteOrderAddress")
    @Operation(summary = "删除订单地址")
    public ResponseResult<String> deleteOrderAddress(String oid) {
        orderAddressService.deleteOrderAddress(oid);
        return ResponseResult.ok("订单地址删除成功！");
    }
}
