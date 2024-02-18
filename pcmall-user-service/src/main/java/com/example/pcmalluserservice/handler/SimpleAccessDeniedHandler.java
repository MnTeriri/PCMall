//package com.example.pcmalluserservice.handler;
//
//import com.alibaba.fastjson2.JSON;
//import com.example.pcmallcommon.response.ResponseResult;
//import com.example.pcmallcommon.response.ResponseCode;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@Slf4j
//@Component
//public class SimpleAccessDeniedHandler implements AccessDeniedHandler {
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        PrintWriter writer = response.getWriter();
//        ResponseResult<String> message = ResponseResult.error(ResponseCode.FORBIDDEN_ERROR);
//        log.error(message.toString());
//        writer.write(JSON.toJSONString(message));
//    }
//}
