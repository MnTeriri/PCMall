package com.example.pcmalluserservice.filter;

import cn.hutool.jwt.JWT;
import com.alibaba.fastjson2.JSON;
import com.example.pcmallcommon.model.LoginUser;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.response.ResponseStatus;
import com.example.pcmallcommon.utils.JwtUtils;
import com.example.pcmalluserservice.utils.RedisUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("进入JwtAuthenticationTokenFilter");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String requestURI = request.getRequestURI();
        if ("/api/login".equals(requestURI)
                || "/api/register".equals(requestURI)
                || "/api/captcha.jpg".equals(requestURI)) {
            log.debug("是{}，放行", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("token");
        if (token == null) {
            //没有token
            ResponseResult<String> message = ResponseResult.error(ResponseStatus.NO_TOKEN_ERROR);
            log.error(message.toString());
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(message));
            //filterChain.doFilter(request, response);
            return;
        }
        //解析并验证token
        JWT jwt = JwtUtils.parseToken(token);
        if (JwtUtils.verify(jwt)) {
            //验证结果为真代表token失效
            ResponseResult<String> message = ResponseResult.error(ResponseStatus.TOKEN_EXPIRE_ERROR);
            log.error(message.toString());
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(message));
            return;
        }
        //获取uid
        String uid = JwtUtils.getPayload(jwt, "uid");
        log.debug("登录用户uid：{}", uid);
        LoginUser loginUser = RedisUtils.getCacheObject(uid, LoginUser.class);
        log.debug("登录用户信息：{}", loginUser);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, loginUser.getUser().getPassword(), loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);//添加到SecurityContext上下文
        log.debug("用户权限信息已存放到SecurityContextHolder，此用户拥有权限：{}", authenticationToken.getAuthorities());
        filterChain.doFilter(request, response);
    }
}
