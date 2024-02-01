package com.example.pcmalluserservice.handler;

import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.response.ResponseStatus;
import com.example.pcmalluserservice.exception.SystemException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseResult<String> handlerSystemException(SystemException exception) {
        log.error("发生自定义SystemException异常：{}", exception.getResponseStatus());
        return ResponseResult.error(exception.getResponseStatus());
    }

    //处理账号和密码错误异常
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseResult<String> handlerBadCredentialsException(BadCredentialsException exception) {
        log.error("发生BadCredentialsException异常：{}", exception.getMessage());
        return ResponseResult.error(ResponseStatus.ACCOUNT_ERROR);
    }

    //处理权限异常
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult<String> handlerAccessDeniedException(HttpServletResponse response, AccessDeniedException e) {
        log.error("发生AccessDeniedException异常：{}", e.getMessage());
        log.error("Class：{}", e.getClass());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ResponseResult<String> message = ResponseResult.error(ResponseStatus.FORBIDDEN_ERROR);
        log.error(message.toString());
        return message;
    }

    //处理Authentication内部其他异常
    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult<String> handlerAuthenticationException(HttpServletResponse response, AuthenticationException e) {
        log.error("发生AuthenticationException异常：{}", e.getMessage());
        log.error("Class：{}", e.getClass());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseResult<String> message = ResponseResult.error(ResponseStatus.AUTHORIZED_ERROR);
        log.error(message.toString());
        return message;
    }
}
