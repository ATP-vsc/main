package com.itheima.dao;

import com.itheima.model.User;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户数据访问对象类
 * 提供对用户数据的增删改查操作
 * 使用Apache Commons DbUtils简化JDBC操作
 */
public class UserDao {
    private QueryRunner runner;

    /**
     * 构造函数，初始化QueryRunner
     * 
     * @param ds 数据源
     */
    public UserDao(DataSource ds) {
        this.runner = new QueryRunner(ds);
    }

    /**
     * 根据用户名和密码查找用户
     * 
     * @param username 用户名
     * @param password 密码
     * @return 匹配的User对象，如果未找到则返回null
     * @throws Exception 数据库访问异常
     */
    public User findByUsernameAndPassword(String username, String password) throws Exception {
        // 使用自定义的ResultSetHandler处理hire_date字段
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        return runner.query(sql, new UserResultSetHandler(), username, password);
    }

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 匹配的User对象，如果未找到则返回null
     * @throws Exception 数据库访问异常
     */
    public User findByUsername(String username) throws Exception {
        // 使用自定义的ResultSetHandler处理hire_date字段
        String sql = "SELECT * FROM users WHERE username=?";
        return runner.query(sql, new UserResultSetHandler(), username);
    }

    /**
     * 根据用户ID查找用户
     * 
     * @param id 用户ID
     * @return 匹配的User对象，如果未找到则返回null
     * @throws Exception 数据库访问异常
     */
    public User findById(int id) throws Exception {
        // 使用自定义的ResultSetHandler处理hire_date字段
        String sql = "SELECT * FROM users WHERE id=?";
        User user = runner.query(sql, new UserResultSetHandler(), id);
        if (user != null) {
            System.out.println("用户ID: " + user.getId());
            System.out.println("用户名: " + user.getUsername());
            System.out.println("入职时间: " + user.getHireDate());
        } else {
            System.out.println("未找到ID为 " + id + " 的用户");
        }
        return user;
    }

    /**
     * 保存新用户到数据库
     * 
     * @param user 用户对象
     * @return 受影响的行数
     * @throws Exception 数据库访问异常
     */
    public int save(User user) throws Exception {
        String sql = "INSERT INTO users(username, password, phone, role_id, hire_date, status) VALUES (?, ?, ?, ?, ?, '在职')";
        // 确保hireDate不为null
        java.util.Date hireDate = user.getHireDate();
        if (hireDate == null) {
            hireDate = new java.util.Date(); // 使用当前日期作为默认值
        }
        return runner.update(sql, user.getUsername(), user.getPassword(), user.getPhone(), user.getRoleId(), new java.sql.Timestamp(hireDate.getTime()));
    }

    /**
     * 更新用户信息
     * 
     * @param user 用户对象
     * @return 受影响的行数
     * @throws Exception 数据库访问异常
     */
    public int updateUser(User user) throws Exception {
        // 密码为null或空时，不更新密码
        boolean updatePassword = user.getPassword() != null && !user.getPassword().trim().isEmpty();
        
        String sql;
        if (updatePassword) {
            sql = "UPDATE users SET username=?, phone=?, password=?, hire_date=?, role_id=? WHERE id=?";
        } else {
            sql = "UPDATE users SET username=?, phone=?, hire_date=?, role_id=? WHERE id=?";
        }
        
        // 确保hireDate不为null
        java.util.Date hireDate = user.getHireDate();
        if (hireDate == null) {
            // 如果hireDate为null，从数据库获取原始值
            try {
                User originalUser = findById(user.getId());
                if (originalUser != null) {
                    hireDate = originalUser.getHireDate();
                }
            } catch (Exception e) {
                System.out.println("获取原始入职时间失败: " + e.getMessage());
            }
            // 如果还是null，使用当前日期
            if (hireDate == null) {
                hireDate = new java.util.Date();
            }
        }
        
        System.out.println("执行SQL: " + sql); // 添加调试日志
        if (updatePassword) {
            System.out.println("参数: " + user.getUsername() + ", " + user.getPhone() + ", " + user.getPassword() + ", " + hireDate + ", " + user.getRoleId() + ", " + user.getId()); // 添加调试日志
            return runner.update(sql, user.getUsername(), user.getPhone(), user.getPassword(), new java.sql.Timestamp(hireDate.getTime()), user.getRoleId(), user.getId());
        } else {
            System.out.println("参数: " + user.getUsername() + ", " + user.getPhone() + ", " + hireDate + ", " + user.getRoleId() + ", " + user.getId()); // 添加调试日志
            return runner.update(sql, user.getUsername(), user.getPhone(), new java.sql.Timestamp(hireDate.getTime()), user.getRoleId(), user.getId());
        }
    }
    
    /**
     * 根据用户ID删除用户
     * 
     * @param id 用户ID
     * @return 受影响的行数
     * @throws Exception 数据库访问异常
     */
    public int deleteById(int id) throws Exception {
        String sql = "DELETE FROM users WHERE id=?";
        return runner.update(sql, id);
    }
    
    /**
     * 查询所有用户信息，包含角色和部门信息
     * 
     * @return 用户列表
     * @throws Exception 数据库访问异常
     */
    public List<User> findAll() throws Exception {
        String sql = "SELECT u.*, r.role_name as roleName, d.department_name as departmentName " +
                "FROM users u " +
                "LEFT JOIN roles r ON u.role_id = r.role_id " +
                "LEFT JOIN departments d ON u.department_id = d.department_id";

        System.out.println("执行SQL: " + sql);

        return runner.query(sql, rs -> {
            List<User> users = new java.util.ArrayList<>();

            while (rs.next()) {
                User user = new User();

                // 映射基本字段
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getString("role_id"));
                user.setPhone(rs.getString("phone"));

                // 处理可能为null的字段
                Object positionId = rs.getObject("position_id");
                if (positionId != null) {
                    user.setPositionId((Integer) positionId);
                }

                Object departmentId = rs.getObject("department_id");
                if (departmentId != null) {
                    user.setDepartmentId((Integer) departmentId);
                }

                user.setHireDate(rs.getTimestamp("hire_date"));
                user.setStatus(rs.getString("status"));

                // 设置JOIN查询的字段
                user.setRoleName(rs.getString("roleName"));
                user.setDepartmentName(rs.getString("departmentName"));

                // 调试信息
                System.out.println("用户: " + user.getUsername() +
                        ", 角色ID: " + user.getRoleId() +
                        ", 角色名称: " + user.getRoleName() +
                        ", 部门ID: " + user.getDepartmentId() +
                        ", 部门名称: " + user.getDepartmentName());

                users.add(user);
            }

            System.out.println("查询完成，共" + users.size() + "个用户");
            return users;
        });
    }
    
    /**
     * 用户结果集处理器内部类
     * 用于将数据库查询结果转换为User对象
     * 继承了通用的BaseResultSetHandler基类
     */
    private class UserResultSetHandler extends BaseResultSetHandler<User> {
        /**
         * 处理结果集，将当前行数据转换为User对象
         * 
         * @param rs 数据库查询结果集
         * @return 转换后的User对象，如果结果集没有更多行则返回null
         * @throws SQLException 当数据库访问错误或结果集处理错误时抛出
         */
        @Override
        public User handle(ResultSet rs) throws SQLException {
            // 检查结果集是否还有下一行数据
            if (rs.next()) {
                // 创建新的User对象用于存储当前行的数据
                User user = new User();
                
                // 设置用户的基本信息
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getString("role_id"));
                user.setPhone(rs.getString("phone"));

                // 使用基类方法处理可能为NULL的整数类型字段
                user.setPositionId(getIntegerValue(rs, "position_id"));
                user.setDepartmentId(getIntegerValue(rs, "department_id"));

                // 处理hire_date字段，该字段存储员工的入职时间
                user.setHireDate(rs.getTimestamp("hire_date"));

                // 设置用户状态信息
                user.setStatus(rs.getString("status"));
                
                // 返回填充完数据的User对象
                return user;
            }
            // 如果结果集没有更多行，返回null
            return null;
        }
    }
}