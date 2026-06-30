package com.example.pcmallai.ai.graph.state;

import com.example.pcmallai.model.OrderIntent;
import com.example.pcmallcommon.model.dto.Order;
import org.bsc.langgraph4j.state.AgentState;

import java.util.List;
import java.util.Map;

public class OrderGraphState extends AgentState {
    public OrderGraphState(Map<String, Object> initData) {
        super(initData);
    }

    // ──── 输入字段 ────
    public String userId() {
        return this.<String>value("userId").orElse(null);
    }

    public String sessionId() {
        return this.<String>value("sessionId").orElse(null);
    }

    public String memoryId() {
        return this.<String>value("memoryId").orElse(null);
    }

    public String userMessage() {
        return this.<String>value("userMessage").orElse(null);
    }

    // ──── 路由字段 ────
    public String route() {
        return this.<String>value("route").orElse("none");
    }

    // ──── 各节点输出 ────
    public OrderIntent orderIntent() {
        return this.<OrderIntent>value("orderIntent").orElse(null);
    }

    public List<Order> candidateOrders() {
        return this.<List<Order>>value("candidateOrders").orElse(List.of());
    }

    public Order selectedOrder() {
        return this.<Order>value("selectedOrder").orElse(null);
    }

    public String pendingDecision() {
        return this.<String>value("pendingDecision").orElse("none");
    }

    public boolean needConfirm() {
        return this.<Boolean>value("needConfirm").orElse(false);
    }

    public String blockReason() {
        return this.<String>value("blockReason").orElse(null);
    }

    public String actionResult() {
        return this.<String>value("actionResult").orElse(null);
    }

    public String orderReplyPrompt() {
        return this.<String>value("orderReplyPrompt").orElse(null);
    }

    public String finalReply() {
        return this.<String>value("finalReply").orElse("");
    }
}
