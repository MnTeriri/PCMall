package com.example.pcmallcommon.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String oid;//订单编号
    private String uid;//用户编号

    @TableField(exist = false)
    private List<OrderGoods> goodsList;//订单商品信息

    @TableField(exist = false)
    private OrderAddress address;//地址信息

    private BigDecimal price;//总金额
    private OrderState status;//状态（0待付款、1待发货、2待收货、3交易成功、4交易取消、5退货中、6退货成功）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime payTime;//付款时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sendTime;//发货时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime finishTime;//完成时间

    @AllArgsConstructor
    @ToString
    @Getter
    public enum OrderState {
        PENDING_PAYMENT(0, "待付款"),
        PENDING_SHIPMENT(1, "待发货"),
        PENDING_RECEIPT(2, "待收货"),
        SUCCESS(3, "交易成功"),
        CANCELED(4, "交易取消"),
        RETURNING(5, "退货中"),
        RETURNED(6, "退货成功");

        @JsonValue
        @EnumValue
        private final Integer code;
        private final String name;

        @JsonCreator
        public static OrderState fromCode(Integer code) {
            for (OrderState state : values()) {
                if (Objects.equals(state.code, code)) {
                    return state;
                }
            }
            return null;
        }
    }
}
