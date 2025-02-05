package com.example.pcmallproviderorder.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class RocketMQOrderServiceImpl extends OrderServiceImpl {
    @Autowired
    private StreamBridge streamBridge;

    public RocketMQOrderServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Override
    public String createOrder(String uid, Integer aid) {
        Message<String> message = MessageBuilder.withPayload("rocket测试：").build();
        streamBridge.send("demo1-out-0", message);
        return "";
    }

    @Bean
    public Consumer<Message<String>> demo1() {
        return message -> {
            System.out.println("接收到消息Payload：" + message.getPayload());
            System.out.println("接收到消息Header：" + message.getHeaders());
        };
    }
}
