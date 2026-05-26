<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.itheima.dao.PromotionRequestDao" %>
<%@ page import="java.util.List" %>
<%-- jobChange.jsp：职务异变管理页面 --%>
<%
    // 从request中获取升职申请记录
    List<PromotionRequestDao.PromotionRequestRecord> pendingRequests = 
        (List<PromotionRequestDao.PromotionRequestRecord>) request.getAttribute("pendingRequests");
    com.itheima.model.User currentUser = (com.itheima.model.User) request.getAttribute("currentUser");
%>

<div class="content-box">
    <h1 style="text-align: center;">职务异变管理</h1>
    
    <div style="margin-top: 30px; overflow-x: auto;">
        <h2 style="text-align: center;">待处理升职申请</h2>
        <table style="width: 100%; border-collapse: collapse; margin-top: 10px;">
            <thead>
                <tr style="background-color: #f2f2f2;">

                    <th style="border: 1px solid #ddd; padding: 8px;">申请人</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">当前职务</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">目标职务</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">申请时间</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">操作</th>
                </tr>
            </thead>
            <tbody id="promotionTableBody">
                <% if (pendingRequests != null && !pendingRequests.isEmpty()) { %>
                    <% for (PromotionRequestDao.PromotionRequestRecord record : pendingRequests) { %>
                    <tr>

                        <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getUserName() != null ? record.getUserName() : "未知用户" %></td>
                        <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getCurrentRoleId() %></td>
                        <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getTargetRoleId() %></td>
                        <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getRequestDate() != null ? record.getRequestDate().toString() : "" %></td>
                        <td style="border: 1px solid #ddd; padding: 8px; text-align: center;">
                            <form action="jobChange" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="approve">
                                <input type="hidden" name="requestId" value="<%= record.getRequestId() %>">
                                <button type="submit" class="btn approve-btn" style="background-color: #4CAF50; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; margin-right: 5px;" onclick="return confirm('确定要批准此升职申请吗？')">批准</button>
                            </form>
                            <form action="jobChange" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="reject">
                                <input type="hidden" name="requestId" value="<%= record.getRequestId() %>">
                                <button type="submit" class="btn reject-btn" style="background-color: #f44336; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer;" onclick="return confirm('确定要拒绝此升职申请吗？')">拒绝</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                <% } else { %>
                    <tr>
                        <td colspan="6" style="border: 1px solid #ddd; padding: 8px; text-align: center;">暂无待处理的升职申请</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    
    <!-- 已处理申请记录 -->
    <div style="margin-top: 30px; overflow-x: auto;">
        <h2 style="text-align: center;">历史申请记录</h2>
        <table style="width: 100%; border-collapse: collapse; margin-top: 10px;">
            <thead>
                <tr style="background-color: #f2f2f2;">
                    <th style="border: 1px solid #ddd; padding: 8px;">申请ID</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">申请人</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">当前职务</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">目标职务</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">申请时间</th>
                    <th style="border: 1px solid #ddd; padding: 8px;">状态</th>
                </tr>
            </thead>
            <tbody>
                <%
                // 获取所有申请记录（包括已处理的）
                com.itheima.dao.PromotionRequestDao promotionDao = new com.itheima.dao.PromotionRequestDao();
                java.util.List<com.itheima.dao.PromotionRequestDao.PromotionRequestRecord> allRequests = 
                    promotionDao.getAllPromotionRequests();
                
                if (allRequests != null && !allRequests.isEmpty()) {
                    for (com.itheima.dao.PromotionRequestDao.PromotionRequestRecord record : allRequests) {
                        if (!"待审批".equals(record.getStatus())) { // 只显示已处理的记录
                %>
                <tr>
                    <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getRequestId() %></td>
                    <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getUserName() != null ? record.getUserName() : "未知用户" %></td>
                    <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getCurrentRoleId() %></td>
                    <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getTargetRoleId() %></td>
                    <td style="border: 1px solid #ddd; padding: 8px; text-align: center;"><%= record.getRequestDate() != null ? record.getRequestDate().toString() : "" %></td>
                    <td style="border: 1px solid #ddd; padding: 8px; text-align: center;">
                        <span style="<%= "已批准".equals(record.getStatus()) ? "color: green;" : "color: red;" %>">
                            <%= record.getStatus() %>
                        </span>
                    </td>
                </tr>
                <%
                        }
                    }
                } else {
                %>
                <tr>
                    <td colspan="6" style="border: 1px solid #ddd; padding: 8px; text-align: center;">暂无申请记录</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>



<style>
    .content-box {
        max-width: 1200px;
        margin: 0 auto;
        padding: 20px;
    }

    table {
        border-collapse: collapse;
        width: 100%;
    }

    th, td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }

    th {
        background-color: #f2f2f2;
        text-align: center;
    }

    tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    .approve-btn, .reject-btn {
        cursor: pointer;
        padding: 5px 10px;
        border: none;
        border-radius: 3px;
        color: white;
    }

    .approve-btn:hover {
        opacity: 0.8;
    }

    .reject-btn:hover {
        opacity: 0.8;
    }
    
    .btn {
        padding: 6px 12px;
        font-size: 12px;
        border: none;
        border-radius: 3px;
        cursor: pointer;
    }
    
    .btn-success {
        background-color: #28a745;
        color: white;
    }
    
    .btn-warning {
        background-color: #ffc107;
        color: white;
    }
    
    .btn-danger {
        background-color: #dc3545;
        color: white;
    }
</style>