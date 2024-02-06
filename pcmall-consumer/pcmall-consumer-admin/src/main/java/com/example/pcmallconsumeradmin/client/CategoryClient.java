package com.example.pcmallconsumeradmin.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "categoryClient", value = "pcmall-provider-goods")
public interface CategoryClient {
}
