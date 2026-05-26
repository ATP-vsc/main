package com.itheima.controller;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.itheima.dao.UserDao;
import com.itheima.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

@WebServlet("/personnelmanagement")
public class PersonnelManagementController extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            this.userDao = new UserDao(dataSource);

            // 测试连接
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");
            if(rs.next()){
                System.out.println("数据库连接成功");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("初始化PersonnelManagementController失败", e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("PersonnelManagementController doGet方法被调用");

        // 从session中获取当前用户
        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null) {
            // 用户未登录，重定向到登录页或返回错误
            response.sendRedirect("login.jsp");
            return;
        }

        System.out.println("当前用户ID: " + currentUser.getId());

        try {
            // 获取所有员工信息
            List<User> allUsers = userDao.findAll();
            request.setAttribute("allUsers", allUsers);
            System.out.println("所有员工数量: " + (allUsers != null ? allUsers.size() : 0));

            // 发到JSP页面
            request.getRequestDispatcher("/personnelmanagement.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "查询员工信息失败: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 从session中获取当前用户
        User currentUser = (User) request.getSession().getAttribute("user");
        
        if (currentUser == null) {
            // 用户未登录，重定向到登录页
            response.sendRedirect("login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        System.out.println("PersonnelManagementController doPost方法被调用，action: " + action);
        
        try {
            if ("edit".equals(action)) {
                // 修改员工信息
                String userIdStr = request.getParameter("userId");
                if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                    int userId = Integer.parseInt(userIdStr);
                    // 可以跳转到编辑页面或模态框处理
                    response.sendRedirect("userInfo?id=" + userId);
                    return;
                } else {
                    request.getSession().setAttribute("errorMessage", "用户ID不能为空");
                    response.sendRedirect("index.jsp");
                    return;
                }
            } else if ("delete".equals(action)) {
                // 删除员工
                String userIdStr = request.getParameter("userId");
                if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                    int userId = Integer.parseInt(userIdStr);
                    // 执行删除操作
                    userDao.deleteById(userId);
                    request.getSession().setAttribute("successMessage", "成功删除员工ID为 " + userId + " 的员工");
                } else {
                    request.getSession().setAttribute("errorMessage", "用户ID不能为空");
                }
                // 重定向回主页
                response.sendRedirect("index.jsp");
                return;
            } else if ("promote".equals(action) || "demote".equals(action)) {
                // 升职或降职操作
                String userIdStr = request.getParameter("userId");
                if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                    int userId = Integer.parseInt(userIdStr);
                    
                    // 获取用户信息
                    User user = userDao.findById(userId);
                    if (user != null) {
                        String currentRoleId = user.getRoleId();
                        
                        // 计算新的角色ID
                        String newRoleId = calculateNewRoleId(currentRoleId, "promote".equals(action));
                        
                        // 更新用户角色
                        user.setRoleId(newRoleId);
                        userDao.updateUser(user);
                        
                        String message = "promote".equals(action) ? "升职" : "降职";
                        request.getSession().setAttribute("successMessage", "用户 " + userId + " 已成功" + message + "，新职位为 " + newRoleId);
                    } else {
                        request.getSession().setAttribute("errorMessage", "未找到用户ID为 " + userId + " 的用户");
                    }
                } else {
                    request.getSession().setAttribute("errorMessage", "用户ID不能为空");
                }
                // 重定向回主页
                response.sendRedirect("index.jsp");
                return;
            } else {
                request.getSession().setAttribute("errorMessage", "非法操作");
                response.sendRedirect("index.jsp");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "操作失败: " + e.getMessage());
            // 重定向回主页
            response.sendRedirect("index.jsp");
            return;
        }
    }
    
    /**
     * 计算新的角色ID
     * @param currentRoleId 当前角色ID
     * @param isPromote 是否是升职操作
     * @return 新的角色ID
     */
    private String calculateNewRoleId(String currentRoleId, boolean isPromote) {
        // 角色ID从V1到V8，V1为最高级
        if (currentRoleId != null && currentRoleId.startsWith("V")) {
            try {
                int level = Integer.parseInt(currentRoleId.substring(1));
                if (isPromote) {
                    // 升职，数字减小（V3 -> V2）
                    level = Math.max(1, level - 1);
                } else {
                    // 降职，数字增大（V3 -> V4）
                    level = Math.min(8, level + 1);
                }
                return "V" + level;
            } catch (NumberFormatException e) {
                // 解析失败，默认返回V8
                return "V8";
            }
        }
        // 默认返回V8
        return "V8";
    }
}