package com.example.pcmalluserservice.service;

import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseResult;

public interface IUserService {
//    public String login(String uid, String password);

    public ResponseResult<User> login(String uid, String password);

    public String register();

}
