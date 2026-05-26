<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- sidebar.jsp：左侧功能区 --%>
<h2>职员管理系统</h2>

<%
    // 从session中获取用户信息
    com.itheima.model.User user = (com.itheima.model.User) session.getAttribute("user");

    // 检查用户角色是否为v3-v5范围
    if (user != null && user.getRoleId() != null) {
        String roleId = user.getRoleId();
        // 判断roleId是否在v3-v5范围内（不区分大小写）
        if (roleId.equalsIgnoreCase("v3") || roleId.equalsIgnoreCase("v4") || roleId.equalsIgnoreCase("v5")) {
%>
<button class="nav-btn" onclick="loadContent('userAttendance')">工作考勤</button>
<button class="nav-btn" onclick="loadContent('team')">团队管理</button>
<button class="nav-btn" onclick="loadContent('department')">我的部门</button>
<button class="nav-btn" onclick="loadContent('job')">我的职务</button>
<button class="nav-btn" onclick="loadContent('userInfo')">我的信息</button>
<%
} else if (roleId.equalsIgnoreCase("v1") || roleId.equalsIgnoreCase("v2")) {
%>
<button class="nav-btn" onclick="loadContent('companyAttendance')">公司考勤</button>
<button class="nav-btn" onclick="loadContent('department')">部门管理</button>
<button class="nav-btn" onclick="loadContent('personnelmanagement')">人事管理</button>
<button class="nav-btn" onclick="loadContent('jobChange')">职务异变</button>
<button class="nav-btn" onclick="loadContent('userInfo')">我的信息</button>
<%
} else {
%>
<button class="nav-btn" onclick="loadContent('userAttendance')">工作考勤</button>
<button class="nav-btn" onclick="loadContent('team')">我的团队</button>
<button class="nav-btn" onclick="loadContent('department')">我的部门</button>
<button class="nav-btn" onclick="loadContent('job')">我的职务</button>
<button class="nav-btn" onclick="loadContent('userInfo')">我的信息</button>
<%
        }
    }
%>