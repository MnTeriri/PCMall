package com.example.pcmallai.listener;

import com.example.pcmallai.service.impl.GoodsKnowledgeService;
import com.example.pcmallcommon.model.dto.Goods;
import com.example.pcmallcommon.model.message.GoodsChangeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@RequiredArgsConstructor
@Service
@RocketMQMessageListener(
        topic = "${rocketmq.topic}",
        consumerGroup = "${rocketmq.consumer.group}"
)
public class GoodsChangeListener implements RocketMQListener<GoodsChangeMessage> {

    private final GoodsKnowledgeService goodsKnowledgeService;

    /**
     * 每个 goodsId 最近一次处理的时间戳，用于防乱序
     */
    private static final ConcurrentMap<Integer, Long> LATEST_TIMESTAMPS = new ConcurrentHashMap<>();

    @Override
    public void onMessage(GoodsChangeMessage message) {
        Integer goodsId = message.getGoodsId();

        Long msgTime = message.getTimestamp();

        // 防乱序：跳过比已处理时间戳更旧的消息
        Long latest = LATEST_TIMESTAMPS.get(goodsId);
        if (latest != null && msgTime <= latest) {
            log.debug("跳过过期消息: goodsId={}, msgTime={}, lastTime={}", goodsId, msgTime, latest);
            return;
        }

        GoodsChangeMessage.Action action = message.getAction();
        try {
            switch (action) {
                case ADD, UPDATE, RECOVER -> {
                    log.debug("触发动作：{}，进行商品知识更新", action);
                    goodsKnowledgeService.upsertGoods(message.getGoods());
                }
                case DELETE -> {
                    log.debug("触发动作：{}，进行商品知识删除", action);
                    goodsKnowledgeService.deleteById(message.getGoodsId());
                }
                case STATUS_CHANGE -> {
                    Goods goods = message.getGoods();
                    Goods.GoodsState status = goods.getStatus();
                    if (status == Goods.GoodsState.OUT_OF_STOCK || status == Goods.GoodsState.OFF_SHELF) {
                        log.debug("触发动作：{}，商品状态：{}，进行商品知识删除", action, goods.getStatus());
                        goodsKnowledgeService.deleteById(message.getGoodsId());
                    } else {
                        log.debug("触发动作：{}，商品状态：{}，进行商品知识更新", action, goods.getStatus());
                        goodsKnowledgeService.upsertGoods(message.getGoods());
                    }
                }
            }
            LATEST_TIMESTAMPS.put(goodsId, msgTime);
        } catch (Exception e) {
            log.error("消费异常，等待 RocketMQ 重试: goodsId={}, action={}", goodsId, action, e);
            throw e;
        }
    }
}
