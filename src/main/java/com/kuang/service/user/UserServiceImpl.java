package com.kuang.service.user;

import com.kuang.dao.BaseDao;
import com.kuang.dao.user.UserDao;
import com.kuang.dao.user.UserDaoImp;
import com.kuang.pojo.User;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    //引入dao层
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImp();
    }

    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = new User();

        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }

        return user;

    }

    @Test
    public void test() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        User admin = userServiceImpl.login("admin", "111111");

        System.out.println(admin.getUserPassword());


    }
}
