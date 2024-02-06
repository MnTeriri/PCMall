package com.example.pcmallconsumeradmin.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "brandClient", value = "pcmall-provider-goods")
public interface BrandClient {
}
