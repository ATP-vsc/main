<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itheima.model.User,com.itheima.model.Role,com.itheima.model.Department,com.itheima.model.Team" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人信息</title>
    <link rel="stylesheet" type="text/css" href="resources/main.css"/>
</head>
<body>
<div class="main-container">
    <div class="content" id="main-content">
        <div class="content-box">
            <h1 style="text-align: center;">我的信息</h1>
            <%
                // 从请求属性中获取用户相关信息
                // 包括用户基本信息、角色、部门和团队信息
                User user = (User) request.getAttribute("user");
                Role role = (Role) request.getAttribute("role");
                Department dept = (Department) request.getAttribute("dept");
                Team team = (Team) request.getAttribute("team");
            %>
            
            <% if(user != null){ %>
                <%-- 使用JSP条件判断控制显示内容 --%>
                <% String action = request.getParameter("action");
                   if ("edit".equals(action)) { %>
                    <!-- 编辑表单 -->
                    <h2 style="text-align: center;">修改个人信息</h2>
                    <% if (request.getAttribute("message") != null) { %>
                        <div class="message <%= request.getAttribute("messageType") %>">
                            <%= request.getAttribute("message") %>
                        </div>
                    <% } %>
                    <form action="userInfo?action=save" method="post">
                        <div class="form-group">
                            <label for="id">员工ID</label>
                            <input type="text" id="id" name="id" value="<%= user.getId() %>" readonly style="background-color: #f5f5f5; cursor: not-allowed;">
                        </div>
                        <div class="form-group">
                            <label for="username">姓名</label>
                            <input type="text" id="username" name="username" value="<%= user.getUsername() %>" required>
                        </div>
                        <div class="form-group">
                            <label for="phone">手机号</label>
                            <input type="text" id="phone" name="phone" value="<%= user.getPhone() %>" required>
                        </div>
                        <div class="form-group">
                            <label for="password">新密码（不修改请留空）</label>
                            <input type="password" id="password" name="password">
                        </div>
                        <div class="action-buttons">
                            <button type="submit" class="btn btn-primary">保存</button>
                        </div>
                    </form>
                <% } else { %>
                    <!-- 信息显示 -->
                    <div style="max-width: 600px; margin: 0 auto;">
                        <table style="width: 100%;">
                            <tr>
                                <td class="info-label" style="font-weight: bold; color: #5677a3; min-width: 100px;">员工ID</td>
                                <td><%= user.getId() %></td>
                            </tr>
                            <tr>
                                <td class="info-label" style="font-weight: bold; color: #5677a3;">姓名</td>
                                <td><%= user.getUsername() %></td>
                            </tr>
                            <tr>
                                <td class="info-label" style="font-weight: bold; color: #5677a3;">手机号</td>
                                <td><%= user.getPhone() %></td>
                            </tr>
                            <tr>
                                <td class="info-label" style="font-weight: bold; color: #5677a3;">入职时间</td>
                                <td><% 
                                    if (user.getHireDate() != null) {
                                        // 只显示年月日部分
                                        out.println(new java.text.SimpleDateFormat("yyyy年MM月dd日").format(user.getHireDate()));
                                    } else {
                                        out.println("未设置");
                                    }
                                %></td>
                            </tr>
                            <tr>
                                <td class="info-label" style="font-weight: bold; color: #5677a3;">状态</td>
                                <td><%= user.getStatus() %></td>
                            </tr>
                            <tr>
                                <td class="info-label" style="font-weight: bold; color: #5677a3;">职务</td>
                                <td><%= role == null ? "无" : role.getRoleName() %></td>
                            </tr>
                            <tr>
                                <td class="info-label" style="font-weight: bold; color: #5677a3;">部门</td>
                                <td><%= dept == null ? "无" : dept.getDepartmentName() %></td>
                            </tr>
                            <tr>
                                <td class="info-label" style="font-weight: bold; color: #5677a3;">小组</td>
                                <td><%= team == null ? "无" : team.getTeamName() %></td>
                            </tr>
                        </table>
                        <div class="action-buttons">
                            <a href="userInfo?action=edit" class="btn btn-primary" style="display: inline-block; text-decoration: none;">修改个人信息</a>
                        </div>
                    </div>
                <% } %>
            <% } else { %>
            <div class="error-message">
                无用户信息，请通过Servlet正确访问本页面！
            </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>