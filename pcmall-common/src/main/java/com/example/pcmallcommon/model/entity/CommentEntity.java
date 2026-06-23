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
@TableName("comment")
public class CommentEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String uid;//用户id
    private Integer gid;//商品id
    private String content;//评论内容
    private Integer star;//评分
    private LocalDateTime createTime;//创建时间
    private Integer pid;//上一级评论id
}
