package com.example.pcmalluserservice.service;

import com.example.pcmallcommon.model.dto.User;
import com.example.pcmallcommon.response.ResponseResult;

public interface IUserService {
    ResponseResult<User> login(String uid, String password);

    ResponseResult<String> register(String uid, String password);

}
