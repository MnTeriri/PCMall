package com.example.pcmallcommon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName("storage")
public class Storage {
    @TableId(type = IdType.AUTO)
    private Integer id;//库存编号
    private Integer gid;//商品编号
    private String uid;//用户编号
    private Integer count;//数量
    private Integer status;//状态 0入库、1卖出、2退货、3取消订单
    private LocalDateTime createdTime;//创建时间
}
