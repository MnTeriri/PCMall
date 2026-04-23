package com.example.pcmallcommon.filter;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.jwt.JWT;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.LoginUser;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallcommon.utils.JwtUtils;
import com.example.pcmallcommon.utils.RedisUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("进入 JwtAuthenticationTokenFilter");
        String uri = request.getRequestURI();
        String token = request.getHeader("token");
        HandlerExceptionResolver resolver= SpringUtil.getBean("handlerExceptionResolver");

        if (token == null) {
            //没有token
            log.error("访问 {}，但无 token", uri);
            resolver.resolveException(request, response, null, new SystemException(ResponseCode.NO_TOKEN_ERROR));
            return;
        }

        //解析并验证token
        JWT jwt = JwtUtils.parseToken(token);
        if (JwtUtils.verify(jwt)) {
            //验证结果为真代表token失效
            log.error("访问 {}，但 token {} 失效", uri, token);
            resolver.resolveException(request, response, null, new SystemException(ResponseCode.TOKEN_EXPIRE_ERROR));
            return;
        }

        //获取uid
        log.debug("访问 {}，token {} 认证成功", uri, token);
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
