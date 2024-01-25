package com.example.pcmalluserservice.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWT;
import com.example.pcmallcommon.model.LoginUser;
import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallcommon.utils.JwtUtils;
import com.example.pcmalluserservice.service.IUserService;
import com.example.pcmalluserservice.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private AuthenticationManager authenticationManager;

//    @Override
//    public String login(String uid, String password) {
//        //查询权限信息
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uid, "123456");
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
//        User user = loginUser.getUser();
//        RedisUtils.setCacheObject(user.getUid(), loginUser);
//        String token = JWT.create()
//                .setPayload("uid", user.getUid())
//                .setKey("Teriri".getBytes())
//                .sign();
//        return token;
//    }

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
        String token = JwtUtils.createToken(payload, 1);

        return new ResponseResult<>(200, token, user);
    }

    @Override
    public String register() {
        return null;
    }
}
