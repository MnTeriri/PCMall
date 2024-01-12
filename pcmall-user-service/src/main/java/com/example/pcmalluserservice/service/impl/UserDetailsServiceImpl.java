package com.example.pcmalluserservice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.model.LoginUser;
import com.example.pcmallcommon.model.User;
import com.example.pcmalluserservice.dao.IUserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IUserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("uid", username);
        User user = userDao.selectOne(queryWrapper);
        log.debug("用户{}信息查询成功", user);
        user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode("123456"));
        //user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        ArrayList<String> roles = new ArrayList<>();
        roles.add("admin");
        roles.add("user");
        LoginUser loginUser = new LoginUser(user, roles);
        log.debug("用户权限信息为{}", JSON.toJSONString(loginUser));

        return loginUser;
    }
}
