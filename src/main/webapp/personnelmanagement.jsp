<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itheima.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>人事管理</title>
    <link rel="stylesheet" type="text/css" href="resources/main.css"/>
</head>
<body>
<div class="main-container">
    <div class="content" id="main-content">
        <div class="content-box">
            <h1>人事管理</h1>

            <%-- 错误信息显示 --%>
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null) {
            %>
            <div class="error-message">
                <%= errorMessage %>
            </div>
            <% } %>

            <%-- 成功信息显示 --%>
            <%
                String successMessage = (String) request.getAttribute("successMessage");
                if (successMessage != null) {
            %>
            <div class="success-message">
                <%= successMessage %>
            </div>
            <% } %>

            <%-- 员工数据 --%>
            <%
                List<User> allUsers = (List<User>) request.getAttribute("allUsers");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                if (allUsers != null && !allUsers.isEmpty()) {
                    int employeeCount = allUsers.size();
            %>
            <div class="employee-count">共有 <%= employeeCount %> 名员工</div>

            <table>
                <thead>
                <tr>
                    <th>员工ID</th>
                    <th>姓名</th>
                    <th>角色</th>
                    <th>部门</th>
                    <th>手机号</th>
                    <th>入职日期</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <% for (User u : allUsers) {
                    // 确定状态样式
                    String statusClass = "";
                    String status = u.getStatus();
                    if (status != null) {
                        if (status.contains("在职") || status.contains("激活") || status.contains("正常")) {
                            statusClass = "status-active";
                        } else if (status.contains("离职") || status.contains("离开")) {
                            statusClass = "status-leave";
                        } else {
                            statusClass = "status-inactive";
                        }
                    }

                    // 获取角色和部门显示名称
                    String roleDisplay = "未分配";
                    if (u.getRoleName() != null && !u.getRoleName().trim().isEmpty()) {
                        roleDisplay = u.getRoleName();
                    } else if (u.getRoleId() != null && !u.getRoleId().trim().isEmpty()) {
                        roleDisplay = "ID:" + u.getRoleId();
                    }

                    String deptDisplay = "未分配";
                    if (u.getDepartmentName() != null && !u.getDepartmentName().trim().isEmpty()) {
                        deptDisplay = u.getDepartmentName();
                    } else if (u.getDepartmentId() != null) {
                        deptDisplay = "ID:" + u.getDepartmentId();
                    }
                %>
                <tr>
                    <td class="employee-id">#<%= u.getId() %></td>
                    <td class="employee-name"><%= u.getUsername() != null ? u.getUsername() : "未命名" %></td>
                    <td><span class="role-badge"><%= roleDisplay %></span></td>
                    <td><span class="department-badge"><%= deptDisplay %></span></td>
                    <td class="phone-cell"><%= u.getPhone() != null ? u.getPhone() : "-" %></td>
                    <td class="date-cell"><%= u.getHireDate() != null ? dateFormat.format(u.getHireDate()) : "-" %></td>
                    <td>
                        <% if (status != null && !status.trim().isEmpty()) { %>
                        <span class="status-badge <%= statusClass %>"><%= status %></span>
                        <% } else { %>
                        <span class="status-badge">未知</span>
                        <% } %>
                    </td>
                    <td>
                        <div style="display: flex; gap: 8px;">
                            <form action="personnelmanagement" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="promote">
                                <input type="hidden" name="userId" value="<%= u.getId() %>">
                                <button type="submit" class="btn btn-success" style="padding: 6px 12px; font-size: 12px;" onclick="return confirm('确定要将该员工升职吗？')">升职</button>
                            </form>
                            <form action="personnelmanagement" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="demote">
                                <input type="hidden" name="userId" value="<%= u.getId() %>">
                                <button type="submit" class="btn btn-warning" style="padding: 6px 12px; font-size: 12px;" onclick="return confirm('确定要将该员工降职吗？')">降职</button>
                            </form>
                            <form action="personnelmanagement" method="post" style="display: inline;" onsubmit="return confirm('确定要删除该员工吗？此操作不可恢复！');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="userId" value="<%= u.getId() %>">
                                <button type="submit" class="btn btn-danger" style="padding: 6px 12px; font-size: 12px;">删除员工</button>
                            </form>
                        </div>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>

            <% } else { %>
            <%-- 空状态显示 --%>
            <div class="empty-state">
                <h3>暂无员工信息</h3>
                <p>当前没有可显示的员工信息。</p>
            </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>