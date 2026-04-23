package com.example.pcmalluserservice.handler;

import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallcommon.response.ResponseResult;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    public GlobalExceptionHandler() {
        log.debug("创建 GlobalExceptionHandler：{}", this);
    }

    //处理自定义异常
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> handlerSystemException(SystemException exception) {
        log.error("发生自定义SystemException异常：", exception);
        return ResponseResult.error(exception.getResponseStatus());
    }

    //处理账号和密码错误异常
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> handlerBadCredentialsException(BadCredentialsException exception) {
        log.error("发生BadCredentialsException异常：", exception);
        return ResponseResult.error(ResponseCode.ACCOUNT_ERROR);
    }

    //处理权限异常
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseResult<String> handlerAccessDeniedException(AccessDeniedException e) {
        log.error("发生AccessDeniedException异常：", e);
        return ResponseResult.error(ResponseCode.FORBIDDEN_ERROR);
    }

    //处理AuthenticationException内部其他异常
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult<String> handlerAuthenticationException(AuthenticationException e) {
        log.error("发生AuthenticationException异常：", e);
        return ResponseResult.error(ResponseCode.AUTHORIZED_ERROR);
    }

    //处理Exception异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<String> handlerException(Exception e) {
        log.error("发生Exception异常：", e);
        return ResponseResult.error(ResponseCode.INTERNAL_SERVER_ERROR);
    }
}
