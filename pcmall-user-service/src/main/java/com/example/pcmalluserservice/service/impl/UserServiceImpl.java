package com.example.pcmalluserservice.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.LoginUser;
import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.utils.JwtUtils;
import com.example.pcmalluserservice.dao.IUserDao;
import com.example.pcmalluserservice.dao.IUserRoleDao;
import com.example.pcmalluserservice.service.IUserService;
import com.example.pcmalluserservice.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IUserRoleDao userRoleDao;

    @Override
    public ResponseResult<User> login(String uid, String password) {
        //查询权限信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uid, DigestUtil.md5Hex(password));
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        User user = loginUser.getUser();
        //用户信息存到redis
        RedisUtils.setCacheObject(user.getUid(), loginUser);
        //生成token
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("uid", user.getUid());
        String token = JwtUtils.createToken(payload, 10);

        return new ResponseResult<>(200, token, user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult<String> register(String uid, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("uid", uid);
        User user = userDao.selectOne(queryWrapper);
        if (user != null) {
            //用户存在
            throw new SystemException(ResponseCode.USER_EXIST_ERROR);
        }
        user = new User().setUid(uid).setUname("未设置用户名").setPassword(DigestUtil.md5Hex(password));
        if (userDao.insert(user) != 1) {
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
