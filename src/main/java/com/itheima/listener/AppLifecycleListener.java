package com.itheima.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppLifecycleListener implements ServletContextListener {
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 应用程序关闭时清空消息文件

        System.out.println("Application is shutting down. Messages cleared.");
    }
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 应用程序启动时的初始化操作
        System.out.println("Application is starting up.");
    }
}