package com.example.pcmallcommon.model.dto;

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
public class Address {
    private Integer id;//地址编号
    private String uid;//用户编号
    private String province;//省
    private String city;//市
    private String district;//区
    private String addressDetail;//详细地址
    private String receiverName;//收件人
    private String phone;//手机号码

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;//修改时间

    private Integer isDefault;//是否选中（0不选中 1选中）
}