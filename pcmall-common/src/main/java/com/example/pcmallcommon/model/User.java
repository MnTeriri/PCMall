package com.example.pcmallcommon.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("user")
public class User {
    private Integer id;
    private String uid;//用户号
    private String uname;//用户名
    private String password;//密码（MD5加密）
    private Integer isDelete;//0正常，1删除
    private LocalDateTime createdTime;//创建时间
    private LocalDateTime loginTime;//最后一次登录时间
    //private byte[] image;
    //private String imageString;//头像
}
