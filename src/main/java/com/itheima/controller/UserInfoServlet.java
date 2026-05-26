package com.itheima.controller;

import com.itheima.dao.*;
import com.itheima.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import javax.sql.DataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.util.Properties;
import org.json.JSONObject;

/**
 * 用户信息管理Servlet
 * 处理用户信息的查看、编辑和保存功能
 */
@WebServlet("/userInfo")
public class UserInfoServlet extends HttpServlet {
    // 数据访问对象，用于操作用户、角色、部门和团队数据
    private UserDao userDao;
    private RoleDao roleDao;
    private DepartmentDao departmentDao;
    private TeamDao teamDao;

    /**
     * 初始化方法，在Servlet启动时执行
     * 加载数据库连接配置，初始化所有数据访问对象
     * @throws ServletException 如果初始化失败
     */
    @Override
    public void init() throws ServletException {
        try {
            // 加载数据库连接配置文件
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("druid.properties"));
            // 创建数据源
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            // 初始化各个数据访问对象
            userDao = new UserDao(dataSource);
            roleDao = new RoleDao(dataSource);
            departmentDao = new DepartmentDao(dataSource);
            teamDao = new TeamDao(dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理GET请求，用于显示用户信息页面
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws ServletException 如果处理请求失败
     * @throws IOException 如果发生I/O错误
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 从会话中获取当前登录用户的ID
        HttpSession session = request.getSession(false);
        int userId = 1; // 默认值
        
        // 检查URL参数中的id
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                userId = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                // 如果参数不是有效的数字，则使用默认值
                System.out.println("无效的用户ID: " + idStr);
            }
        }
        
        // 如果没有从URL获取到id，且用户已登录，则使用当前登录用户ID
        if (session != null) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null && userId == 1) {
                userId = currentUser.getId();
            }
        }
        
        // 初始化用户相关对象
        User user = null;
        Role role = null;
        Department dept = null;
        Team team = null;
        try {
            // 查询用户信息及其关联的角色、部门和团队信息
            user = userDao.findById(userId);
            if(user != null){
                role = roleDao.findById(user.getRoleId());
                dept = departmentDao.findById(user.getDepartmentId());
                team = teamDao.findByUserId(user.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        // 将查询结果设置到请求属性中，供JSP页面使用
        request.setAttribute("user", user);
        request.setAttribute("role", role);
        request.setAttribute("dept", dept);
        request.setAttribute("team", team);
        
        // 检查是否为编辑请求
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            // 保持在编辑页面
        } else {
            // 清除之前的消息
            request.removeAttribute("message");
            request.removeAttribute("messageType");
        }
        
        // 转发到用户信息页面
        request.getRequestDispatcher("info.jsp").forward(request, response);
    }
    /**
     * 处理POST请求，用于处理用户信息的保存操作
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws ServletException 如果处理请求失败
     * @throws IOException 如果发生I/O错误
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查是否为保存操作
        String action = request.getParameter("action");
        if (!"save".equals(action)) {
            // 如果不是保存操作，则重定向到查看页面
            response.sendRedirect("userInfo");
            return;
        }
        
        // 获取参数
        String idStr = request.getParameter("id");
        String username = request.getParameter("username");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password"); // 新密码可选
        
        // 设置请求属性，用于在JSP页面显示消息
        try {
            int userId = Integer.parseInt(idStr);
            
            // 查询原用户，更新
            User user = userDao.findById(userId);
            if(user != null){
                // 保留原始入职时间
                java.util.Date originalHireDate = user.getHireDate();

                user.setUsername(username);
                user.setPhone(phone);
                // 只有当用户输入了新密码时才更新密码
                if(password != null && !password.trim().isEmpty()){                    user.setPassword(password);
                    System.out.println("正在更新用户密码: " + password); // 添加调试日志
                } else {
                    System.out.println("密码为空，不更新密码"); // 添加调试日志
                }

                // 确保入职时间被保留
                user.setHireDate(originalHireDate);
                userDao.updateUser(user);
                
                // 保存成功，设置会话属性并重定向到主页
                HttpSession session = request.getSession();
                session.setAttribute("refreshUserInfo", "true");
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            } else {
                // 设置失败消息
                request.setAttribute("message", "找不到该用户，保存失败");
                request.setAttribute("messageType", "error");
            }
        } catch (Exception e) {
            // 设置错误消息
            request.setAttribute("message", "保存时出错: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }
        
        // 重新获取用户信息
        doGet(request, response);
    }
}