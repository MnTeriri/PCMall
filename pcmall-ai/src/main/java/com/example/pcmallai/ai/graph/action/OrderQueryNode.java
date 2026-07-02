package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.model.OrderIntent;
import com.example.pcmallai.service.order.OrderQueryService;
import com.example.pcmallcommon.model.dto.Order;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_CANDIDATE_ORDERS;
import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_STREAMING;
import static org.bsc.langgraph4j.prebuilt.MessagesState.MESSAGES_STATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderQueryNode implements NodeAction<OrderGraphState> {

    private final OrderQueryService orderQueryService;

    @Override
    public Map<String, Object> apply(OrderGraphState state) throws Exception {
        OrderIntent intent = state.orderIntent();
        OrderIntent.OrderAction action = intent.getAction();
        String oid = intent.getOid() == null ? "" : intent.getOid();
        String uid = state.userId();

        var generator = StreamingChatGenerator.<OrderGraphState>builder()
                .mapResult(response -> Map.of(MESSAGES_STATE, response.aiMessage()))
                .startingNode("orderQuery")
                .startingState(state)
                .build();

        StreamingChatResponseHandler handler = generator.handler();
        handler.onPartialResponse("正在查询订单...");

        log.debug("查询订单: action={}, uid={}, oid={}", action, uid, oid);

        Map<String, Object> result = new HashMap<>();
        result.put(KEY_STREAMING, generator);

        List<Order> orders = new ArrayList<>();
        switch (action) {
            case QUERY, QUERY_RECENT -> orders = orderQueryService.queryRecent(uid);
            case QUERY_PENDING_PAYMENT -> orders = orderQueryService.queryPendingPayment(uid);
            case QUERY_PENDING_RECEIPT -> orders = orderQueryService.queryPendingReceipt(uid);
        }
        handler.onPartialResponse("查询订单完成");

        handler.onCompleteResponse(ChatResponse.builder().aiMessage(AiMessage.builder().text("正在查询订单...\n查询订单完成").build()).build());
        log.debug("查询订单完成: count={}", orders.size());

        result.put(KEY_CANDIDATE_ORDERS, orders);
        return result;
    }
}
