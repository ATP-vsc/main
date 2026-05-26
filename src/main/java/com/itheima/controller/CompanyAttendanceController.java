package com.itheima.controller;

import com.itheima.dao.AttendanceDao;
import com.itheima.dao.UserDao;
import com.itheima.model.Attendance;
import com.itheima.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.util.Properties;

@WebServlet("/companyAttendance")
public class CompanyAttendanceController extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("CompanyAttendanceController 初始化");
        try {
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            this.userDao = new UserDao(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("初始化CompanyAttendanceController失败", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("CompanyAttendanceController doGet方法被调用");

        // 从session中获取当前用户
        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null) {
            // 用户未登录，重定向到登录页或返回错误
            response.sendRedirect("login.jsp");
            return;
        }

        System.out.println("当前用户ID: " + currentUser.getId());

        try {
            // 使用静态方法获取所有考勤信息
            List<Attendance> allAttendance = AttendanceDao.findAll();

            System.out.println("所有考勤数量: " + (allAttendance != null ? allAttendance.size() : 0));

            if (allAttendance != null) {
                for (Attendance att : allAttendance) {
                    System.out.println("考勤记录: 用户=" + att.getUserName() +
                            ", 日期=" + att.getCheckDate() +
                            ", 上班时间=" + att.getCheckInTime() +
                            ", 下班时间=" + att.getCheckOutTime() +
                            ", 状态=" + att.getStatus());
                }
            }

            request.setAttribute("allAttendance", allAttendance);
            request.getRequestDispatcher("/companyAttendance.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "查询考勤信息失败: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "系统错误: " + e.getMessage());
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
        System.out.println("CompanyAttendanceController doPost方法被调用，action: " + action);
        
        try {
            if ("promote".equals(action) || "demote".equals(action)) {
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
            } else {
                request.getSession().setAttribute("errorMessage", "非法操作");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "操作失败: " + e.getMessage());
        }
        
        // 重定向回主页
        response.sendRedirect("index.jsp");
        return;
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