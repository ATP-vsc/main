package com.itheima.model;

public class Team {
    private Integer teamId;
    private String teamName;
    private Integer teamLeaderId;
    private String teamLeaderName;

    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public Integer getTeamLeaderId() { return teamLeaderId; }
    public void setTeamLeaderId(Integer teamLeaderId) { this.teamLeaderId = teamLeaderId; }

    public String getTeamLeaderName() { return teamLeaderName; }
    public void setTeamLeaderName(String teamLeaderName) { this.teamLeaderName = teamLeaderName; }
}