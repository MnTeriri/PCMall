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
@TableName("cart")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer id;//购物车信息编号
    private String uid;//用户编号
    private Integer gid;//商品编号
    private Integer count;//选购数量
    private LocalDateTime createdTime;//创建时间
    private Integer is_select;//0为未选购，1为选购
    private Integer is_delete;//是否删除（0正常 1删除）
}
