//package com.example.pcmalluserservice.handler;
//
//import com.alibaba.fastjson2.JSON;
//import com.example.pcmallcommon.response.ResponseCode;
//import com.example.pcmallcommon.response.ResponseResult;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@Slf4j
//@Component
//public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        PrintWriter writer = response.getWriter();
//        ResponseResult<String> message = ResponseResult.error(ResponseCode.AUTHORIZED_ERROR);
//        log.error(message.toString());
//        writer.write(JSON.toJSONString(message));
//    }
//}
