package com.example.pcmallconsumermobile.filter;

import cn.hutool.jwt.JWT;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.LoginUser;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallcommon.utils.JwtUtils;
import com.example.pcmallconsumermobile.utils.RedisUtils;
import com.example.pcmallconsumermobile.utils.SpringContextUtils;
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
//@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
//    @Autowired
//    @Qualifier("handlerExceptionResolver")
//    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("进入JwtAuthenticationTokenFilter");
        HandlerExceptionResolver resolver= SpringContextUtils.getBean("handlerExceptionResolver");
        String token = request.getHeader("token");
        if (token == null|| token.isEmpty()) {
            //没有token
            log.error(ResponseCode.NO_TOKEN_ERROR.toString());
            resolver.resolveException(request, response, null, new SystemException(ResponseCode.NO_TOKEN_ERROR));
            return;
        }
        //解析并验证token
        JWT jwt = JwtUtils.parseToken(token);
        if (JwtUtils.verify(jwt)) {
            //验证结果为真代表token失效
            log.error(ResponseCode.TOKEN_EXPIRE_ERROR.toString());
            resolver.resolveException(request, response, null, new SystemException(ResponseCode.TOKEN_EXPIRE_ERROR));
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
