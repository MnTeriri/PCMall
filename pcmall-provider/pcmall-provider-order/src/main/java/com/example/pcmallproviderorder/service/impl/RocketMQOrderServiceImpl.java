package com.example.pcmallproviderorder.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Order;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallproviderorder.dao.IOrderDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
public class RocketMQOrderServiceImpl extends OrderServiceImpl {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private StreamBridge streamBridge;

    public RocketMQOrderServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Override
    public String createOrder(String uid, Integer aid) {
        String oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        while (orderDao.selectCount(new QueryWrapper<Order>().eq("oid", oid)) != 0) {//如果生成的订单号存在，则重新生成，直到不存在
            oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("oid", oid);
        data.put("uid", uid);
        data.put("aid", aid);
        orderDao.createOrder(data);//执行存储过程
        Integer result = (Integer) data.get("result");//获取输出参数
        if (result == -4) {
            throw new SystemException(ResponseCode.ERROR);
        } else if (result == -3) {
            throw new SystemException(ResponseCode.CART_EMPTY_ERROR);
        } else if (result == -2) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);
        } else if (result == -1) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);
        } else if (result == 1) {
            Message<String> message = MessageBuilder
                    .withPayload("CHECK_OID:" + oid)
                    .setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, 15)//设置延时等级1~10
                    .setHeader("ORDER_ID", oid)
                    .build();
            streamBridge.send("cancelOrder-out-0", message);
        }
        return oid;
    }

    @Bean
    public Consumer<Message<String>> cancelOrder() {
        return message -> {
            String oid = (String) message.getHeaders().get("ORDER_ID");
            Order order = orderDao.selectOne(new QueryWrapper<Order>().eq("oid", oid));
            if (order.getStatus() == Order.OrderState.PENDING_PAYMENT) {//如果订单未付款
                Integer result = cancelOrder(oid, 4);//取消订单
                if (result == 1) {
                    log.debug("当前时间：{}，订单：{}付款超时，被取消！", LocalDateTime.now(), oid);
                } else {
                    log.debug("订单：{}，取消订单执行出错！", oid);
                }
            } else {
                log.debug("订单：{}，订单已付款或已取消！", oid);
            }
        };
    }
}
