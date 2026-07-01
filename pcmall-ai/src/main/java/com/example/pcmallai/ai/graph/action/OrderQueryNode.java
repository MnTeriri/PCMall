package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.model.OrderIntent;
import com.example.pcmallai.service.order.OrderQueryService;
import com.example.pcmallcommon.model.dto.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_CANDIDATE_ORDERS;

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

        log.debug("查询订单: action={}, uid={}, oid={}", action, uid, oid);

        Map<String, Object> result = new HashMap<>();
        List<Order> orders = new ArrayList<>();

        switch (action) {
            case QUERY, QUERY_RECENT -> orders = orderQueryService.queryRecent(uid);
            case QUERY_PENDING_PAYMENT -> orders = orderQueryService.queryPendingPayment(uid);
            case QUERY_PENDING_RECEIPT -> orders = orderQueryService.queryPendingReceipt(uid);
        }

        log.debug("查询订单完成: count={}", orders.size());

        result.put(KEY_CANDIDATE_ORDERS, orders);
        return result;
    }
}
