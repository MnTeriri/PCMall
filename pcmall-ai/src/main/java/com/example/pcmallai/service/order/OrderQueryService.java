package com.example.pcmallai.service.order;

import com.example.pcmallcommon.client.OrderClient;
import com.example.pcmallcommon.model.dto.Order;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderQueryService {
    private static final int RECENT_PAGE_SIZE = 5;// 最近订单默认拉取条数
    private static final int DEFAULT_PAGE_SIZE = 20;// 普通查询默认每页条数
    private static final int TYPE_ALL = -1;// type 为 -1 表示不过滤状态

    private final OrderClient orderClient;

    public List<Order> queryRecent(String uid) {
        return searchByType(uid, TYPE_ALL, RECENT_PAGE_SIZE);
    }

    public List<Order> queryPendingPayment(String uid) {
        return searchByType(uid, Order.OrderState.PENDING_PAYMENT.getCode(), DEFAULT_PAGE_SIZE);
    }

    public List<Order> queryPendingReceipt(String uid) {
        return searchByType(uid, Order.OrderState.PENDING_RECEIPT.getCode(), DEFAULT_PAGE_SIZE);
    }

    private List<Order> searchByType(String uid, int type, int pageSize) {
        ResponseResult<List<Order>> response = orderClient.searchOrderList("", uid, type, 1, pageSize);
        if (response == null || response.getData() == null) {
            return List.of();
        }
        return response.getData();
    }
}
