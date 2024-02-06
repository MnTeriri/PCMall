package com.example.pcmallconsumeradmin.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "pcmall-provider-goods")
public interface CategoryClient {
}
