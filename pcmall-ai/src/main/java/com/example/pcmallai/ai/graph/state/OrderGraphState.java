package com.example.pcmallai.ai.graph.state;

import com.example.pcmallai.model.OrderIntent;
import com.example.pcmallcommon.model.dto.Order;
import org.bsc.langgraph4j.state.AgentState;

import java.util.List;
import java.util.Map;

public class OrderGraphState extends AgentState {
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_SESSION_ID = "sessionId";
    public static final String KEY_MEMORY_ID = "memoryId";
    public static final String KEY_USER_MESSAGE = "userMessage";

    public static final String KEY_ROUTE = "route";
    public static final String KEY_ORDER_INTENT = "orderIntent";
    public static final String KEY_CANDIDATE_ORDERS = "candidateOrders";
    public static final String KEY_FINAL_REPLY = "finalReply";

    public OrderGraphState(Map<String, Object> initData) {
        super(initData);
    }

    // 原始输入
    public String userId() {
        return this.<String>value(KEY_USER_ID).orElse(null);
    }

    public String sessionId() {
        return this.<String>value(KEY_SESSION_ID).orElse(null);
    }

    public String memoryId() {
        return this.<String>value(KEY_MEMORY_ID).orElse(null);
    }

    public String userMessage() {
        return this.<String>value(KEY_USER_MESSAGE).orElse(null);
    }

    // 节点执行后增加信息
    public String route() {
        return this.<String>value(KEY_ROUTE).orElse("unknown");
    }

    public OrderIntent orderIntent() {
        return this.<OrderIntent>value(KEY_ORDER_INTENT).orElse(new OrderIntent());
    }

    public List<Order> candidateOrders() {
        return this.<List<Order>>value(KEY_CANDIDATE_ORDERS).orElse(List.of());
    }

    public String finalReply() {
        return this.<String>value(KEY_FINAL_REPLY).orElse("");
    }
}
