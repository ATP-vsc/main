package com.itheima.dao;

import com.itheima.model.Role;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDao {
    private QueryRunner runner;
    
    public RoleDao(DataSource ds) { 
        this.runner = new QueryRunner(ds); 
    }

    public Role findById(String roleId) throws Exception {
        String sql = "SELECT * FROM roles WHERE role_id=?";
        Role role = runner.query(sql, new RoleResultSetHandler(), roleId);
        if (role != null) {
            System.out.println("角色ID: " + role.getRoleId());
            System.out.println("角色名称: " + role.getRoleName());
        } else {
            System.out.println("未找到ID为 " + roleId + " 的角色");
        }
        return role;
    }
    
    /**
     * 内部类，用于处理角色查询结果集
     */
    private class RoleResultSetHandler extends BaseResultSetHandler<Role> {
        @Override
        public Role handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getString("role_id"));
                role.setRoleName(rs.getString("role_name"));
                return role;
            }
            return null;
        }
    }
}