package com.example.pcmallcommon.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private Integer status;//状态 0入库、1出库、2卖出、3退货、4取消订单
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdTime;//创建时间

    @AllArgsConstructor
    @ToString
    @Getter
    public enum StorageState {
        IN_STOCK(0, "入库"),
        OUT_STOCK(1, "出库"),
        SOLD(2, "卖出"),
        RETURNED(3, "退货"),
        CANCELED(4, "取消订单");

        @JsonValue
        @EnumValue
        private final Integer code;
        private final String name;

        @JsonCreator
        public static StorageState fromCode(Integer code) {
            for (StorageState state : values()) {
                if (Objects.equals(state.code, code)) {
                    return state;
                }
            }
            return null;
        }
    }
}
