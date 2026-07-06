package com.example.pcmallai.ai.graph.action;

import com.example.pcmallai.ai.graph.state.OrderGraphState;
import com.example.pcmallai.model.OrderIntent;
import com.example.pcmallcommon.model.dto.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_ACTION_ROUTE;
import static com.example.pcmallai.ai.graph.state.OrderGraphState.KEY_FAILURE_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderActionNode implements NodeAction<OrderGraphState> {

    @Override
    public Map<String, Object> apply(OrderGraphState state) throws Exception {
        OrderIntent intent = state.orderIntent();
        OrderIntent.OrderAction action = intent.getAction();
        String oid = intent.getOid();
        String uid = state.userId();

        // 无订单号：引导先查询
        if (oid == null || oid.isBlank()) {
            return Map.of(KEY_ACTION_ROUTE, "failed", KEY_FAILURE_MESSAGE, guideToQuery(action));
        }

//        Order order = null;
//        if (order == null) {
//            return Map.of(KEY_ACTION_ROUTE, "failed", KEY_FAILURE_MESSAGE, "未找到订单号 " + oid + "，请确认后重试。");
//        }
//
//        // 状态校验
//        String blockReason = validate(action, order);
//        if (blockReason != null) {
//            return Map.of(KEY_ACTION_ROUTE, "failed", KEY_FAILURE_MESSAGE, blockReason);
//        }

        // 通过
        log.debug("校验通过: oid={}", oid);
        return Map.of(KEY_ACTION_ROUTE, "confirm");
    }

    private String guideToQuery(OrderIntent.OrderAction action) {
        return switch (action) {
            case PAY -> "请先查询您的订单，再告诉我具体要付款哪一个。";
            case CONFIRM_RECEIPT -> "请先查询您的订单，再告诉我具体要确认收货哪一个。";
            case CANCEL -> "请先查询您的订单（例如说'查最近订单'），再告诉我具体要取消哪一个。";
            case REFUND_REQUEST -> "请先查询您的订单，再告诉我具体要申请退货哪一个。";
            default -> "请先查询您的订单。";
        };
    }

    private String validate(OrderIntent.OrderAction action, Order order) {
        Order.OrderState state = order.getStatus();

        return switch (action) {
            case PAY -> state == Order.OrderState.PENDING_PAYMENT ? null
                    : "订单 " + order.getOid() + " 当前状态为" + state.getName() + "，无法付款。";
            case CANCEL ->
                    (state == Order.OrderState.PENDING_PAYMENT || state == Order.OrderState.PENDING_SHIPMENT) ? null
                            : "订单 " + order.getOid() + " 当前状态为" + state.getName() + "，无法取消。";
            case REFUND_REQUEST ->
                    (state == Order.OrderState.PENDING_SHIPMENT || state == Order.OrderState.PENDING_RECEIPT || state == Order.OrderState.SUCCESS) ? null
                            : "订单 " + order.getOid() + " 当前状态为" + state.getName() + "，无法申请退货。";
            case CONFIRM_RECEIPT -> state == Order.OrderState.PENDING_RECEIPT ? null
                    : "订单 " + order.getOid() + " 当前状态为" + state.getName() + "，无法确认收货。";
            default -> "不支持的操作。";
        };
    }
}
