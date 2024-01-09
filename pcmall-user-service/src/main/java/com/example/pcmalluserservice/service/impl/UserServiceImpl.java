package com.example.pcmalluserservice.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.pcmalluserservice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String login(String uid, String password) {
        //查询权限信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uid, DigestUtil.md5Hex(password));
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return null;
    }

    @Override
    public String register() {
        return null;
    }
}
