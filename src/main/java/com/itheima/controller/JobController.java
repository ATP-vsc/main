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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 职务信息管理Servlet
 * 处理职务信息的查看功能
 */
@WebServlet("/job")
public class JobController extends HttpServlet {
    // 数据访问对象，用于操作用户和角色数据
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
     * 处理GET请求，用于显示职务信息页面
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws ServletException 如果处理请求失败
     * @throws IOException 如果发生I/O错误
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("JobController: 接收到GET请求");
        
        // 从会话中获取当前登录用户的ID
        HttpSession session = request.getSession(false);
        int userId = 1; // 默认值
        
        if (session != null) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                userId = currentUser.getId();
                System.out.println("当前用户ID: " + userId);
            }
        }
        
        // 初始化用户和角色对象
        User user = null;
        Role role = null;
        try {
            // 查询用户信息及其关联的角色信息
            user = userDao.findById(userId);
            if(user != null){
                role = roleDao.findById(user.getRoleId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        // 将查询结果设置到请求属性中，供JSP页面使用
        request.setAttribute("user", user);
        request.setAttribute("role", role);
        
        // 获取并设置用户的升职申请记录
        if (user != null) {
            java.util.List<PromotionRequestDao.PromotionRequestRecord> promotionRequests = 
                promotionRequestDao.getPromotionRequestsByUserId(user.getId());
            request.setAttribute("promotionRequests", promotionRequests);
        }
        
        // 转发到职务信息页面
        request.getRequestDispatcher("job.jsp").forward(request, response);



    }
    
    /**
     * 处理POST请求，用于处理职务相关的操作
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws ServletException 如果处理请求失败
     * @throws IOException 如果发生I/O错误
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("JobController: 接收到POST请求");
        
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
        
        if ("promotionRequest".equals(action)) {
            System.out.println("处理升职申请请求");
            handlePromotionRequest(request, response, currentUser);
        } else {
            System.out.println("无效的操作: " + action);
            // 返回错误信息
            PrintWriter out = response.getWriter();
            out.print("{\"success\":false,\"message\":\"无效的操作\"}");
            out.flush();
        }
    }
    
    /**
     * 处理升职申请
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param user 当前用户
     * @throws IOException 如果发生I/O错误
     */
    private void handlePromotionRequest(HttpServletRequest request, HttpServletResponse response, User user) 
            throws IOException {
        System.out.println("开始处理升职申请，用户: " + user.getUsername());
        PrintWriter out = response.getWriter();
        try {
            // 准备要写入的数据
            String currentRoleId = user.getRoleId();
            // 暂时设定目标角色为当前角色的上一级（实际应用中可能需要更复杂的逻辑）
            String targetRoleId = getNextRoleLevel(currentRoleId);
            
            System.out.println("当前角色ID: " + currentRoleId + ", 目标角色ID: " + targetRoleId);
            
            // 保存到数据库
            boolean success = promotionRequestDao.savePromotionRequest(user.getId(), currentRoleId, targetRoleId);
            
            if (success) {
                // 设置成功消息
                System.out.println("升职申请处理成功");
                request.setAttribute("message", "升职申请已提交成功！");
                request.setAttribute("messageType", "success");
                
                // 重新获取数据并转发到页面
                User updatedUser = userDao.findById(user.getId());
                Role updatedRole = null;
                if(updatedUser != null){
                    updatedRole = roleDao.findById(updatedUser.getRoleId());
                }
                
                // 将更新后的数据设置到请求属性中
                request.setAttribute("user", updatedUser);
                request.setAttribute("role", updatedRole);
                
                // 获取并设置用户的升职申请记录
                java.util.List<PromotionRequestDao.PromotionRequestRecord> promotionRequests = 
                    promotionRequestDao.getPromotionRequestsByUserId(user.getId());
                request.setAttribute("promotionRequests", promotionRequests);
                
                // 转发到页面显示成功信息
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "数据库保存失败");
                request.setAttribute("messageType", "error");
                
                // 重新获取数据并转发到页面
                User updatedUser = userDao.findById(user.getId());
                Role updatedRole = null;
                if(updatedUser != null){
                    updatedRole = roleDao.findById(updatedUser.getRoleId());
                }
                
                // 将更新后的数据设置到请求属性中
                request.setAttribute("user", updatedUser);
                request.setAttribute("role", updatedRole);
                
                // 获取并设置用户的升职申请记录
                java.util.List<PromotionRequestDao.PromotionRequestRecord> promotionRequests = 
                    promotionRequestDao.getPromotionRequestsByUserId(user.getId());
                request.setAttribute("promotionRequests", promotionRequests);
                
                // 转发到页面显示错误信息
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("升职申请处理失败: " + e.getMessage());
            e.printStackTrace();
            // 设置错误消息
            request.setAttribute("message", "申请提交失败：" + e.getMessage());
            request.setAttribute("messageType", "error");
            
            try {
                // 重新获取数据并转发到页面
                User updatedUser = userDao.findById(user.getId());
                Role updatedRole = null;
                if(updatedUser != null){
                    updatedRole = roleDao.findById(updatedUser.getRoleId());
                }
                
                // 将更新后的数据设置到请求属性中
                request.setAttribute("user", updatedUser);
                request.setAttribute("role", updatedRole);
                
                // 获取并设置用户的升职申请记录
                java.util.List<PromotionRequestDao.PromotionRequestRecord> promotionRequests = 
                    promotionRequestDao.getPromotionRequestsByUserId(user.getId());
                request.setAttribute("promotionRequests", promotionRequests);
                
                // 转发到页面显示错误信息
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    /**
     * 获取下一个角色等级
     * @param currentRoleId 当前角色ID
     * @return 下一个角色ID
     */
    private String getNextRoleLevel(String currentRoleId) {
        // 根据当前角色返回下一个更高级别的角色
        // V8(实习生) -> V7(助理) -> V6(工程师) -> V5(组长) -> V4(总监) -> V3(经理) -> V2(董事) -> V1(CEO)
        switch (currentRoleId) {
            case "V8": return "V7"; // 实习生 -> 助理
            case "V7": return "V6"; // 助理 -> 工程师
            case "V6": return "V5"; // 工程师 -> 组长
            case "V5": return "V4"; // 组长 -> 总监
            case "V4": return "V3"; // 总监 -> 经理
            case "V3": return "V2"; // 经理 -> 董事
            case "V2": return "V1"; // 董事 -> CEO
            default: return currentRoleId; // 如果已经是最高级别，则返回当前级别
        }
    }
}