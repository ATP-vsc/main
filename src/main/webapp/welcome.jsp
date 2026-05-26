<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itheima.model.User" %>
<div class="content-box">
    <%
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            String initial = currentUser.getUsername().substring(0, 1).toUpperCase();
    %>
    <div style="background-color: #fff; border-radius: 10px; padding: 20px; margin-bottom: 30px; box-shadow: 0 5px 15px rgba(0,0,0,0.05); text-align: left; display: inline-block;">
        <div style="width: 60px; height: 60px; border-radius: 50%; background-color: #4e8bef; color: white; display: flex; align-items: center; justify-content: center; font-size: 24px; font-weight: bold; float: left; margin-right: 20px;">
            <%= initial %>
        </div>
        <div style="overflow: hidden;">
            <h2 style="font-size: 1.3em; font-weight: 600; color: #355d9c; margin: 0 0 5px 0;">欢迎回来，<%= currentUser.getUsername() %></h2>
            <p style="color: #666; margin: 0;">欢迎您使用员工管理系统</p>
        </div>
        <div style="clear: both;"></div>
    </div>
    <% } else { %>
    <div style="background-color: #fff; border-radius: 10px; padding: 20px; margin-bottom: 30px; box-shadow: 0 5px 15px rgba(0,0,0,0.05); text-align: left; display: inline-block;">
        <div style="width: 60px; height: 60px; border-radius: 50%; background-color: #4e8bef; color: white; display: flex; align-items: center; justify-content: center; font-size: 24px; font-weight: bold; float: left; margin-right: 20px;">
            U
        </div>
        <div style="overflow: hidden;">
            <h2 style="font-size: 1.3em; font-weight: 600; color: #355d9c; margin: 0 0 5px 0;">欢迎来到员工管理系统</h2>
            <p style="color: #666; margin: 0;">请先登录您的账户</p>
        </div>
        <div style="clear: both;"></div>
    </div>
    <% } %>

    <h1 style="font-size: 2.5em; color: #355d9c; margin-bottom: 20px; font-weight: 600; text-align: center;">员工管理系统</h1>
    <p style="font-size: 1.2em; color: #5677a3; margin-bottom: 40px; max-width: 800px; margin-left: auto; margin-right: auto; line-height: 1.6; text-align: center;">
        这是一个全面的员工管理解决方案，旨在帮助企业高效管理员工信息、部门分配、团队协作和日常任务。
    </p>

    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 30px; margin-top: 50px;">
        <div style="background-color: #fff; border-radius: 10px; padding: 30px; box-shadow: 0 5px 15px rgba(0,0,0,0.05); transition: transform 0.3s, box-shadow 0.3s;">
            <div style="font-size: 2.5em; margin-bottom: 20px; color: #4e8bef;">👥</div>
            <h3 style="font-size: 1.3em; color: #355d9c; margin-bottom: 15px;">员工管理</h3>
            <p style="color: #666; line-height: 1.5;">
                全面管理员工信息，包括个人资料、职位、联系方式等，支持快速检索和更新。
            </p>
        </div>

        <div style="background-color: #fff; border-radius: 10px; padding: 30px; box-shadow: 0 5px 15px rgba(0,0,0,0.05); transition: transform 0.3s, box-shadow 0.3s;">
            <div style="font-size: 2.5em; margin-bottom: 20px; color: #4e8bef;">🏢</div>
            <h3 style="font-size: 1.3em; color: #355d9c; margin-bottom: 15px;">部门组织</h3>
            <p style="color: #666; line-height: 1.5;">
                清晰的部门结构管理，支持多层级组织架构，便于企业内部协作和资源分配。
            </p>
        </div>
    </div>
</div>