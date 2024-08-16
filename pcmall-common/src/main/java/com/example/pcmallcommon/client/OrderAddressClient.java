package com.example.pcmallcommon.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "orderAddressClient", value = "pcmall-provider-address")
public interface OrderAddressClient {

}
