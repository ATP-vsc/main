<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itheima.dao.PromotionRequestDao" %>
<%@ page import="java.util.List" %>
<%-- job.jsp：主内容 我的职务 --%>
<%
    // 从request中获取用户和角色信息
    com.itheima.model.User user = (com.itheima.model.User) request.getAttribute("user");
    com.itheima.model.Role role = (com.itheima.model.Role) request.getAttribute("role");
    
    // 从request中获取升职申请记录
    List<PromotionRequestDao.PromotionRequestRecord> promotionRequests = 
        (List<PromotionRequestDao.PromotionRequestRecord>) request.getAttribute("promotionRequests");
%>

<div class="content-box">
    <h1 style="text-align: center;">我的职务</h1>
    <div style="max-width: 400px; margin: 0 auto; padding: 20px; text-align: center;">
        <% if (role != null) { %>
        <div style="margin: 15px 0; padding: 10px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9;">
            <div style="margin: 10px 0;">
                <label style="font-weight: bold; color: #333;">角色ID:</label>
                <span style="color: #666; margin-left: 10px;"><%= role.getRoleId() %></span>
            </div>
            <div style="margin: 10px 0;">
                <label style="font-weight: bold; color: #333;">角色名称:</label>
                <span style="color: #666; margin-left: 10px;"><%= role.getRoleName() %></span>
            </div>
        </div>

        <%-- 申请按钮 --%>
        <form action="job" method="post" style="display: inline-block;">
            <input type="hidden" name="action" value="promotionRequest">
            <button type="submit" style="background-color: #4CAF50; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px;" onclick="return confirm('确定要申请升职吗？');">申请升职</button>
        </form>

        <% } else { %>
        <p>暂无职务信息</p>
        <% } %>
    </div>
    
    <!-- 结果显示区域 -->
    <div id="resultMessage" style="max-width: 400px; margin: 20px auto; padding: 10px; text-align: center;">
        <%
            String message = (String) request.getAttribute("message");
            String messageType = (String) request.getAttribute("messageType");
            if (message != null) {
        %>
            <div style="padding: 10px; border-radius: 5px; 
                <%= "success".equals(messageType) ? "background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb;" : "background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb;" %>">
                <%= message %>
            </div>
        <% } %>
    </div>
    
    <!-- 申请记录表格 -->
    <div style="margin-top: 30px; overflow-x: auto;">
        <h2 style="text-align: center;">升职申请记录</h2>
        <table style="width: 100%; border-collapse: collapse; margin-top: 10px;">
            <thead>
                <tr style="background-color: #f2f2f2;">
                    <th style="border: 1px solid #ddd; padding: 8px;">序号</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">申请时间</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">状态</th>
                </tr>
            </thead>
            <tbody id="promotionTableBody">
                <% if (promotionRequests != null && !promotionRequests.isEmpty()) { %>
                    <% for (int i = 0; i < promotionRequests.size(); i++) { 
                        PromotionRequestDao.PromotionRequestRecord record = promotionRequests.get(i); %>
                    <tr>
                        <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= i + 1 %></td>
                        <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getRequestDate() %></td>
                        <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getStatus() %></td>
                    </tr>
                    <% } %>
                <% } else { %>
                    <tr>
                        <td colspan="3" style="border: 1px solid #ddd; padding: 8px; text-align: center;">暂无申请记录</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<style>
    .content-box {
        width: 400px;
        margin: 0 auto;
    }

    .info-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        text-align: center;
    }

    .info-item {
        margin: 15px 0;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 5px;
        background-color: #f9f9f9;
        width: 300px;
    }

    .info-item label {
        font-weight: bold;
        margin-right: 10px;
        color: #333;
    }

    .info-item span {
        color: #666;
    }

    button {
        background-color: #4CAF50;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
    }

    button:hover {
        background-color: #45a049;
    }
</style>