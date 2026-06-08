package com.example.pcmallcommon.config;

import com.example.pcmallcommon.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration
public class HttpClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public AddressClient addressClient(@LoadBalanced RestClient.Builder builder) {
        return createClient(builder, "http://pcmall-provider-address", AddressClient.class);
    }

    @Bean
    public BrandClient brandClient(@LoadBalanced RestClient.Builder builder) {
        return createClient(builder, "http://pcmall-provider-brand", BrandClient.class);
    }

    @Bean
    public CartClient cartClient(@LoadBalanced RestClient.Builder builder) {
        return createClient(builder, "http://pcmall-provider-cart", CartClient.class);
    }

    @Bean
    public CategoryClient categoryClient(@LoadBalanced RestClient.Builder builder) {
        return createClient(builder, "http://pcmall-provider-category", CategoryClient.class);
    }

    @Bean
    public GoodsClient goodsClient(@LoadBalanced RestClient.Builder builder) {
        return createClient(builder, "http://pcmall-provider-goods", GoodsClient.class);
    }

    @Bean
    public OrderClient orderClient(@LoadBalanced RestClient.Builder builder) {
        return createClient(builder, "http://pcmall-provider-order", OrderClient.class);
    }

    @Bean
    public StorageClient storageClient(@LoadBalanced RestClient.Builder builder) {
        return createClient(builder, "http://pcmall-provider-storage", StorageClient.class);
    }

    @Bean
    public UserClient userClient(@LoadBalanced RestClient.Builder builder) {
        return createClient(builder, "http://pcmall-provider-user", UserClient.class);
    }

    private <T> T createClient(RestClient.Builder builder, String serviceUrl, Class<T> clientType) {
        RestClient restClient = builder
                // 指定服务提供方的应用名称（必须与 Provider 的 spring.application.name 一致）
                .baseUrl(serviceUrl)
                .build();

        // 创建 HttpServiceProxyFactory 工厂，用于生成接口代理对象
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(clientType);
    }
}
