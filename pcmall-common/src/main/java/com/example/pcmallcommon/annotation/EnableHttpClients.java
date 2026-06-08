package com.example.pcmallcommon.annotation;

import com.example.pcmallcommon.config.HttpClientConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({HttpClientConfig.class})
public @interface EnableHttpClients {
}
