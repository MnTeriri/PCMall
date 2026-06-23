package com.example.pcmallcommon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.pcmallcommon.model.dto.Storage;
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
public class StorageEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;//库存编号
    private Integer gid;//商品编号
    private String uid;//用户编号
    private Integer count;//数量
    private Storage.StorageState status;//状态 0入库、1出库、2卖出、3退货、4取消订单
    private LocalDateTime createTime;//创建时间
}
