package com.example.pcmallai.service.facade;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallcommon.model.ai.AiChatEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderGraphService {
    private final CompiledGraph<OrderGraphState> orderGraph;

    public Flux<AiChatEvent> chatFlux() {
        Map<String, Object> initialState = Map.of(
                "userId", "userId",
                "sessionId", "sessionId",
                "memoryId", "memoryId",
                "userMessage", "我要查昨天的订单"
        );

        OrderGraphState result = orderGraph.invoke(initialState)
                .orElseThrow(() -> new RuntimeException("订单图执行失败"));

        return Flux.just(
                new AiChatEvent(AiChatEvent.AiChatEventType.TEXT, result.finalReply())
        );
    }
}
