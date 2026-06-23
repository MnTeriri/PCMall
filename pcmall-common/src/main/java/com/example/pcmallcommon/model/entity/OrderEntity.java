package com.example.pcmallcommon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.pcmallcommon.model.dto.Order;
import com.example.pcmallcommon.model.dto.OrderAddress;
import com.example.pcmallcommon.model.dto.OrderGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName("`order`")
public class OrderEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String oid;//订单编号
    private String uid;//用户编号

    @TableField(exist = false)
    private List<OrderGoods> goodsList;//订单商品信息
    @TableField(exist = false)
    private OrderAddress address;//地址信息

    private BigDecimal price;//总金额
    private Order.OrderState status;//状态（0待付款、1待发货、2待收货、3交易成功、4交易取消、5退货中、6退货成功）
    private LocalDateTime createTime;//创建时间
    private LocalDateTime payTime;//付款时间
    private LocalDateTime sendTime;//发货时间
    private LocalDateTime finishTime;//完成时间
}
