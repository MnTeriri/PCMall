package com.example.pcmalluserservice.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.dto.LoginUser;
import com.example.pcmallcommon.model.dto.User;
import com.example.pcmallcommon.model.entity.UserEntity;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.utils.JwtUtils;
import com.example.pcmallcommon.utils.RedisUtils;
import com.example.pcmalluserservice.dao.IUserDao;
import com.example.pcmalluserservice.dao.IUserRoleDao;
import com.example.pcmalluserservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final AuthenticationManager authenticationManager;

    private final IUserDao userDao;

    private final IUserRoleDao userRoleDao;

    @Override
    public ResponseResult<User> login(String uid, String password) {
        //查询权限信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uid, DigestUtil.md5Hex(password));
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //用户信息存到redis
        RedisUtils.setCacheObject(uid, loginUser);
        //生成token
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("uid", uid);
        String token = JwtUtils.createToken(payload, 10);

        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<UserEntity>()
                .eq("uid", uid)
                .set("login_time", LocalDateTime.now());
        if (userDao.update(updateWrapper) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }

        return new ResponseResult<>(200, token, loginUser.getUser());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult<String> register(String uid, String password) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>()
                .eq("uid", uid);
        UserEntity entity = userDao.selectOne(queryWrapper);
        if (entity != null) {
            //用户存在
            throw new SystemException(ResponseCode.USER_EXIST_ERROR);
        }
        entity = new UserEntity().setUid(uid).setUname("未设置用户名").setPassword(DigestUtil.md5Hex(password));
        if (userDao.insert(entity) != 1) {
            //插入用户失败，未知错误
            throw new SystemException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        if (userRoleDao.insertUserRole(uid, 2) != 1) {
            //插入权限失败，未知错误
            throw new SystemException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return ResponseResult.ok("用户注册成功！");
    }

}
