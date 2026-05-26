// src/main/java/com/itheima/service/UserService.java
package com.itheima.service;

import com.itheima.dao.UserDao;
import com.itheima.model.User;
import javax.sql.DataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.util.Properties;

public class UserService {
    private UserDao userDao;

    public UserService() {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            this.userDao = new UserDao(dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String username, String password) {
        try {
            return userDao.findByUsernameAndPassword(username, password);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isUserExist(String username) {
        try {
            return userDao.findByUsername(username) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean register(User user) {
        try {
            return userDao.save(user) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}