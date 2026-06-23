package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.client.UserClient;
import com.example.pcmallcommon.model.dto.User;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping("/updateInformation")
    public ResponseResult<User> updateInformation(@RequestBody User user) {
        return userClient.updateInformation(user);
    }

    @PostMapping("/updatePassword")
    public ResponseResult<String> updatePassword(String uid, String oldPassword, String newPassword) {
        return userClient.updatePassword(uid, oldPassword, newPassword);
    }
}
