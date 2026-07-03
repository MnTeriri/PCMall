package com.example.pcmallai.ai.graph.state;

import com.example.pcmallai.model.OrderIntent;
import com.example.pcmallcommon.model.dto.Order;
import dev.langchain4j.data.message.ChatMessage;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import java.util.List;
import java.util.Map;

public class OrderGraphState extends MessagesState<ChatMessage> {
    public static final String KEY_STREAMING = "streaming_messages";

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_SESSION_ID = "sessionId";
    public static final String KEY_MEMORY_ID = "memoryId";
    public static final String KEY_USER_MESSAGE = "userMessage";
    public static final String KEY_APPROVAL = "approval";

    public static final String KEY_INTENT_ROUTE = "intentRoute";
    public static final String KEY_ACTION_ROUTE = "actionRoute";
    public static final String KEY_ORDER_INTENT = "orderIntent";
    public static final String KEY_CANDIDATE_ORDERS = "candidateOrders";
    public static final String KEY_IS_SUCCESS_EXECUTE = "isSuccessExecute";
    public static final String KEY_FAILURE_MESSAGE = "failureMessage";

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

    public Boolean approval() {
        return this.<Boolean>value(KEY_APPROVAL).orElse(false);
    }

    // 节点执行后增加信息
    public String intentRoute() {
        return this.<String>value(KEY_INTENT_ROUTE).orElse("unknown");
    }

    public String actionRoute() {
        return this.<String>value(KEY_ACTION_ROUTE).orElse("failed");
    }

    public OrderIntent orderIntent() {
        return this.<OrderIntent>value(KEY_ORDER_INTENT).orElse(new OrderIntent());
    }

    public List<Order> candidateOrders() {
        return this.<List<Order>>value(KEY_CANDIDATE_ORDERS).orElse(List.of());
    }

    public Boolean isSuccessExecute() {
        return this.<Boolean>value(KEY_IS_SUCCESS_EXECUTE).orElse(false);
    }

    public String failureMessage() {
        return this.<String>value(KEY_FAILURE_MESSAGE).orElse("");
    }
}
