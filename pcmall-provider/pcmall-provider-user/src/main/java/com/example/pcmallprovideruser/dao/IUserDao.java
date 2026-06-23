package com.example.pcmallprovideruser.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends BaseMapper<UserEntity> {

}
