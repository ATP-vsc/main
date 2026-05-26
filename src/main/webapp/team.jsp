<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itheima.model.Team" %>
<%@ page import="com.itheima.model.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>团队管理</title>
    <link rel="stylesheet" type="text/css" href="resources/main.css"/>
</head>
<body>
<div class="main-container">
    <div class="content" id="main-content">
        <div class="content-box">
            <h1>团队管理</h1>

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

            <%-- 团队数据 --%>
            <%
                List<Team> allTeams = (List<Team>) request.getAttribute("allTeams");
                User currentUser = (User) request.getAttribute("currentUser");
                Boolean showAll = (Boolean) request.getAttribute("showAll");

                if (allTeams != null && !allTeams.isEmpty()) {
                    int teamCount = allTeams.size();
            %>

            <div class="stats">

                <%
                    if (showAll != null && !showAll) {
                %>
                您已加入 <span><%= teamCount %></span> 个团队
                <span style="color: #666; margin-left: 10px;">（仅显示您已加入的团队）</span>
                <%
                    }else  {
                %>
                共有 <span><%= teamCount %></span> 个团队
                <%
                }
                %>
            </div>

            <table>
                <thead>
                <tr>
                    <th style="width: 100px;">团队ID</th>
                    <th style="width: 250px;">团队名称</th>
                    <th>团队领导</th>
                    <th style="width: 100px;">领导ID</th>
                    <th style="width: 120px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (Team team : allTeams) {
                        if (team != null && team.getTeamId() != null) {
                %>
                <tr>
                    <td class="team-id">#<%= team.getTeamId() %></td>
                    <td class="team-name">
                        <%= team.getTeamName() != null ? team.getTeamName() : "未命名" %>
                    </td>
                    <td>
                        <%
                            if (team.getTeamLeaderName() != null) {
                        %>
                        <span class="leader-name"><%= team.getTeamLeaderName() %></span>
                        <% if (team.getTeamLeaderId() != null) { %>
                        <span class="leader-id">(ID: <%= team.getTeamLeaderId() %>)</span>
                        <% } %>
                        <%
                        } else if (team.getTeamLeaderId() != null) {
                        %>
                        用户ID: <%= team.getTeamLeaderId() %>
                        <%
                        } else {
                        %>
                        未设置
                        <% } %>
                    </td>
                    <td>
                        <%= team.getTeamLeaderId() != null ? team.getTeamLeaderId() : "-" %>
                    </td>
                    <td>
                        <%
                            if (currentUser != null && currentUser.getRoleId() != null) {
                                String roleId = currentUser.getRoleId();
                                // 对于普通成员，显示退出团队按钮
                                if ((roleId.startsWith("V8") || roleId.startsWith("V7") || roleId.startsWith("V6")) 
                                    && team.getTeamId() != null) {
                        %>
                            <form action="team" method="post" style="display: inline-block;" onsubmit="return confirm('确定要退出该团队吗？');">
                                <input type="hidden" name="action" value="leave">
                                <input type="hidden" name="teamId" value="<%= team.getTeamId() %>">
                                <button type="submit" class="btn btn-danger btn-sm">退出团队</button>
                            </form>
                        <%
                                }
                                // 对于管理员，显示解散团队按钮
                                else if ((roleId.startsWith("V5") || roleId.startsWith("V4") || roleId.startsWith("V3")) 
                                         && team.getTeamId() != null) {
                        %>
                            <form action="team" method="post" style="display: inline-block;" onsubmit="return confirm('确定要解散该团队吗？此操作不可恢复！');">
                                <input type="hidden" name="action" value="disband">
                                <input type="hidden" name="teamId" value="<%= team.getTeamId() %>">
                                <button type="submit" class="btn btn-danger btn-sm">解散团队</button>
                            </form>
                        <%      }
                            }
                        %>
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
                <h3>暂无团队信息</h3>
                <p>当前没有可显示的团队数据</p>
            </div>
            <% } %>

            <%-- 操作按钮区域 --%>
            <div class="action-buttons">
                <%
                    if (currentUser != null && currentUser.getRoleId() != null) {
                        String roleId = currentUser.getRoleId();

                        if (roleId.startsWith("V8") || roleId.startsWith("V7") || roleId.startsWith("V6")) {
                            // v8,v7,v6 显示加入团队按钮
                %>
                <div style="display: flex; gap: 10px;">
                    <form action="team" method="post" class="form-inline">
                        <input type="hidden" name="action" value="join">
                        <input type="text" name="teamId" class="form-control" placeholder="输入团队ID" required>
                        <button type="submit" class="btn btn-primary">加入团队</button>
                    </form>
                </div>
                <%
                } else if (roleId.startsWith("V5") || roleId.startsWith("V4") || roleId.startsWith("V3")) {
                    // v5,v4,v3 显示创建团队按钮
                %>
                <div style="display: flex; gap: 10px;">
                    <form action="team" method="post" class="form-inline">
                        <input type="hidden" name="action" value="create">
                        <input type="text" name="teamName" class="form-control" placeholder="输入团队名称" required>
                        <button type="submit" class="btn btn-success">创建团队</button>
                    </form>
                </div>
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