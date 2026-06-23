package com.example.pcmalluserservice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.model.dto.LoginUser;
import com.example.pcmallcommon.model.entity.UserEntity;
import com.example.pcmallcommon.model.mapper.UserMapper;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmalluserservice.dao.IUserDao;
import com.example.pcmalluserservice.dao.IUserRoleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserDao userDao;

    private final IUserRoleDao userRoleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>()
                .eq("uid", username);
        UserEntity entity = userDao.selectOne(queryWrapper);
        if (entity == null) {
            throw new UsernameNotFoundException(ResponseCode.ACCOUNT_ERROR.toString());//账号错误
        }
        log.debug("用户信息查询成功！，信息为：{}", entity);
        entity.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(entity.getPassword()));
        List<String> roles = userRoleDao.findUserRole(username);
        LoginUser loginUser = new LoginUser(UserMapper.INSTANCE.toDto(entity), roles);
        log.debug("用户权限信息为：{}", JSON.toJSONString(loginUser));
        return loginUser;
    }
}
