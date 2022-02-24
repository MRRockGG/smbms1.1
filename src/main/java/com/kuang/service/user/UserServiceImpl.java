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

    @Override
    public Boolean updatePwd(int id, String pwd) {
        System.out.println("UserServiceImpl"+pwd);
        Connection connection = null;
        Boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if(userDao.updatePwd(connection,id,pwd)>0){

               flag = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Test
    public void test() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        User admin = userServiceImpl.login("admin", "111111");

        System.out.println(admin.getUserPassword());


    }
}
