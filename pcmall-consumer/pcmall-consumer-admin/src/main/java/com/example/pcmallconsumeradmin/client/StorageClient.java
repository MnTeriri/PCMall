package com.example.pcmallconsumeradmin.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "storageClient", value = "pcmall-provider-goods")
public interface StorageClient {

}
