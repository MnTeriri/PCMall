package com.example.pcmalluserservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringContextUtils implements ApplicationContextAware {
    public static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        log.debug("ApplicationContext已装配");
        SpringContextUtils.context = context;
    }

    public static <T> T getBean(Class<T> tClass) {
        log.debug("获取Class为{}的Bean", tClass);
        return context.getBean(tClass);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        log.debug("获取名称为{}的Bean", name);
        return (T) context.getBean(name);
    }
}