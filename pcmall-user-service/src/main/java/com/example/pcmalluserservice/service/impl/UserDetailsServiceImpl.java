package com.example.pcmalluserservice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.model.LoginUser;
import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmalluserservice.dao.IUserDao;
import com.example.pcmalluserservice.dao.IUserRoleDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IUserRoleDao userRoleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("uid", username);
        User user = userDao.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException(ResponseCode.ACCOUNT_ERROR.toString());//账号错误
        }
        log.debug("用户信息查询成功！，信息为：{}", user);
        user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(user.getPassword()));
        List<String> roles = userRoleDao.findUserRole(username);
        LoginUser loginUser = new LoginUser(user, roles);
        log.debug("用户权限信息为：{}", JSON.toJSONString(loginUser));
        return loginUser;
    }
}
