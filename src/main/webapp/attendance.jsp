<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List,com.itheima.model.Attendance" %>
<%
    // 从请求中获取考勤记录列表和操作参数
    // attendanceList: 包含用户考勤信息的列表
    // action: 表示当前请求的操作类型（如"add"表示添加考勤）
    List<Attendance> attendanceList = (List<Attendance>) request.getAttribute("attendanceList");
    String action = request.getParameter("action");
%>

<div class="main-container">
    <div class="content" id="main-content">
        <div class="content-box">
            <h1 style="text-align: center;">工作考勤</h1>

            <% if ("add".equals(action)) { %>
            <!-- 提交考勤表单 -->
            <h2 style="text-align: center;">提交考勤</h2>
            <% if (request.getAttribute("message") != null) { %>
                <div class="message <%= request.getAttribute("messageType") %>">
                    <%= request.getAttribute("message") %>
                </div>
            <% } %>
            <form action="userAttendance?action=save" method="post" style="max-width: 400px; margin: 0 auto;">
                <div class="form-group">
                    <label for="checkDate">考勤日期</label>
                    <input type="date" id="checkDate" name="checkDate" required class="form-control">
                </div>
                <div class="form-group">
                    <label for="checkInTime">签到时间</label>
                    <input type="time" id="checkInTime" name="checkInTime" required class="form-control">
                </div>
                <div class="form-group">
                    <label for="checkOutTime">签退时间</label>
                    <input type="time" id="checkOutTime" name="checkOutTime" required class="form-control">
                </div>
                <div class="form-group">
                    <label for="status">考勤状态</label>
                    <select id="status" name="status" required class="form-control">
                        <option value="正常">正常</option>
                        <option value="迟到">迟到</option>
                        <option value="早退">早退</option>
                        <option value="缺勤">缺勤</option>
                        <option value="请假">请假</option>
                        <option value="加班">加班</option>
                    </select>
                </div>
                <div class="action-buttons">
                    <button type="submit" class="btn btn-primary" >提交</button>
                </div>
            </form>
            <% } else { %>
            <!-- 考勤记录列表 -->
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>日期</th>
                            <th>签到时间</th>
                            <th>签退时间</th>
                            <th>状态</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (attendanceList != null && !attendanceList.isEmpty()) {
                                for (Attendance att : attendanceList) {
                        %>
                        <tr>
                            <td><%= att.getCheckDate() %></td>
                            <td><%= att.getCheckInTime() %></td>
                            <td><%= att.getCheckOutTime() %></td>
                            <td><%= att.getStatus() %></td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="4" style="text-align: center; color: #888;">暂无考勤数据</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>

            <!-- 添加考勤按钮 -->
            <div class="action-buttons">
                <a href="userAttendance?action=add" class="btn btn-primary" style="display: inline-block; text-decoration: none;">提交考勤</a>
            </div>
            <% } %>
        </div>
    </div>
</div>