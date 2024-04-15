package com.example.pcmallconsumeradmin.handler;

import com.alibaba.fastjson2.JSON;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallcommon.response.ResponseResult;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    public GlobalExceptionHandler() {
        log.debug("创建全局异常处理对象：GlobalExceptionHandler");
    }

    //处理自定义异常
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> handlerSystemException(SystemException exception) {
        log.error("发生自定义SystemException异常：{}", exception.getResponseStatus());
        return ResponseResult.error(exception.getResponseStatus());
    }

    //处理权限异常
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseResult<String> handlerAccessDeniedException(AccessDeniedException e) {
        log.error("发生AccessDeniedException异常：{}", e.getMessage());
        log.error("Class：{}", e.getClass());
        ResponseResult<String> message = ResponseResult.error(ResponseCode.FORBIDDEN_ERROR);
        log.error(message.toString());
        return message;
    }

    //处理AuthenticationException内部其他异常
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult<String> handlerAuthenticationException(AuthenticationException e) {
        log.error("发生AuthenticationException异常：{}", e.getMessage());
        log.error("Class：{}", e.getClass());
        ResponseResult<String> message = ResponseResult.error(ResponseCode.AUTHORIZED_ERROR);
        log.error(message.toString());
        return message;
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> handlerSystemException(FeignException.BadRequest exception) {
        log.error("发生FeignException.BadRequest异常：{}", exception.getMessage());
        ByteBuffer byteBuffer = exception.responseBody().get();
        Charset charset = StandardCharsets.UTF_8;
        String json = charset.decode(byteBuffer).toString();
        return JSON.parseObject(json, ResponseResult.class);
    }

//    //处理Exception异常
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseResult<String> handlerException(Exception e) {
//        log.error("发生Exception异常：{}", e.getMessage());
//        log.error("Class：{}", e.getClass());
//        ResponseResult<String> message = ResponseResult.error(ResponseCode.INTERNAL_SERVER_ERROR);
//        log.error(message.toString());
//        return message;
//    }
}
