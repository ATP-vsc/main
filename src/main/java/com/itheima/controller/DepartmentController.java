package com.itheima.controller;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.itheima.dao.DepartmentDao;
import com.itheima.model.Department;
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

@WebServlet("/department")
public class DepartmentController extends HttpServlet {
    private DepartmentDao departmentDao;

    @Override
    public void init() throws ServletException {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            this.departmentDao = new DepartmentDao(dataSource);

            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");
            if (rs.next()) {
                System.out.println("DepartmentController: 数据库连接成功");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("初始化DepartmentController失败", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("DepartmentController doGet方法被调用");

        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        System.out.println("当前用户: " + currentUser.getUsername() + ", 角色: " + currentUser.getRoleId());

        try {
            String roleId = currentUser.getRoleId();
            List<Department> departments;

            // 根据角色ID决定显示哪些部门
            if (roleId != null && (roleId.startsWith("V8") || roleId.startsWith("V7") || roleId.startsWith("V6") || roleId.startsWith("V5") || roleId.startsWith("V4") || roleId.startsWith("V3"))) {
                // v8,v7,v6: 仅显示已加入的部门
                departments = departmentDao.findDepartmentsByUserId(currentUser.getId());
                request.setAttribute("showAll", false); // 标记不是显示所有部门
            } else if (roleId != null && (roleId.startsWith("V1") || roleId.startsWith("V2") )) {
                // v5,v4,v3: 显示所有部门
                departments = departmentDao.findAll();
                request.setAttribute("showAll", true); // 标记是显示所有部门
            } else {
                // 其他角色：默认显示所有部门
                departments = departmentDao.findAll();
                request.setAttribute("showAll", true);
            }

            // 传递用户信息和部门列表到JSP
            request.setAttribute("allDepartments", departments);
            request.setAttribute("currentUser", currentUser);
            System.out.println("显示部门数量: " + (departments != null ? departments.size() : 0));
            System.out.println(departments);

            request.getRequestDispatcher("/department.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "查询部门信息失败: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String roleId = currentUser.getRoleId();

        try {
            if ("create".equals(action) && roleId != null &&
                    (roleId.startsWith("V1") || roleId.startsWith("V2"))) {
                // 创建部门
                String departmentName = request.getParameter("departmentName");
                if (departmentName != null && !departmentName.trim().isEmpty()) {
                    boolean success = departmentDao.createDepartment(departmentName, currentUser.getId());
                    if (success) {
                        // 创建成功，重定向到index页面
                        request.getSession().setAttribute("successMessage", "部门创建成功！");
                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                        return;
                    } else {
                        request.setAttribute("errorMessage", "部门创建失败！");
                    }
                } else {
                    request.setAttribute("errorMessage", "部门名称不能为空！");
                }
            } else if ("join".equals(action) && roleId != null &&
                    (roleId.startsWith("V5") || roleId.startsWith("V4") || roleId.startsWith("V3") ||
                            roleId.startsWith("V8") || roleId.startsWith("V7") || roleId.startsWith("V6"))) {
                // 加入部门
                String departmentIdStr = request.getParameter("departmentId");
                if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                    try {
                        int departmentId = Integer.parseInt(departmentIdStr);
                        boolean success = departmentDao.joinDepartment(currentUser.getId(), departmentId);
                        if (success) {
                            // 加入成功，重定向到index页面
                            request.getSession().setAttribute("successMessage", "成功加入部门！");
                            response.sendRedirect("index.jsp");
                            return;
                        } else {
                            request.setAttribute("errorMessage", "加入部门失败！");
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "无效的部门ID！");
                    }
                } else {
                    request.setAttribute("errorMessage", "部门ID不能为空！");
                }
            } else if ("delete".equals(action) && roleId != null &&
                    (roleId.startsWith("V1") || roleId.startsWith("V2"))) {
                // 删除部门
                String departmentIdStr = request.getParameter("departmentId");
                if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                    try {
                        int departmentId = Integer.parseInt(departmentIdStr);
                        boolean success = departmentDao.deleteDepartment(departmentId);
                        if (success) {
                            // 删除成功
                            request.getSession().setAttribute("successMessage", "部门删除成功！");
                            response.sendRedirect("index.jsp");
                            return;
                        } else {
                            request.setAttribute("errorMessage", "部门删除失败！");
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "无效的部门ID！");
                    } catch (Exception e) {
                        request.setAttribute("errorMessage", "删除失败: " + e.getMessage());
                    }
                } else {
                    request.setAttribute("errorMessage", "部门ID不能为空！");
                }
            } else if ("leave".equals(action) && roleId != null &&
                    (roleId.startsWith("V8") || roleId.startsWith("V7") || roleId.startsWith("V6") ||
                     roleId.startsWith("V5") || roleId.startsWith("V4") || roleId.startsWith("V3"))) {
                // 退出部门
                String departmentIdStr = request.getParameter("departmentId");
                if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                    try {
                        int departmentId = Integer.parseInt(departmentIdStr);
                        boolean success = departmentDao.leaveDepartment(currentUser.getId(), departmentId);
                        if (success) {
                            // 退出成功
                            request.getSession().setAttribute("successMessage", "成功退出部门！");
                            response.sendRedirect("index.jsp");
                            return;
                        } else {
                            request.setAttribute("errorMessage", "退出部门失败！");
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "无效的部门ID！");
                    } catch (Exception e) {
                        request.setAttribute("errorMessage", "退出失败: " + e.getMessage());
                    }
                } else {
                    request.setAttribute("errorMessage", "部门ID不能为空！");
                }
            } else {
                request.setAttribute("errorMessage", "非法操作或权限不足！");
            }

            // 如果执行到这里，说明操作失败或者参数验证失败
            // 重新获取部门数据并转发到部门页面
            List<Department> allDepartments;
            if (roleId != null && (roleId.matches("^V[8-6].*"))) {
                allDepartments = departmentDao.findDepartmentsByUserId(currentUser.getId());
                request.setAttribute("showAll", false);
            } else {
                allDepartments = departmentDao.findAll();
                request.setAttribute("showAll", true);
            }
            request.setAttribute("allDepartments", allDepartments);
            request.setAttribute("currentUser", currentUser);
            request.getRequestDispatcher("/department.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "操作失败: " + e.getMessage());

            // 失败时也要重新加载数据
            try {
                List<Department> allDepartments = departmentDao.findAll();
                request.setAttribute("allDepartments", allDepartments);
                request.setAttribute("currentUser", currentUser);
                request.setAttribute("showAll", true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            request.getRequestDispatcher("/department.jsp").forward(request, response);
        }
    }
}