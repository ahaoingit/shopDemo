package com.brianyi.service;


import com.brianyi.dao.UserDao;
import com.brianyi.entity.Result;
import com.brianyi.entity.User;
import com.brianyi.utils.UUIDUtils;

import java.sql.SQLException;
import java.util.Date;

/**
 * TODO
 *
 * @author ahao 2020-10-23
 */
public class UserService {

    public Result register(String username, String password, String email) {
        Result result = new Result();
        User user = new User();
        user.setUname(username);
        user.setPassword(password);
        user.setRegisterTime( new Date());
        user.setUid(UUIDUtils.getUUID());
        user.setEmail(email);
        UserDao userDao = new UserDao();
        try {
            if (userDao.findUserByUsername(user) == null) {
                result.setCode(userDao.insertUser(user));
            }else {
                result.setCode(Result.FAILS);
                result.setMessage("注册失败,此用户名已被注册.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public Result login(String username, String password) {
        UserDao userDao = new UserDao();
        Result result = new Result();
        User user = new User();
        user.setUname(username);
        user.setPassword(password);
        try {
            result.setCode(userDao.findUserForLogin(user)==null?0:1);
            result.setMessage(userDao.findUserForLogin(user)==null?"用户名或者密码错误":"登录成功");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
