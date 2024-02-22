package com.example.pcmallprovidergoods.handler;

import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    //处理Exception异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<String> handlerException(Exception e) {
        log.error("发生Exception异常：{}", e.getMessage());
        log.error("Class：{}", e.getClass());
        ResponseResult<String> message = ResponseResult.error(ResponseCode.INTERNAL_SERVER_ERROR);
        log.error(message.toString());
        return message;
    }
}
