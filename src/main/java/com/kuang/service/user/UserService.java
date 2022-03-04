package com.kuang.service.user;

import com.kuang.pojo.User;

public interface UserService {
    //用户登录
    public User login(String userCode, String password);
    //根据用户id修改密码
    public Boolean updatePwd(int id, String pwd);
    //查询记录数
    public int getUserCount(String username,int userRole);

}
