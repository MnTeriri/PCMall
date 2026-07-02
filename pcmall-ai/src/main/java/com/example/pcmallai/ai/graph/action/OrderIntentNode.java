package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.ai.service.OrderIntentAiService;
import com.example.pcmallai.model.OrderIntent;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.langchain4j.generators.StreamingChatGenerator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.*;
import static com.example.pcmallai.model.OrderIntent.OrderAction.*;
import static org.bsc.langgraph4j.prebuilt.MessagesState.MESSAGES_STATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderIntentNode implements NodeAction<OrderGraphState> {

    // 查询类动作
    private static final Set<OrderIntent.OrderAction> QUERY_ACTIONS = Set.of(
            QUERY, QUERY_RECENT, QUERY_PENDING_PAYMENT, QUERY_PENDING_RECEIPT
    );

    //操作类动作
    private static final Set<OrderIntent.OrderAction> ACTION_ACTIONS = Set.of(
            PAY, CANCEL, REFUND_REQUEST, CONFIRM_RECEIPT
    );

    private final OrderIntentAiService orderIntentAiService;

    @Override
    public Map<String, Object> apply(OrderGraphState state) throws Exception {
        String userMessage = state.userMessage();
        Map<String, Object> result = new HashMap<>();

        var generator = StreamingChatGenerator.<OrderGraphState>builder()
                .mapResult(response -> Map.of(MESSAGES_STATE, response.aiMessage()))
                .startingNode("orderIntent")
                .startingState(state)
                .build();

        StreamingChatResponseHandler handler = generator.handler();
        handler.onPartialResponse("正在解析意图...");
        handler.onCompleteResponse(ChatResponse.builder().aiMessage(AiMessage.builder().text("正在解析意图...").build()).build());

        result.put(MESSAGES_STATE, UserMessage.userMessage(TextContent.from(userMessage)));
        result.put(KEY_STREAMING, generator);

        OrderIntent intent = orderIntentAiService.parseIntent(userMessage);
        log.debug("Ai 解析的订单操作意图: {}", intent);

        result.put(KEY_ORDER_INTENT, intent);

        // 路由决策：query（走 OrderQueryNode） / action（走 OrderActionNode） / unknown（ReplyNode）
        if (QUERY_ACTIONS.contains(intent.getAction())) {
            result.put(KEY_ROUTE, "query");
        } else if (ACTION_ACTIONS.contains(intent.getAction())) {
            result.put(KEY_ROUTE, "action");
        } else {
            result.put(KEY_ROUTE, "unknown");
        }
        return result;
    }
}
