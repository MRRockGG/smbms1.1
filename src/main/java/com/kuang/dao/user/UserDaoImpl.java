package com.kuang.dao.user;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.Role;
import com.kuang.pojo.User;
import com.kuang.service.user.UserServiceImpl;
import com.mysql.jdbc.StringUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    //得到要登录的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if (connection != null) {
            String sql = "select * from smbms_user where userCode=? ";//and userPassword = ?
            Object[] params = {userCode};

            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if (rs.next()) {

                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                // user.setUserRoleName(rs.getString("userRoleName"));

            }
            BaseDao.closeResource(null, pstm, rs);

        }


        return user;
    }
    //修改当前用户密码
    @Override
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        System.out.println("UserDaoImpl"+password);
        int execute = 0;
        PreparedStatement pstm=null;
        if(connection!=null){

            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object params[]={password,id};//Object[] params={id,password};
           execute= BaseDao.execute(connection, pstm, sql, params);//BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null,pstm,null);
        }
       return execute;
    }

    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs=null;
        int count = 0;
        if(connection!=null)
        {
            //使用流来串接sql语句
            StringBuffer sql = new StringBuffer();
            //使用ArrayList来存放sql所需要获取的参数，sql中的（？） String username, int userRole
            ArrayList<Object> list = new ArrayList<>();
            //默认参数查询sql
            sql.append("select  count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
            //userName参数查询
            if(!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");//index :0

            }
            if(userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);//index = 1;

            }

            //怎么把list转换为数组
            Object[] params = list.toArray();
            //日志，输出sql验证
            System.out.println("UserDaoImpl->getUserCount:"+sql.toString());

            rs = BaseDao.execute(connection,pstm,rs,sql.toString(),params);
            System.out.println("rs长啥样？"+rs);
            if (rs.next()){
                //从结果集中获取最终的数量
                count = rs.getInt("count");

            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs=null;
        List<User>  userList=new ArrayList<>();
        if(connection!=null)
        {
            //使用流来串接sql语句
            StringBuffer sql = new StringBuffer();
            //默认参数查询sql
            sql.append("select  u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object>  list=new ArrayList<>();
            //userName参数查询
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");//index :0

            }
            if(userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);//index = 1;

            }

            //分页使用limit
            sql.append("order by creationDate DESC limit ?,?");
            currentPageNo=(currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            //怎么把list转换为数组
            Object[] params = list.toArray();
            //日志，输出sql验证
            System.out.println("UserDaoImpl->getUserCount:"+sql.toString());
            System.out.println("sql-->"+sql.toString());
            rs = BaseDao.execute(connection,pstm,rs,sql.toString(),params);
            ;

            if (rs.next()){
                //从结果集中获取最终的数量
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));

                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));


                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return userList;
    }



    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount(null, 0);
        System.out.println(userCount);
    }



}
