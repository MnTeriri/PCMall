package com.example.pcmallgateway.filter;

import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.response.ResponseCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter implements Ordered {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("进入 AuthenticationFilter");
        String uri = request.getRequestURI();
        log.debug("访问的链接是：{}", uri);
        if ("/api/login".equals(uri)
                || "/api/register".equals(uri)
                || "/api/captcha.jpg".equals(uri)
                || uri.startsWith("/api/image")
                || uri.startsWith("/api/mobile/goods")
                || uri.startsWith("/api/mobile/category")
                || uri.startsWith("/api/mobile/brand")) {
            log.debug("访问 {}，无需 token", uri);
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("token");
        if (token == null) {
            log.error("访问 {}，但无 token", uri);
            resolver.resolveException(request, response, null, new SystemException(ResponseCode.NO_TOKEN_ERROR));
            return;
        }
        log.debug("访问 {}，token：{}", uri, token);
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}