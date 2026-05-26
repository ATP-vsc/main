package com.itheima.dao;

import com.itheima.model.Attendance;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AttendanceDao {
    private QueryRunner runner;
    private DataSource dataSource;

    // 静态实例，用于支持静态方法调用
    private static AttendanceDao staticInstance;

    // 静态初始化块
    static {
        try {
            // 创建静态实例
            Properties prop = new Properties();
            prop.load(AttendanceDao.class.getClassLoader().getResourceAsStream("druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            staticInstance = new AttendanceDao(dataSource);
            System.out.println("AttendanceDao 静态实例初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("AttendanceDao 静态实例初始化失败: " + e.getMessage());
        }
    }

    // 构造函数，接受DataSource
    public AttendanceDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.runner = new QueryRunner(dataSource);
    }

    // 静态方法：查询所有考勤记录
    public static List<Attendance> findAll() throws SQLException {
        if (staticInstance == null) {
            throw new IllegalStateException("AttendanceDao 未初始化");
        }
        return staticInstance.findAllInternal();
    }

    // 实例方法：查询所有考勤记录
    public List<Attendance> findAllInternal() throws SQLException {
        String sql = "SELECT a.*, u.username as userName FROM attendance a " +
                "LEFT JOIN users u ON a.user_id = u.id " +
                "ORDER BY a.check_date DESC, a.check_in_time DESC";

        System.out.println("执行SQL: " + sql);

        return runner.query(sql, rs -> {
            List<Attendance> list = new ArrayList<>();
            while (rs.next()) {
                Attendance att = new Attendance();
                att.setAttendanceId(rs.getInt("attendance_id"));
                att.setUserId(rs.getInt("user_id"));
                att.setCheckDate(rs.getDate("check_date"));
                att.setCheckInTime(rs.getTime("check_in_time"));
                att.setCheckOutTime(rs.getTime("check_out_time"));
                att.setStatus(rs.getString("status"));
                att.setWorkHours(rs.getDouble("work_hours"));
                att.setOvertimeHours(rs.getDouble("overtime_hours"));

                try {
                    att.setUserName(rs.getString("userName"));
                } catch (SQLException e) {
                    att.setUserName(null);
                }

                list.add(att);
            }
            return list;
        });
    }

    // 静态方法：根据用户ID查询考勤记录
    public static List<Attendance> findByUserId(int userId) {
        if (staticInstance == null) {
            throw new IllegalStateException("AttendanceDao 未初始化");
        }
        try {
            return staticInstance.findByUserIdInternal(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 实例方法：根据用户ID查询考勤记录
    public List<Attendance> findByUserIdInternal(int userId) throws SQLException {
        String sql = "SELECT * FROM attendance WHERE user_id = ? ORDER BY check_date DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Attendance> list = new ArrayList<>();
                while (rs.next()) {
                    Attendance att = new Attendance();
                    att.setAttendanceId(rs.getInt("attendance_id"));
                    att.setUserId(rs.getInt("user_id"));
                    att.setCheckDate(rs.getDate("check_date"));
                    att.setCheckInTime(rs.getTime("check_in_time"));
                    att.setCheckOutTime(rs.getTime("check_out_time"));
                    att.setStatus(rs.getString("status"));
                    att.setWorkHours(rs.getDouble("work_hours"));
                    att.setOvertimeHours(rs.getDouble("overtime_hours"));
                    list.add(att);
                }
                return list;
            }
        }
    }

    // 静态方法：保存考勤记录
    public static boolean save(Attendance attendance) {
        if (staticInstance == null) {
            throw new IllegalStateException("AttendanceDao 未初始化");
        }
        try {
            return staticInstance.saveOrUpdateInternal(attendance);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 实例方法：保存或更新考勤记录
    public boolean saveOrUpdateInternal(Attendance attendance) throws SQLException {
        // 首先检查该用户在该日期是否已有考勤记录
        String checkSql = "SELECT attendance_id FROM attendance WHERE user_id = ? AND check_date = ?";
        String updateSql = "UPDATE attendance SET check_in_time = ?, check_out_time = ?, status = ?, work_hours = ?, overtime_hours = ? WHERE user_id = ? AND check_date = ?";
        String insertSql = "INSERT INTO attendance(user_id, check_date, check_in_time, check_out_time, status, work_hours, overtime_hours) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            // 检查是否已有记录
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, attendance.getUserId());
            checkStmt.setDate(2, attendance.getCheckDate());
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                // 已有记录，执行更新
                updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setTime(1, attendance.getCheckInTime());
                updateStmt.setTime(2, attendance.getCheckOutTime());
                updateStmt.setString(3, attendance.getStatus());
                updateStmt.setDouble(4, attendance.getWorkHours());
                updateStmt.setDouble(5, attendance.getOvertimeHours());
                updateStmt.setInt(6, attendance.getUserId());
                updateStmt.setDate(7, attendance.getCheckDate());

                int result = updateStmt.executeUpdate();
                return result > 0;
            } else {
                // 没有记录，执行插入
                insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, attendance.getUserId());
                insertStmt.setDate(2, attendance.getCheckDate());
                insertStmt.setTime(3, attendance.getCheckInTime());
                insertStmt.setTime(4, attendance.getCheckOutTime());
                insertStmt.setString(5, attendance.getStatus());
                insertStmt.setDouble(6, attendance.getWorkHours());
                insertStmt.setDouble(7, attendance.getOvertimeHours());

                int result = insertStmt.executeUpdate();
                return result > 0;
            }
        } finally {
            // 关闭资源
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (checkStmt != null) checkStmt.close(); } catch (SQLException e) {}
            try { if (updateStmt != null) updateStmt.close(); } catch (SQLException e) {}
            try { if (insertStmt != null) insertStmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    // 静态方法：保存考勤记录（保留原方法以兼容其他代码）
    public static boolean saveOrUpdate(Attendance attendance) {
        return save(attendance);
    }
}