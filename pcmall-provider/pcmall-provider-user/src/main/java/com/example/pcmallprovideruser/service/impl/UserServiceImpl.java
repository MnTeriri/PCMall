package com.example.pcmallprovideruser.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.User;
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
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>()
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
        return userDao.selectOne(new QueryWrapper<User>().eq("uid", user.getUid()));

    }

    @Override
    public void updatePassword(String uid, String oldPassword, String newPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("uid", uid)
                .eq("password", DigestUtil.md5Hex(oldPassword));
        if (userDao.selectOne(queryWrapper) == null) {
            throw new SystemException(ResponseCode.ACCOUNT_ERROR);
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>()
                .eq("uid", uid)
                .set("password", DigestUtil.md5Hex(newPassword));
        if (userDao.update(updateWrapper) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
