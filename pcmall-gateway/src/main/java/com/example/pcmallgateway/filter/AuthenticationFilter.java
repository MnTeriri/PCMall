package com.example.pcmallgateway.filter;

import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.response.ResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
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
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("进入AuthenticationFilter");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String path = request.getURI().getPath();
        if ("/api/login".equals(path)
                || "/api/register".equals(path)
                || "/api/captcha.jpg".equals(path)
                || path.startsWith("/api/image")) {
            log.debug("是{}，放行", path);
            return chain.filter(exchange);
        }
        List<String> token = request.getHeaders().get("token");
        if (token == null) {
            ResponseResult<String> message = ResponseResult.error(ResponseStatus.NO_TOKEN_ERROR);
            log.error(message.toString());
            response.setStatusCode(HttpStatus.FORBIDDEN);
            DataBufferFactory bufferFactory = response.bufferFactory();
            ObjectMapper objectMapper = new ObjectMapper();
            DataBuffer wrap = bufferFactory.wrap(objectMapper.writeValueAsBytes(message));
            return response.writeWith(Mono.fromSupplier(() -> wrap));
        }
        log.debug("token:{}", request.getHeaders().get("token"));
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
