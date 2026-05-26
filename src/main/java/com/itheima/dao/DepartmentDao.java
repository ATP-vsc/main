package com.itheima.dao;

import com.itheima.model.Department;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class DepartmentDao {
    private QueryRunner runner;

    public DepartmentDao(DataSource ds) {
        this.runner = new QueryRunner(ds);
    }

    public Department findById(Integer deptId) throws Exception {
        String sql = "SELECT * FROM departments WHERE department_id=?";
        return runner.query(sql, new BeanHandler<>(Department.class), deptId);
    }

    /**
     * 查找所有部门
     *
     * @return 部门列表
     * @throws Exception 数据库异常
     */
    public List<Department> findAll() throws Exception {
        String sql = "SELECT d.*, u.username as managerName " +
                "FROM departments d " +
                "LEFT JOIN users u ON d.manager_id = u.id " +
                "ORDER BY d.department_id";

        List<Department> departments = new ArrayList<>();

        try (Connection conn = runner.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("使用基础JDBC查询部门...");

            while (rs.next()) {
                Department department = new Department();
                department.setDepartmentId(rs.getInt("department_id"));
                department.setDepartmentName(rs.getString("department_name"));
                department.setManagerId(rs.getInt("manager_id"));
                department.setManagerName(rs.getString("managerName"));

                System.out.println("读取到部门: ID=" + department.getDepartmentId() +
                        ", 名称=" + department.getDepartmentName() +
                        ", 经理=" + department.getManagerName());

                departments.add(department);
            }

            System.out.println("基础JDBC查询完成，共" + departments.size() + "个部门");
        } catch (SQLException e) {
            System.err.println("数据库查询错误: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return departments;
    }

    /**
     * 根据用户ID查找所属部门
     *
     * @param userId 用户ID
     * @return 部门列表
     * @throws Exception 数据库异常
     */
    public List<Department> findDepartmentsByUserId(Integer userId) throws Exception {
        if (userId == null) {
            return new ArrayList<>();
        }

        String sql = "SELECT d.*, u.username as managerName " +
                "FROM departments d " +
                "LEFT JOIN users u ON d.manager_id = u.id " +
                "WHERE d.department_id IN (SELECT department_id FROM departmentmenbers WHERE user_id = ?) " +
                "ORDER BY d.department_id";

        List<Department> departments = new ArrayList<>();

        try (Connection conn = runner.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("使用基础JDBC查询用户部门...");

                while (rs.next()) {
                    Department department = new Department();
                    department.setDepartmentId(rs.getInt("department_id"));
                    department.setDepartmentName(rs.getString("department_name"));
                    department.setManagerId(rs.getInt("manager_id"));
                    department.setManagerName(rs.getString("managerName"));

                    System.out.println("读取到部门: ID=" + department.getDepartmentId() +
                            ", 名称=" + department.getDepartmentName() +
                            ", 经理=" + department.getManagerName());

                    departments.add(department);
                }

                System.out.println("基础JDBC查询完成，共" + departments.size() + "个部门");
            }
        } catch (SQLException e) {
            System.err.println("数据库查询错误: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return departments;
    }

    /**
     * 创建新部门
     *
     * @param departmentName 部门名称
     * @param managerId      管理员ID
     * @return 是否创建成功
     * @throws Exception 数据库异常
     */
    public boolean createDepartment(String departmentName, Integer managerId) throws Exception {
        String sql = "INSERT INTO departments (department_name, manager_id) VALUES (?, ?)";
        try {
            int result = runner.update(sql, departmentName, managerId);
            return result > 0;
        } catch (Exception e) {
            System.err.println("创建部门失败: " + e.getMessage());
            if (e.getMessage().contains("Duplicate")) {
                throw new Exception("部门名称已存在");
            }
            throw e;
        }
    }

    /**
     * 加入部门
     *
     * @param userId       用户ID
     * @param departmentId 部门ID
     * @return 是否加入成功
     * @throws Exception 数据库异常
     */
    public boolean joinDepartment(Integer userId, Integer departmentId) throws Exception {
        // 检查部门是否存在
        String checkDepartmentSql = "SELECT COUNT(*) FROM departments WHERE department_id = ?";
        Integer departmentCount = runner.query(checkDepartmentSql, rs -> {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }, departmentId);

        if (departmentCount == 0) {
            throw new Exception("部门不存在");
        }

        // 检查是否已加入部门
        String checkSql = "SELECT COUNT(*) FROM departmentmenbers WHERE user_id = ? AND department_id = ?";
        Integer count = runner.query(checkSql, rs -> {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }, userId, departmentId);

        if (count > 0) {
            throw new Exception("您已经加入该部门");
        }

        // 加入部门
        String sql = "INSERT INTO departmentmenbers (user_id, department_id) VALUES (?, ?)";

        try {
            int result = runner.update(sql, userId, departmentId);
            System.out.println("用户" + userId + "加入部门" + departmentId + ", 结果: " + result);
            return result > 0;
        } catch (Exception e) {
            System.err.println("加入部门失败: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 删除部门
     *
     * @param departmentId 部门ID
     * @return 是否删除成功
     * @throws Exception 数据库异常
     */
    public boolean deleteDepartment(Integer departmentId) throws Exception {
        String sql = "DELETE FROM departments WHERE department_id = ?";
        
        try {
            int result = runner.update(sql, departmentId);
            return result > 0;
        } catch (Exception e) {
            System.err.println("删除部门失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 退出部门
     *
     * @param userId       用户ID
     * @param departmentId 部门ID
     * @return 是否退出成功
     * @throws Exception 数据库异常
     */
    public boolean leaveDepartment(Integer userId, Integer departmentId) throws Exception {
        String sql = "DELETE FROM departmentmenbers WHERE user_id = ? AND department_id = ?";
        
        try {
            int result = runner.update(sql, userId, departmentId);
            return result > 0;
        } catch (Exception e) {
            System.err.println("退出部门失败: " + e.getMessage());
            throw e;
        }
    }
}