package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.dto.Address;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/address")
public interface AddressClient {
    @PostExchange("/searchAddressById")
    ResponseResult<Address> searchAddressById(@RequestParam("id") Integer id);

    @PostExchange("/searchAddressList")
    ResponseResult<List<Address>> searchAddressList(@RequestParam("uid") String uid);

    @PostExchange("/searchDefaultAddress")
    ResponseResult<Address> searchDefaultAddress(@RequestParam("uid") String uid);

    @PostExchange("/addAddress")
    ResponseResult<String> addAddress(@RequestBody Address address);

    @PostExchange("/updateAddress")
    ResponseResult<String> updateAddress(@RequestBody Address address);

    @PostExchange("/deleteAddress")
    ResponseResult<String> deleteAddress(@RequestParam("id") Integer id);
}
