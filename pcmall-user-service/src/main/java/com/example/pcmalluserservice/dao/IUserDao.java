package com.example.pcmalluserservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDao extends BaseMapper<User> {
    @Select("SELECT * FROM user_role WHERE uid=#{uid}")
    List<String> findUserRole(String uid);
}
