package com.example.pcmallcommon.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("User")
public class User {
    private Integer id;
    private String uid;//用户号
    private String uname;//用户名
    private String password;//密码（MD5加密）
    private Integer flag;//账号类型，0商家，1用户
    private Integer status;//假删除，0删除了，1没删除
    //private byte[] image;
    //private String imageString;//头像
}
