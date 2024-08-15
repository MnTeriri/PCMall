package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "userClient", value = "pcmall-provider-user")
public interface UserClient {
    @PostMapping("/user/updateInformation")
    ResponseResult<User> updateInformation(@RequestBody User user);

    @PostMapping("/user/updatePassword")
    ResponseResult<String> updatePassword(
            @RequestParam("uid") String uid,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword);
}
