package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HumanApprovalNode implements NodeAction<OrderGraphState> {
    @Override
    public Map<String, Object> apply(OrderGraphState state) throws Exception {
        Boolean approval = state.approval();
        if (Boolean.TRUE.equals(approval)) {
            log.debug("用户批准");
        } else {
            log.debug("用户拒绝");
        }
        return Map.of();
    }
}
