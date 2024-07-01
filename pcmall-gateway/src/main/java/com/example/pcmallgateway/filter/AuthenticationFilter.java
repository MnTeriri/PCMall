package com.example.pcmallgateway.filter;

import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("进入AuthenticationFilter");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String path = request.getURI().getPath();
        log.debug("访问的链接是：{}", path);
        if ("/api/login".equals(path)
                || "/api/register".equals(path)
                || "/api/captcha.jpg".equals(path)
                || path.startsWith("/api/image")
                || path.startsWith("/api/mobile/goods")
                || path.startsWith("/api/mobile/category")
                || path.startsWith("/api/mobile/brand")) {
            log.debug("是{}，放行", path);
            return chain.filter(exchange);
        }
        List<String> token = request.getHeaders().get("token");
        if (token == null) {
            log.error(ResponseCode.NO_TOKEN_ERROR.toString());
            throw new SystemException(ResponseCode.NO_TOKEN_ERROR);
        }
        log.debug("token:{}", request.getHeaders().get("token"));
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
