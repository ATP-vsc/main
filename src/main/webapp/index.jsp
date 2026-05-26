<%-- index.jsp：主页面 — 负责布局和页面结构 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>职员管理系统-首页</title>
    <link rel="stylesheet" type="text/css" href="resources/main.css"/>
    <script src="resources/main.js"></script>
</head>
<body>
<div class="topbar">
    <%@ include file="topbar.jsp" %>
</div>
<div class="main-container">
    <div class="sidebar">
        <%@ include file="sidebar.jsp" %>
    </div>
    <div class="content" id="main-content">
        <%@ include file="welcome.jsp" %>
    </div>
</div>
<div class="copyright">
    &copy; 2025 职员管理系统
</div>
</body>
</html>