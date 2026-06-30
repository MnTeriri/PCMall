package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.ai.service.OrderIntentAiService;
import com.example.pcmallai.model.OrderIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderIntentNode implements NodeAction<OrderGraphState> {

    private final OrderIntentAiService orderIntentAiService;

    @Override
    public Map<String, Object> apply(OrderGraphState state) throws Exception {
        String userMessage = state.userMessage();
        log.debug("[OrderIntentNode] 解析意图: {}", userMessage);

        OrderIntent intent = orderIntentAiService.parseIntent(userMessage);
        log.debug("[OrderIntentNode] 结果: action={}, oid={}, searchValue={}",
                intent.getAction(), intent.getOid(), intent.getSearchValue());

        return Map.of("orderIntent", intent);
    }
}
