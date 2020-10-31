package com.brianyi.dao;

import com.brianyi.entity.User;
import com.brianyi.utils.DruidUtils;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * TODO
 *
 * @author ahao 2020-10-23
 */
public class UserDao {
    QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());
    private final BeanProcessor beanProcessor = new GenerousBeanProcessor();
    private final RowProcessor processor = new BasicRowProcessor(beanProcessor);
    public User findUserByUsername(User user) throws SQLException {
        String sql = "select * from userdb where uname = ?";
        return   queryRunner.query(sql, new BeanHandler<>(User.class), user.getUname());
    }

    public int insertUser(User user) throws SQLException {
        String sql = "insert into userdb values(?,?,?,?,?)";
        int update = queryRunner.update(sql, user.getUid(), user.getUname(), user.getPassword(), user.getEmail(), user.getRegisterTime());
        return update;
    }

    public User findUserForLogin(User user) throws SQLException {
        String sql = "select * from userdb where uname = ? and password = ?";
        User query = queryRunner.query(sql, new BeanHandler<>(User.class), user.getUname(), user.getPassword());
        return query;
    }
}
