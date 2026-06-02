package com.example.pcmallcommon.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName("goods")
public class Goods {
    @TableId(type = IdType.AUTO)
    private Integer id;//商品编号

    private Integer cid;//分类编号，参考category的主键
    @TableField(exist = false)
    private Category category;

    private Integer bid;//品牌编号，参考brand的主键
    @TableField(exist = false)
    private Brand brand;

    private String gname;//商品名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;//修改时间

    private String image;//图片
    private BigDecimal price;//价格
    private BigDecimal discount;//折扣
    private Integer count;//数量
    private String description;//商品描述
    private GoodsState status;//商品状态（0正常、1缺货、2下架）
    private Integer isDelete;//是否删除（0正常 1删除）

    @AllArgsConstructor
    @ToString
    @Getter
    public enum GoodsState {
        NORMAL(0, "正常"),
        OUT_OF_STOCK(1, "缺货"),
        OFF_SHELF(2, "下架");

        @JsonValue
        @EnumValue
        private final Integer code;
        private final String name;

        @JsonCreator
        public static GoodsState fromCode(Integer code) {
            for (GoodsState state : values()) {
                if (Objects.equals(state.code, code)) {
                    return state;
                }
            }
            return null;
        }
    }
}
