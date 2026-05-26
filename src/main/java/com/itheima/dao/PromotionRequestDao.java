package com.itheima.dao;

import com.itheima.model.User;
import java.sql.*;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import java.util.ArrayList;

public class PromotionRequestDao {
    private DataSource dataSource;
    
    public PromotionRequestDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    // 无参构造函数，用于直接连接数据库（备用）
    public PromotionRequestDao() {
        this.dataSource = null;
    }
    // 数据库连接 - 使用与项目其他部分相同的连接方式
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/javaweb_staffsystem?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    /**
     * 保存升职申请到数据库
     * @param userId 申请用户ID
     * @param currentRoleId 当前角色ID
     * @param targetRoleId 目标角色ID
     * @return 是否保存成功
     */
    public boolean savePromotionRequest(int userId, String currentRoleId, String targetRoleId) {
        String sql = "INSERT INTO promotion_requests (user_id, current_role_id, target_role_id, status) VALUES (?, ?, ?, '待审批')";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, currentRoleId);
            stmt.setString(3, targetRoleId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 根据用户ID获取所有升职申请记录
     * @param userId 用户ID
     * @return 升职申请列表
     */
    public java.util.List<PromotionRequestRecord> getPromotionRequestsByUserId(int userId) {
        String sql = "SELECT request_id, user_id, request_date, status, current_role_id, target_role_id FROM promotion_requests WHERE user_id = ? ORDER BY request_date DESC";
        
        java.util.List<PromotionRequestRecord> requests = new java.util.ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PromotionRequestRecord record = new PromotionRequestRecord();
                    record.setRequestId(rs.getInt("request_id"));
                    record.setUserId(rs.getInt("user_id"));
                    record.setRequestDate(rs.getTimestamp("request_date").toLocalDateTime());
                    record.setStatus(rs.getString("status"));
                    record.setCurrentRoleId(rs.getString("current_role_id"));
                    record.setTargetRoleId(rs.getString("target_role_id"));
                    
                    requests.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * 根据状态获取升职申请记录
     * @param status 申请状态
     * @return 升职申请列表
     */
    public java.util.List<PromotionRequestRecord> getPromotionRequestsByStatus(String status) {
        String sql = "SELECT pr.request_id, pr.user_id, pr.request_date, pr.status, pr.current_role_id, pr.target_role_id, u.username as user_name " +
                     "FROM promotion_requests pr LEFT JOIN users u ON pr.user_id = u.id WHERE pr.status = ? ORDER BY pr.request_date DESC";
        
        java.util.List<PromotionRequestRecord> requests = new java.util.ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PromotionRequestRecord record = new PromotionRequestRecord();
                    record.setRequestId(rs.getInt("request_id"));
                    record.setUserId(rs.getInt("user_id"));
                    record.setRequestDate(rs.getTimestamp("request_date").toLocalDateTime());
                    record.setStatus(rs.getString("status"));
                    record.setCurrentRoleId(rs.getString("current_role_id"));
                    record.setTargetRoleId(rs.getString("target_role_id"));
                    record.setUserName(rs.getString("user_name"));
                    
                    requests.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * 获取所有升职申请记录
     * @return 升职申请列表
     */
    public java.util.List<PromotionRequestRecord> getAllPromotionRequests() {
        String sql = "SELECT pr.request_id, pr.user_id, pr.request_date, pr.status, pr.current_role_id, pr.target_role_id, u.username as user_name " +
                     "FROM promotion_requests pr LEFT JOIN users u ON pr.user_id = u.id ORDER BY pr.request_date DESC";
        
        java.util.List<PromotionRequestRecord> requests = new java.util.ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PromotionRequestRecord record = new PromotionRequestRecord();
                    record.setRequestId(rs.getInt("request_id"));
                    record.setUserId(rs.getInt("user_id"));
                    record.setRequestDate(rs.getTimestamp("request_date").toLocalDateTime());
                    record.setStatus(rs.getString("status"));
                    record.setCurrentRoleId(rs.getString("current_role_id"));
                    record.setTargetRoleId(rs.getString("target_role_id"));
                    record.setUserName(rs.getString("user_name"));
                    
                    requests.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * 根据申请ID获取升职申请记录
     * @param requestId 申请ID
     * @return 升职申请记录
     */
    public PromotionRequestRecord getPromotionRequestById(int requestId) {
        String sql = "SELECT request_id, user_id, request_date, status, current_role_id, target_role_id FROM promotion_requests WHERE request_id = ?";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PromotionRequestRecord record = new PromotionRequestRecord();
                    record.setRequestId(rs.getInt("request_id"));
                    record.setUserId(rs.getInt("user_id"));
                    record.setRequestDate(rs.getTimestamp("request_date").toLocalDateTime());
                    record.setStatus(rs.getString("status"));
                    record.setCurrentRoleId(rs.getString("current_role_id"));
                    record.setTargetRoleId(rs.getString("target_role_id"));
                    
                    return record;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 更新升职申请状态
     * @param requestId 申请ID
     * @param status 新状态
     * @param processedBy 处理人ID
     * @return 是否更新成功
     */
    public boolean updatePromotionRequestStatus(int requestId, String status, int processedBy) {
        String sql = "UPDATE promotion_requests SET status = ?, processed_date = ?, processed_by = ? WHERE request_id = ?";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, processedBy);
            stmt.setInt(4, requestId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 内部类用于表示升职申请记录
    public static class PromotionRequestRecord {
        private int requestId;
        private int userId;
        private LocalDateTime requestDate;
        private String status;
        private String currentRoleId;
        private String targetRoleId;
        private String userName; // 用于存储用户名
        
        // Getters and Setters
        public int getRequestId() { return requestId; }
        public void setRequestId(int requestId) { this.requestId = requestId; }
        
        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }
        
        public LocalDateTime getRequestDate() { return requestDate; }
        public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getCurrentRoleId() { return currentRoleId; }
        public void setCurrentRoleId(String currentRoleId) { this.currentRoleId = currentRoleId; }
        
        public String getTargetRoleId() { return targetRoleId; }
        public void setTargetRoleId(String targetRoleId) { this.targetRoleId = targetRoleId; }
        
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
    }
}