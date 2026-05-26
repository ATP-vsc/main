package com.itheima.controller;

import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.dao.PromotionRequestDao;
import com.itheima.model.Role;
import com.itheima.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.*;
import java.util.Properties;
import java.util.List;

/**
 * 职务异变管理Servlet
 * 处理职务异变申请的审核和处理功能
 */
@WebServlet("/jobChange")
public class JobChangeController extends HttpServlet {
    // 数据访问对象，用于操作用户、角色和升职申请数据
    private UserDao userDao;
    private RoleDao roleDao;
    private PromotionRequestDao promotionRequestDao;

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
            promotionRequestDao = new PromotionRequestDao(dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理GET请求，用于显示职务异变管理页面
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws ServletException 如果处理请求失败
     * @throws IOException 如果发生I/O错误
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("JobChangeController: 接收到GET请求");
        
        // 从会话中获取当前登录用户
        HttpSession session = request.getSession(false);
        User currentUser = null;
        
        if (session != null) {
            currentUser = (User) session.getAttribute("user");
        }
        
        // 如果用户未登录，重定向到登录页面
        if (currentUser == null) {
            System.out.println("用户未登录，重定向到登录页面");
            response.sendRedirect("login.jsp");
            return;
        }
        
        // 获取所有待处理的升职申请记录
        List<PromotionRequestDao.PromotionRequestRecord> pendingRequests = 
            promotionRequestDao.getPromotionRequestsByStatus("待审批");
        
        // 获取所有用户信息用于显示用户名
        for (PromotionRequestDao.PromotionRequestRecord record : pendingRequests) {
            try {
                User user = userDao.findById(record.getUserId());
                if (user != null) {
                    record.setUserName(user.getUsername());
                }
            } catch (Exception e) {
                System.err.println("获取用户信息失败: " + e.getMessage());
            }
        }
        
        // 将数据设置到请求属性中，供JSP页面使用
        request.setAttribute("pendingRequests", pendingRequests);
        request.setAttribute("currentUser", currentUser);
        
        // 转发到职务异变管理页面
        request.getRequestDispatcher("jobChange.jsp").forward(request, response);
    }
    
    /**
     * 处理POST请求，用于处理职务异变相关的操作
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws ServletException 如果处理请求失败
     * @throws IOException 如果发生I/O错误
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("JobChangeController: 接收到POST请求");
        
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // 从会话中获取当前登录用户
        HttpSession session = request.getSession(false);
        User currentUser = null;
        
        if (session != null) {
            currentUser = (User) session.getAttribute("user");
        }
        
        // 如果用户未登录，返回错误信息
        if (currentUser == null) {
            System.out.println("用户未登录");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("{\"success\":false,\"message\":\"用户未登录\"}");
            out.flush();
            return;
        }
        
        String action = request.getParameter("action");
        System.out.println("接收到action参数: " + action);
        
        if ("approve".equals(action)) {
            System.out.println("处理升职申请批准请求");
            handleApproveRequest(request, response, currentUser);
        } else if ("reject".equals(action)) {
            System.out.println("处理升职申请拒绝请求");
            handleRejectRequest(request, response, currentUser);
        } else {
            System.out.println("无效的操作: " + action);
            // 返回错误信息
            PrintWriter out = response.getWriter();
            out.print("{\"success\":false,\"message\":\"无效的操作\"}");
            out.flush();
        }
    }
    
    /**
     * 处理升职申请批准
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param adminUser 当前管理员用户
     * @throws IOException 如果发生I/O错误
     */
    private void handleApproveRequest(HttpServletRequest request, HttpServletResponse response, User adminUser) 
            throws IOException {
        System.out.println("开始处理升职申请批准，管理员: " + adminUser.getUsername());
        PrintWriter out = response.getWriter();
        
        try {
            // 获取申请ID
            String requestIdStr = request.getParameter("requestId");
            if (requestIdStr == null || requestIdStr.trim().isEmpty()) {
                System.out.println("申请ID为空");
                out.print("{\"success\":false,\"message\":\"申请ID不能为空\"}");
                out.flush();
                return;
            }
            
            int requestId = Integer.parseInt(requestIdStr);
            
            // 获取申请记录
            PromotionRequestDao.PromotionRequestRecord requestRecord = 
                promotionRequestDao.getPromotionRequestById(requestId);
            
            if (requestRecord == null) {
                System.out.println("未找到ID为 " + requestId + " 的申请记录");
                out.print("{\"success\":false,\"message\":\"未找到对应的申请记录\"}");
                out.flush();
                return;
            }
            
            // 更新用户角色
            User user = userDao.findById(requestRecord.getUserId());
            if (user == null) {
                System.out.println("未找到ID为 " + requestRecord.getUserId() + " 的用户");
                out.print("{\"success\":false,\"message\":\"未找到对应的用户\"}");
                out.flush();
                return;
            }
            
            // 更新用户的角色
            user.setRoleId(requestRecord.getTargetRoleId());
            int result = userDao.updateUser(user);
            
            if (result > 0) {
                // 更新申请状态为已批准
                boolean updateStatusResult = promotionRequestDao.updatePromotionRequestStatus(requestId, "已批准", adminUser.getId());
                
                if (updateStatusResult) {
                    System.out.println("升职申请批准成功，用户ID: " + user.getId() + ", 新角色: " + requestRecord.getTargetRoleId());
                    response.sendRedirect("index.jsp");
                    return;
                } else {
                    System.out.println("更新申请状态失败");
                    response.sendRedirect("index.jsp");
                    return;
                }
            } else {
                System.out.println("更新用户角色失败");
                response.sendRedirect("index.jsp");
                return;
            }
            
        } catch (NumberFormatException e) {
            System.err.println("申请ID格式错误: " + e.getMessage());
            out.print("{\"success\":false,\"message\":\"申请ID格式错误\"}");
            out.flush();
        } catch (Exception e) {
            System.err.println("升职申请批准失败: " + e.getMessage());
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"申请批准失败：" + e.getMessage() + "\"}");
            out.flush();
        }
    }
    
    /**
     * 处理升职申请拒绝
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param adminUser 当前管理员用户
     * @throws IOException 如果发生I/O错误
     */
    private void handleRejectRequest(HttpServletRequest request, HttpServletResponse response, User adminUser) 
            throws IOException {
        System.out.println("开始处理升职申请拒绝，管理员: " + adminUser.getUsername());
        PrintWriter out = response.getWriter();
        
        try {
            // 获取申请ID
            String requestIdStr = request.getParameter("requestId");
            if (requestIdStr == null || requestIdStr.trim().isEmpty()) {
                System.out.println("申请ID为空");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }
            
            int requestId = Integer.parseInt(requestIdStr);
            
            // 更新申请状态为已拒绝
            boolean result = promotionRequestDao.updatePromotionRequestStatus(requestId, "已拒绝", adminUser.getId());
            
            if (result) {
                System.out.println("升职申请拒绝成功，申请ID: " + requestId);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                System.out.println("更新申请状态失败");
                out.print("{\"success\":false,\"message\":\"更新申请状态失败\"}");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("申请ID格式错误: " + e.getMessage());
            out.print("{\"success\":false,\"message\":\"申请ID格式错误\"}");
            out.flush();
        } catch (Exception e) {
            System.err.println("升职申请拒绝失败: " + e.getMessage());
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"申请拒绝失败：" + e.getMessage() + "\"}");
            out.flush();
        }
    }
}