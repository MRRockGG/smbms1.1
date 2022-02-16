package com.kuang.service.user;

import com.kuang.pojo.User;

public interface UserService {
    public User login(String userCode, String password);

}
