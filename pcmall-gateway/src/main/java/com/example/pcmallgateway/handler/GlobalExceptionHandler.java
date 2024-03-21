package com.example.pcmallgateway.handler;

import com.alibaba.fastjson2.JSON;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    public GlobalExceptionHandler() {
        log.debug("创建全局异常处理对象：GlobalExceptionHandler");
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ResponseResult<String> message = null;
        if (ex instanceof SystemException exception) {
            log.error("发生自定义SystemException异常：{}", exception.getResponseStatus());
            message = ResponseResult.error(exception.getResponseStatus());
        } else {
            log.error("发生Throwable异常：{}", ex.toString());
            log.error("Class：{}", ex.getClass());
            message = ResponseResult.error(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONBytes(message));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeAndFlushWith(Mono.just(ByteBufMono.just(dataBuffer)));
    }
}
