package com.example.pcmalluserservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends BaseMapper<User> {

}
