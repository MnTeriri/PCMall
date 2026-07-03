package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.ai.service.OrderIntentAiService;
import com.example.pcmallai.model.OrderIntent;
import com.example.pcmallcommon.model.dto.Order;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_INTENT_ROUTE;
import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_ORDER_INTENT;
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
        List<Order> previousOrders = state.candidateOrders();

        Map<String, Object> result = new HashMap<>();
        result.put(MESSAGES_STATE, UserMessage.userMessage(TextContent.from(userMessage)));

        // 若上轮查询留下了 candidateOrders，拼入 LLM 消息，使"第一个""第二个"等可解析成 oid
        String prompt = userMessage;
        if (!previousOrders.isEmpty()) {
            prompt = buildContextMessage(userMessage, previousOrders);
            log.debug("已拼入上轮 {} 个订单作为上下文", previousOrders.size());
        }

        OrderIntent intent = orderIntentAiService.parseIntent(prompt);
        log.debug("Ai 解析的订单操作意图: {}", intent);
        result.put(KEY_ORDER_INTENT, intent);

        // 路由决策：query（走 OrderQueryNode） / action（走 OrderActionNode） / unknown（ReplyNode）
        if (QUERY_ACTIONS.contains(intent.getAction())) {
            result.put(KEY_INTENT_ROUTE, "query");
        } else if (ACTION_ACTIONS.contains(intent.getAction())) {
            result.put(KEY_INTENT_ROUTE, "action");
        } else {
            result.put(KEY_INTENT_ROUTE, "unknown");
        }
        return result;
    }

    /**
     * 将上一轮的订单列表拼接到用户消息尾部作为参考信息。
     * 例如："帮我取消第一个" → "帮我取消第一个\n--- 参考信息 ---\n1. 订单号 xxx..."
     */
    private String buildContextMessage(String userMessage, List<Order> orders) {
        StringBuilder sb = new StringBuilder(userMessage);
        sb.append("\n\n--- 参考信息（上次查询结果）---");
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            sb.append("\n").append(i + 1).append(". 订单号: ").append(o.getOid())
                    .append(", 金额: ").append(o.getPrice()).append("元");
        }
        return sb.toString();
    }
}
