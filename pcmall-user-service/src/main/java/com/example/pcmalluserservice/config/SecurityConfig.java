package com.example.pcmalluserservice.config;

import com.example.pcmalluserservice.filter.JwtAuthenticationTokenFilter;
import com.example.pcmalluserservice.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userService;
//    @Autowired
//    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//    @Autowired
//    private SimpleAuthenticationEntryPoint authenticationEntryPoint;
//    @Autowired
//    private SimpleAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig() {
        log.debug("创建配置类对象：SecurityConfig");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/register").permitAll()
                        .requestMatchers("/api/captcha.jpg").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()//放行所有OPTIONS请求,要不然连header里面的token都获取不了
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);
//                .exceptionHandling(config -> {
//                    config.authenticationEntryPoint(authenticationEntryPoint);
//                    config.accessDeniedHandler(accessDeniedHandler);
//                });
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/api/login")
                .requestMatchers("/api/register")
                .requestMatchers("/api/captcha.jpg");
    }
}