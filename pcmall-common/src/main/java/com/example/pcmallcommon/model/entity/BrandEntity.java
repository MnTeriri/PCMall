package com.example.pcmallcommon.model.entity;

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
@TableName("brand")
public class BrandEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;//品牌id
    private String bname;//品牌名称
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//修改时间
    private String image;
    private Integer isDelete;//是否删除（0正常 1删除）
}