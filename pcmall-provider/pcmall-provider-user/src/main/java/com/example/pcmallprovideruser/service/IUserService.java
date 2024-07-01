package com.example.pcmallprovideruser.service;

import com.example.pcmallcommon.model.User;

public interface IUserService {
    User updateInformation(User user);

    void updatePassword(String uid, String oldPassword, String newPassword);

}
