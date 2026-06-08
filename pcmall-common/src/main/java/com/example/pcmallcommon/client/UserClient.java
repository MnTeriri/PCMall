package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/user")
public interface UserClient {
    @PostExchange("/updateInformation")
    ResponseResult<User> updateInformation(@RequestBody User user);

    @PostExchange("/updatePassword")
    ResponseResult<String> updatePassword(
            @RequestParam("uid") String uid,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword
    );
}
