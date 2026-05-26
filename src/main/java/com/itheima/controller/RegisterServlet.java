// src/main/java/com/itheima/controller/RegisterServlet.java
package com.itheima.controller;

import com.itheima.model.User;
import com.itheima.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");

        if (userService.isUserExist(username)) {
            req.setAttribute("errorMsg", "用户名已存在");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        // 可以根据需求设置role_id, 默认为实习生V8
        user.setRoleId("V8");

        boolean flag = userService.register(user);
        if (flag) {
            resp.sendRedirect("login.jsp");
        } else {
            req.setAttribute("errorMsg", "注册失败");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}