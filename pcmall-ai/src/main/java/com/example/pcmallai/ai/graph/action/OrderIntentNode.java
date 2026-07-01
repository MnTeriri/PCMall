package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.ai.service.OrderIntentAiService;
import com.example.pcmallai.model.OrderIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_ORDER_INTENT;
import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_ROUTE;
import static com.example.pcmallai.model.OrderIntent.OrderAction.*;

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
        OrderIntent intent = orderIntentAiService.parseIntent(userMessage);
        log.debug("Ai 解析的订单操作意图: {}", intent);

        Map<String, Object> result = new HashMap<>();
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
