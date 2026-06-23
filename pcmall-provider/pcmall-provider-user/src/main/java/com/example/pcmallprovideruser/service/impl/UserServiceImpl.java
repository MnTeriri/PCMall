package com.example.pcmallprovideruser.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.dto.User;
import com.example.pcmallcommon.model.entity.UserEntity;
import com.example.pcmallcommon.model.mapper.UserMapper;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovideruser.dao.IUserDao;
import com.example.pcmallprovideruser.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserDao userDao;

    @Override
    public User updateInformation(User user) {
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<UserEntity>()
                .eq("uid", user.getUid())
                .set("uname", user.getUname());
        if (user.getImage() != null) {
            updateWrapper.set("image", user.getImage());
        }
        //修改信息
        if (userDao.update(updateWrapper) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
        //查询最新信息
        UserEntity entity = userDao.selectOne(new QueryWrapper<UserEntity>().eq("uid", user.getUid()));
        return UserMapper.INSTANCE.toDto(entity);

    }

    @Override
    public void updatePassword(String uid, String oldPassword, String newPassword) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>()
                .eq("uid", uid)
                .eq("password", DigestUtil.md5Hex(oldPassword));
        if (userDao.selectOne(queryWrapper) == null) {
            throw new SystemException(ResponseCode.ACCOUNT_ERROR);
        }
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<UserEntity>()
                .eq("uid", uid)
                .set("password", DigestUtil.md5Hex(newPassword));
        if (userDao.update(updateWrapper) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
