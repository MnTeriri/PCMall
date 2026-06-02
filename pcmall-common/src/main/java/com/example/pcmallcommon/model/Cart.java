package com.example.pcmallcommon.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName("cart")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer id;//购物车信息编号
    private String uid;//用户编号
    private Integer gid;//商品编号

    @TableField(exist = false)
    private Goods goods;
    private Integer count;//选购数量

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;//创建时间

    private Integer isSelect;//0为未选购，1为选购
}
