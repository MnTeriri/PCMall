package com.example.pcmallai.service.facade;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallcommon.model.ai.AiChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderGraphService {
    private final CompiledGraph<OrderGraphState> orderGraph;

    public Map<String, Object> chatFlux(AiChatRequest request) {
        Map<String, Object> initialState = Map.of(
                OrderGraphState.KEY_USER_ID, request.getUserId(),
                OrderGraphState.KEY_SESSION_ID, "request.getSessionId()",
                OrderGraphState.KEY_MEMORY_ID, "memoryId",
                OrderGraphState.KEY_USER_MESSAGE, request.getMessage()
        );

        OrderGraphState result = orderGraph.invoke(initialState)
                .orElseThrow(() -> new RuntimeException("订单图执行失败"));

        return result.data();
    }
}
