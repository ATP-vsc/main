<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itheima.model.Attendance" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>公司考勤管理</title>
    <link rel="stylesheet" type="text/css" href="resources/main.css"/>
</head>
<body>
<div class="main-container">
    <div class="content" id="main-content">
        <div class="content-box">
            <h1>公司考勤管理</h1>

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

            <%
                // 显示所有考勤记录
                List<Attendance> allAttendance = (List<Attendance>) request.getAttribute("allAttendance");
                // 按用户ID排序
                if (allAttendance != null && !allAttendance.isEmpty()) {
                    Collections.sort(allAttendance, Comparator.comparingInt(Attendance::getUserId));
                }
                if (allAttendance != null && !allAttendance.isEmpty()) {
            %>

            <div class="stats">
                共有 <span><%= allAttendance.size() %></span> 条考勤记录
            </div>

            <div style="overflow-x: auto;">
                <table>
                    <thead>
                    <tr>
                        <th style="width: 80px;">考勤ID</th>
                        <th style="width: 120px;">员工</th>
                        <th style="width: 80px;">用户ID</th>
                        <th style="width: 120px;">考勤日期</th>
                        <th style="width: 100px;">上班时间</th>
                        <th style="width: 100px;">下班时间</th>
                        <th style="width: 80px;">状态</th>
                        <th style="width: 100px;">工作时长(小时)</th>
                        <th style="width: 100px;">加班时长(小时)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Attendance att : allAttendance) { %>
                    <tr>
                        <td class="employee-id"><%= att.getAttendanceId() %></td>
                        <td><%= att.getUserName() != null ? att.getUserName() : "用户" + att.getUserId() %></td>
                        <td><%= att.getUserId() %></td>
                        <td><%= att.getFormattedCheckDate() %></td>
                        <td><%= att.getFormattedCheckInTime() %></td>
                        <td><%= att.getFormattedCheckOutTime() %></td>
                        <td><%= att.getStatus() %></td>
                        <td><%= att.getFormattedWorkHours() %></td>
                        <td><%= att.getFormattedOvertimeHours() %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <div class="stats" style="margin-top: 20px;">
                <h4>考勤统计</h4>
                <p>总记录数: <%= allAttendance.size() %> 条</p>
                <%
                    int normalCount = 0;
                    int lateCount = 0;
                    int earlyLeaveCount = 0;
                    int absenceCount = 0;
                    int leaveCount = 0;
                    int overtimeCount = 0;

                    for (Attendance att : allAttendance) {
                        if ("正常".equals(att.getStatus())) normalCount++;
                        else if ("迟到".equals(att.getStatus())) lateCount++;
                        else if ("早退".equals(att.getStatus())) earlyLeaveCount++;
                        else if ("缺勤".equals(att.getStatus())) absenceCount++;
                        else if ("请假".equals(att.getStatus())) leaveCount++;
                        else if ("加班".equals(att.getStatus())) overtimeCount++;
                    }
                %>
                <p>正常: <%= normalCount %> 次 | 迟到: <%= lateCount %> 次 | 早退: <%= earlyLeaveCount %> 次</p>
                <p>缺勤: <%= absenceCount %> 次 | 请假: <%= leaveCount %> 次 | 加班: <%= overtimeCount %> 次</p>
            </div>
            <%
            } else {
            %>
            <div class="empty-state">
                <h3>暂无考勤信息</h3>
                <p>当前数据库中还没有考勤记录。</p>
                <p>请提醒员工进行考勤打卡，或联系管理员添加考勤数据。</p>
            </div>
            <%
                }
            %>
        </div>
    </div>
</div>
</body>
</html>