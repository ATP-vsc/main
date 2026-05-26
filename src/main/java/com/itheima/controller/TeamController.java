package com.itheima.controller;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.itheima.dao.TeamDao;
import com.itheima.model.Team;
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

@WebServlet("/team")
public class TeamController extends HttpServlet {
    private TeamDao teamDao;

    @Override
    public void init() throws ServletException {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            this.teamDao = new TeamDao(dataSource);

            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");
            if(rs.next()){
                System.out.println("TeamController: 数据库连接成功");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("初始化TeamController失败", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("TeamController doGet方法被调用");

        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        System.out.println("当前用户: " + currentUser.getUsername() + ", 角色: " + currentUser.getRoleId());

        try {
            String roleId = currentUser.getRoleId();
            List<Team> teams;

            // 根据角色ID决定显示哪些团队
            if (roleId != null && (roleId.startsWith("V8") || roleId.startsWith("V7") || roleId.startsWith("V6"))) {
                // v8,v7,v6: 仅显示已加入的团队
                teams = teamDao.findTeamsByUserId(currentUser.getId());
                request.setAttribute("showAll", false); // 标记不是显示所有团队
            } else if (roleId != null && (roleId.startsWith("V5") || roleId.startsWith("V4") || roleId.startsWith("V3"))) {
                // v5,v4,v3: 显示所有团队
                teams = teamDao.findAllBasic();
                request.setAttribute("showAll", true); // 标记是显示所有团队
            } else {
                // 其他角色：默认显示所有团队
                teams = teamDao.findAllBasic();
                request.setAttribute("showAll", true);
            }

            // 传递用户信息和团队列表到JSP
            request.setAttribute("allTeams", teams);
            request.setAttribute("currentUser", currentUser);
            System.out.println("显示团队数量: " + (teams != null ? teams.size() : 0));

            request.getRequestDispatcher("/team.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "查询团队信息失败: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * 处理团队相关的POST请求，包括创建、加入、退出和解散团队等功能
     * 
     * @param request  HttpServletRequest对象，包含客户端请求信息
     * @param response HttpServletResponse对象，用于向客户端发送响应
     * @throws ServletException 当Servlet处理请求出现错误时抛出
     * @throws IOException      当输入输出操作出现错误时抛出
     */
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
                    (roleId.startsWith("V5") || roleId.startsWith("V4") || roleId.startsWith("V3"))) {
                // 创建团队
                String teamName = request.getParameter("teamName");
                if (teamName != null && !teamName.trim().isEmpty()) {
                    boolean success = teamDao.createTeam(teamName, currentUser.getId());
                    if (success) {
                        // 创建成功，重定向到index页面
                        request.getSession().setAttribute("successMessage", "团队创建成功！");
                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                        return;
                    } else {
                        request.setAttribute("errorMessage", "团队创建失败！");
                    }
                } else {
                    request.setAttribute("errorMessage", "团队名称不能为空！");
                }
            } else if ("join".equals(action) && roleId != null &&
                    (roleId.startsWith("v8") || roleId.startsWith("v7") || roleId.startsWith("v6") ||
                            roleId.startsWith("V8") || roleId.startsWith("V7") || roleId.startsWith("V6"))) {
                // 加入团队
                String teamIdStr = request.getParameter("teamId");
                if (teamIdStr != null && !teamIdStr.trim().isEmpty()) {
                    try {
                        int teamId = Integer.parseInt(teamIdStr);
                        boolean success = teamDao.joinTeam(currentUser.getId(), teamId);
                        if (success) {
                            // 加入成功，重定向到index页面
                            request.getSession().setAttribute("successMessage", "成功加入团队！");
                            response.sendRedirect("index.jsp");
                            return;
                        } else {
                            request.setAttribute("errorMessage", "加入团队失败！");
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "无效的团队ID！");
                    }
                } else {
                    request.setAttribute("errorMessage", "团队ID不能为空！");
                }
            } else if ("leave".equals(action) && roleId != null &&
                    (roleId.startsWith("V8") || roleId.startsWith("V7") || roleId.startsWith("V6"))) {
                // 退出团队
                String teamIdStr = request.getParameter("teamId");
                if (teamIdStr != null && !teamIdStr.trim().isEmpty()) {
                    try {
                        int teamId = Integer.parseInt(teamIdStr);
                        boolean success = teamDao.leaveTeam(currentUser.getId(), teamId);
                        if (success) {
                            // 退出成功，重定向到index页面
                            request.getSession().setAttribute("successMessage", "成功退出团队！");
                            response.sendRedirect("index.jsp");
                            return;
                        } else {
                            request.setAttribute("errorMessage", "退出团队失败！");
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "无效的团队ID！");
                    }
                } else {
                    request.setAttribute("errorMessage", "团队ID不能为空！");
                }
            } else if ("disband".equals(action) && roleId != null &&
                    (roleId.startsWith("V5") || roleId.startsWith("V4") || roleId.startsWith("V3"))) {
                // 解散团队
                String teamIdStr = request.getParameter("teamId");
                if (teamIdStr != null && !teamIdStr.trim().isEmpty()) {
                    try {
                        int teamId = Integer.parseInt(teamIdStr);
                        boolean success = teamDao.disbandTeam(teamId, currentUser.getId());
                        if (success) {
                            // 解散成功，重定向到index页面
                            request.getSession().setAttribute("successMessage", "成功解散团队！");
                            response.sendRedirect("index.jsp");
                            return;
                        } else {
                            request.setAttribute("errorMessage", "解散团队失败！");
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "无效的团队ID！");
                    }
                } else {
                    request.setAttribute("errorMessage", "团队ID不能为空！");
                }
            } else {
                request.setAttribute("errorMessage", "非法操作或权限不足！");
            }

            // 如果执行到这里，说明操作失败或者参数验证失败
            // 重新获取团队数据并转发到团队页面
            List<Team> allTeams;
            if (roleId != null && (roleId.matches("^V[8-6].*"))) {
                allTeams = teamDao.findTeamsByUserId(currentUser.getId());
                request.setAttribute("showAll", false);
            } else {
                allTeams = teamDao.findAllBasic();
                request.setAttribute("showAll", true);
            }
            request.setAttribute("allTeams", allTeams);
            request.setAttribute("currentUser", currentUser);
            request.getRequestDispatcher("/team.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "操作失败: " + e.getMessage());

            // 失败时也要重新加载数据
            try {
                List<Team> allTeams = teamDao.findAllBasic();
                request.setAttribute("allTeams", allTeams);
                request.setAttribute("currentUser", currentUser);
                request.setAttribute("showAll", true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            request.getRequestDispatcher("/team.jsp").forward(request, response);
        }
    }
}