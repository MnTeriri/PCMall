package com.example.pcmallprovideruser.service;

import com.example.pcmallcommon.model.User;

public interface IUserService {
    public User updateInformation(User user);

    public void updatePassword(String uid, String oldPassword, String newPassword);

}
