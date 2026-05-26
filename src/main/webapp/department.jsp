<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itheima.model.Department" %>
<%@ page import="com.itheima.model.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的部门</title>
    <link rel="stylesheet" type="text/css" href="resources/main.css"/>
</head>
<body>
<div class="main-container">
    <div class="content" id="main-content">
        <div class="content-box">
            <h1>我的部门</h1>

            <%-- 成功信息 --%>
            <%
                String successMessage = (String) request.getAttribute("successMessage");
                if (successMessage != null) {
            %>
            <div class="success-message">
                <%= successMessage %>
            </div>
            <% } %>

            <%-- 错误信息 --%>
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null) {
            %>
            <div class="error-message">
                <%= errorMessage %>
            </div>
            <% } %>

            <%-- 部门数据 --%>
            <%
                List<Department> allDepartments = (List<Department>) request.getAttribute("allDepartments");
                User currentUser = (User) request.getAttribute("currentUser");
                Boolean showAll = (Boolean) request.getAttribute("showAll");

                if (allDepartments != null && !allDepartments.isEmpty()) {
                    int departmentCount = allDepartments.size();
            %>

            <div class="stats">

                <%
                    if (showAll != null && !showAll) {
                %>
                您已加入 <span><%= departmentCount %></span> 个部门
                <span style="color: #666; margin-left: 10px;">（仅显示您已加入的部门）</span>
                <%
                    }else  {
                %>
                共有 <span><%= departmentCount %></span> 个部门
                <%
                }
                %>
            </div>

            <table>
                <thead>
                <tr>
                    <th style="width: 100px;">部门ID</th>
                    <th style="width: 250px;">部门名称</th>
                    <th>部门主管</th>
                    <th style="width: 100px;">主管ID</th>
                    <th style="width: 120px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (Department department : allDepartments) {
//                System.out.println(department.getDepartmentId());
                        if (department != null ) {

                %>
                <tr>
                    <td class="department-id">#<%= department.getDepartmentId()%></td>
                    <td class="department-name">
                        <%= department.getDepartmentName() != null ? department.getDepartmentName() : "未命名" %>
                    </td>
                    <td>
                        <%
                            if (department.getManagerId() != null) {
                        %>
                        用户ID: <%= department.getManagerId() %>
                        <%
                        } else {
                        %>
                        未设置
                        <% } %>
                    </td>
                    <td>
                        <%= department.getManagerId() != null ? department.getManagerId() : "-" %>
                    </td>
                    <td>
                        <%
                            // 只有V1和V2角色可以删除部门
                            if (currentUser != null && currentUser.getRoleId() != null && 
                               (currentUser.getRoleId().startsWith("V1") || currentUser.getRoleId().startsWith("V2"))) {
                        %>
                            <form action="department" method="post" style="display: inline-block;" onsubmit="return confirm('确定要删除该部门吗？此操作不可恢复！');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="departmentId" value="<%= department.getDepartmentId() %>">
                                <button type="submit" class="btn btn-danger btn-sm">删除</button>
                            </form>
                        <% } 
                           // 普通员工可以退出部门
                           else if (currentUser != null && currentUser.getRoleId() != null && 
                                   (currentUser.getRoleId().startsWith("V3") || currentUser.getRoleId().startsWith("V4") || 
                                    currentUser.getRoleId().startsWith("V5") || currentUser.getRoleId().startsWith("V6") || 
                                    currentUser.getRoleId().startsWith("V7") || currentUser.getRoleId().startsWith("V8"))) {
                        %>
                            <form action="department" method="post" style="display: inline-block;" onsubmit="return confirm('确定要退出该部门吗？');">
                                <input type="hidden" name="action" value="leave">
                                <input type="hidden" name="departmentId" value="<%= department.getDepartmentId() %>">
                                <button type="submit" class="btn btn-warning btn-sm">退出</button>
                            </form>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>

            <%
            } else {
            %>
            <div class="no-data">
                <h3>暂无部门信息</h3>
                <p>当前没有可显示的部门数据</p>
            </div>
            <% } %>

            <%-- 操作按钮区域 --%>
            <div class="action-buttons">
                <%
                    if (currentUser != null && currentUser.getRoleId() != null) {
                        String roleId = currentUser.getRoleId();

                        if (roleId.startsWith("V8") || roleId.startsWith("V7") || roleId.startsWith("V6") ||
                                roleId.startsWith("V5") || roleId.startsWith("V4") || roleId.startsWith("V3")) {
                            // v8,v7,v6 显示加入部门按钮
                %>
                <form action="department" method="post" class="form-inline">
                    <input type="hidden" name="action" value="join">
                    <input type="text" name="departmentId" class="form-control" placeholder="输入部门ID" required>
                    <button type="submit" class="btn btn-primary">加入部门</button>
                </form>
                <%
                } else if (roleId.startsWith("V1") || roleId.startsWith("V2") ) {
                    // v5,v4,v3 显示创建部门按钮
                %>
                <form action="department" method="post" class="form-inline">
                    <input type="hidden" name="action" value="create">
                    <input type="text" name="departmentName" class="form-control" placeholder="输入部门名称" required>
                    <button type="submit" class="btn btn-success">创建部门</button>
                </form>
                <%
                        }
                    }
                %>
            </div>
        </div>
    </div>
</div>
</body>
</html>