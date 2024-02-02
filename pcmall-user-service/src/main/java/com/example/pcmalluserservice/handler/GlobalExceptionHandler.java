package com.example.pcmalluserservice.handler;

import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    public GlobalExceptionHandler() {
        log.debug("创建全局异常处理对象：GlobalExceptionHandler");
    }

    //处理自定义异常
    @ExceptionHandler(SystemException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> handlerSystemException(SystemException exception) {
        log.error("发生自定义SystemException异常：{}", exception.getResponseStatus());
        return ResponseResult.error(exception.getResponseStatus());
    }

    //处理账号和密码错误异常
    @ExceptionHandler(BadCredentialsException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> handlerBadCredentialsException(BadCredentialsException exception) {
        log.error("发生BadCredentialsException异常：{}", exception.getMessage());
        return ResponseResult.error(ResponseStatus.ACCOUNT_ERROR);
    }

    //处理权限异常
    @ExceptionHandler(AccessDeniedException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseResult<String> handlerAccessDeniedException(AccessDeniedException e) {
        log.error("发生AccessDeniedException异常：{}", e.getMessage());
        log.error("Class：{}", e.getClass());
        ResponseResult<String> message = ResponseResult.error(ResponseStatus.FORBIDDEN_ERROR);
        log.error(message.toString());
        return message;
    }

    //处理AuthenticationException内部其他异常
    @ExceptionHandler(AuthenticationException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult<String> handlerAuthenticationException(AuthenticationException e) {
        log.error("发生AuthenticationException异常：{}", e.getMessage());
        log.error("Class：{}", e.getClass());
        ResponseResult<String> message = ResponseResult.error(ResponseStatus.AUTHORIZED_ERROR);
        log.error(message.toString());
        return message;
    }

    //处理Exception异常
    @ExceptionHandler(Exception.class)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<String> handlerException(Exception e) {
        log.error("发生Exception异常：{}", e.getMessage());
        log.error("Class：{}", e.getClass());
        ResponseResult<String> message = ResponseResult.error(ResponseStatus.INTERNAL_SERVER_ERROR);
        log.error(message.toString());
        return message;
    }
}
