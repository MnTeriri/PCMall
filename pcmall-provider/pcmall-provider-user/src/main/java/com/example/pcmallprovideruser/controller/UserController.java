package com.example.pcmallprovideruser.controller;

import com.example.pcmallcommon.model.User;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovideruser.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    public UserController() {
        log.debug("创建UserController对象：UserController");
    }

    @PostMapping("/updateInformation")
    public ResponseResult<User> updateInformation(@RequestBody User user) {
        return ResponseResult.ok(userService.updateInformation(user));
    }

    @PostMapping("/updatePassword")
    public ResponseResult<String> updatePassword(String uid, String oldPassword, String newPassword) {
        userService.updatePassword(uid, oldPassword, newPassword);
        return ResponseResult.ok("密码修改成功！");
    }
}
