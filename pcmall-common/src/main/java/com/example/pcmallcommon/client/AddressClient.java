package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.Address;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "addressClient", value = "pcmall-provider-address")
public interface AddressClient {
    @PostMapping("/address/searchAddressById")
    ResponseResult<Address> searchAddressById(@RequestParam("id") Integer id);

    @PostMapping("/address/searchAddressList")
    ResponseResult<List<Address>> searchAddressList(@RequestParam("uid") String uid);

    @PostMapping("/address/searchDefaultAddress")
    ResponseResult<Address> searchDefaultAddress(@RequestParam("uid") String uid);

    @PostMapping("/address/addAddress")
    ResponseResult<String> addAddress(@RequestBody Address address);

    @PostMapping("/address/updateAddress")
    ResponseResult<String> updateAddress(@RequestBody Address address);

    @PostMapping("/address/deleteAddress")
    ResponseResult<String> deleteAddress(@RequestParam("id") Integer id);
}
