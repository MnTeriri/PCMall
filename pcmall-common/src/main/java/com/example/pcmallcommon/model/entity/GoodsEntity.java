package com.example.pcmallcommon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.pcmallcommon.model.dto.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName("goods")
public class GoodsEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;//商品编号
    private Integer cid;//分类编号，参考category的主键
    private Integer bid;//品牌编号，参考brand的主键
    private String gname;//商品名称
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//修改时间
    private String image;//图片
    private BigDecimal price;//价格
    private BigDecimal discount;//折扣
    private Integer count;//数量
    private String description;//商品描述
    private Goods.GoodsState status;//商品状态（0正常、1缺货、2下架）
    private Integer isDelete;//是否删除（0正常 1删除）
}
