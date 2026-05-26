package com.itheima.model;

import java.sql.Date;
import java.sql.Time;

public class Attendance {
    private int attendanceId;
    private int userId;
    private Date checkDate;
    private Time checkInTime;
    private Time checkOutTime;
    private String status;
    private double workHours;
    private double overtimeHours;

    // 关联的用户名
    private String userName;

    // Getter/Setter
    public int getAttendanceId() {
        return attendanceId;
    }
    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
    public Time getCheckInTime() {
        return checkInTime;
    }
    public void setCheckInTime(Time checkInTime) {
        this.checkInTime = checkInTime;
    }
    public Time getCheckOutTime() {
        return checkOutTime;
    }
    public void setCheckOutTime(Time checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public double getWorkHours() {
        return workHours;
    }
    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }
    public double getOvertimeHours() {
        return overtimeHours;
    }
    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    // 新增的用户名getter/setter
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // 格式化方法
    public String getFormattedCheckDate() {
        if (checkDate != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(checkDate);
        }
        return "";
    }

    public String getFormattedCheckInTime() {
        if (checkInTime != null) {
            return checkInTime.toString();
        }
        return "";
    }

    public String getFormattedCheckOutTime() {
        if (checkOutTime != null) {
            return checkOutTime.toString();
        }
        return "";
    }

    // 格式化工作时长
    public String getFormattedWorkHours() {
        if (workHours >= 0) {
            return String.format("%.2f", workHours);
        }
        return "0.00";
    }

    public String getFormattedOvertimeHours() {
        if (overtimeHours >= 0) {
            return String.format("%.2f", overtimeHours);
        }
        return "0.00";
    }
}