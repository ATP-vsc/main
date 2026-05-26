package com.itheima.controller;

import com.itheima.dao.AttendanceDao;
import com.itheima.model.Attendance;
import com.itheima.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户考勤管理Servlet
 * 处理用户考勤的查看、添加和保存功能
 */
@WebServlet("/userAttendance")
public class UserAttendanceServlet extends HttpServlet {
    /**
     * 处理GET请求，用于显示考勤页面
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws ServletException 如果处理请求失败
     * @throws IOException 如果发生I/O错误
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从会话中获取当前登录用户的ID
        HttpSession session = request.getSession(false);
        int userId = 1; // 默认值

        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userId = user.getId();
            }
        }

        // 获取action参数，判断是显示考勤列表还是显示添加表单
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            // 显示添加考勤表单
            // 清除之前的消息
            request.removeAttribute("message");
            request.removeAttribute("messageType");
        } else {
            // 显示考勤列表
            List<Attendance> list = AttendanceDao.findByUserId(userId);
            if (list == null) list = new ArrayList<>();  // 防止空指针
            request.setAttribute("attendanceList", list);
            // 清除之前的消息
            request.removeAttribute("message");
            request.removeAttribute("messageType");
        }

        // 转发到考勤页面
        request.getRequestDispatcher("attendance.jsp").forward(request, response);
    }

    /**
     * 处理POST请求，用于处理考勤信息的保存操作
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws ServletException 如果处理请求失败
     * @throws IOException 如果发生I/O错误
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 检查是否为保存操作
        String action = request.getParameter("action");
        if (!"save".equals(action)) {
            // 如果不是保存操作，则重定向到查看页面
            response.sendRedirect("userAttendance");
            return;
        }

        // 从会话中获取当前登录用户的ID
        HttpSession session = request.getSession(false);
        int userId = 1; // 默认值

        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                userId = user.getId();
            }
        }

        // 设置请求属性，用于在JSP页面显示消息
        try {
            // 获取表单参数
            String checkDateStr = request.getParameter("checkDate");
            String checkInTimeStr = request.getParameter("checkInTime");
            String checkOutTimeStr = request.getParameter("checkOutTime");
            String status = request.getParameter("status");

            // 转换日期和时间
            Date checkDate = Date.valueOf(checkDateStr);
            Time checkInTime = Time.valueOf(checkInTimeStr + ":00");
            Time checkOutTime = Time.valueOf(checkOutTimeStr + ":00");

            // 计算工作时长（小时）
            long diff = checkOutTime.getTime() - checkInTime.getTime();
            double workHours = diff / (1000.0 * 60 * 60);

            // 创建考勤对象
            Attendance attendance = new Attendance();
            attendance.setUserId(userId);
            attendance.setCheckDate(checkDate);
            attendance.setCheckInTime(checkInTime);
            attendance.setCheckOutTime(checkOutTime);
            attendance.setStatus(status);
            attendance.setWorkHours(workHours);

            // 保存考勤记录
            boolean success = AttendanceDao.save(attendance);

            if (success) {
                // 保存成功，设置会话属性并重定向到主页
                session.setAttribute("refreshAttendance", "true");
                response.sendRedirect("index.jsp");
                return;
            } else {
                // 设置失败消息
                request.setAttribute("message", "考勤提交失败，请重试！");
                request.setAttribute("messageType", "error");
            }
        } catch (Exception e) {
            // 设置错误消息
            request.setAttribute("message", "提交考勤时发生错误：" + e.getMessage());
            request.setAttribute("messageType", "error");
        }

        // 重新获取考勤信息
        doGet(request, response);
    }
}