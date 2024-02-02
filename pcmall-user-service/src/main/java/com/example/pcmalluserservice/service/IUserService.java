package com.example.pcmalluserservice.service;

import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseResult;

public interface IUserService {
    public ResponseResult<User> login(String uid, String password);

    public ResponseResult<String> register(String uid, String password);

}
