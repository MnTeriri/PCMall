package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.model.OrderIntent;
import com.example.pcmallai.service.order.OrderQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_FAILURE_MESSAGE;
import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_IS_SUCCESS_EXECUTE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderExecuteNode implements NodeAction<OrderGraphState> {

    private final OrderQueryService orderQueryService;

    @Override
    public Map<String, Object> apply(OrderGraphState state) throws Exception {
        OrderIntent intent = state.orderIntent();
        OrderIntent.OrderAction action = intent.getAction();
        String oid = intent.getOid();

        try {
            switch (action) {
                case PAY -> orderQueryService.payOrder(oid);
                case CONFIRM_RECEIPT -> orderQueryService.finishOrder(oid);
                case CANCEL -> orderQueryService.cancelOrder(oid);
                case REFUND_REQUEST -> orderQueryService.refundOrder(oid);
            }

            return Map.of(KEY_IS_SUCCESS_EXECUTE, true);
        } catch (Exception e) {
            log.error("OrderExecuteNode 出现错误", e);
            return Map.of(
                    KEY_IS_SUCCESS_EXECUTE, false,
                    KEY_FAILURE_MESSAGE, e.getMessage()
            );
        }
    }
}
