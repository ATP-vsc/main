package com.itheima.model;

public class TeamMember {
    private Integer id;

    private Integer userId;


    // 关联的用户对象（可选）
    private User user;

    // 关联的团队对象（可选）
    private Team team;

    // 构造方法
    public TeamMember() {}

    // Getter 和 Setter 方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }



    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }



    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

}