package com.itheima.dao;

import com.itheima.model.Team;
import com.itheima.model.TeamMember;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamDao {
    private QueryRunner runner;

    public TeamDao(DataSource ds) {
        this.runner = new QueryRunner(ds);
    }

    // 方法1：根据用户ID查找所属团队（包括领导信息）

    public Team findByUserId(Integer userId) throws Exception {
        String sql = "SELECT t.*, u.username as teamLeaderName " +
                "FROM teams t " +
                "LEFT JOIN users u ON t.team_leader_id = u.id " +
                "WHERE t.team_id IN (SELECT team_id FROM teammenbers WHERE user_id = ?) " +
                "LIMIT 1";

        System.out.println("执行SQL: " + sql + ", 用户ID=" + userId);

        return runner.query(sql, rs -> {
            if (rs.next()) {
                Team team = new Team();
                team.setTeamId(rs.getInt("team_id"));
                team.setTeamName(rs.getString("team_name"));
                team.setTeamLeaderId(rs.getInt("team_leader_id"));
                team.setTeamLeaderName(rs.getString("teamLeaderName"));

                System.out.println("用户" + userId + "所属团队: ID=" + team.getTeamId() +
                        ", 名称=" + team.getTeamName() +
                        ", 领导ID=" + team.getTeamLeaderId() +
                        ", 领导名字=" + team.getTeamLeaderName());
                return team;
            } else {
                System.out.println("用户" + userId + "未加入任何团队");
                return null;
            }
        }, userId);
    }

    // 方法3：基础JDBC查询，包括领导信息
    public List<Team> findAllBasic() throws Exception {
        String sql = "SELECT t.*, u.username as teamLeaderName " +
                "FROM teams t " +
                "LEFT JOIN users u ON t.team_leader_id = u.id " +
                "ORDER BY t.team_id";

        List<Team> teams = new ArrayList<>();

        try (Connection conn = runner.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("使用基础JDBC查询团队...");

            while (rs.next()) {
                Team team = new Team();
                team.setTeamId(rs.getInt("team_id"));
                team.setTeamName(rs.getString("team_name"));
                team.setTeamLeaderId(rs.getInt("team_leader_id"));
                team.setTeamLeaderName(rs.getString("teamLeaderName"));

                System.out.println("读取到团队: ID=" + team.getTeamId() +
                        ", 名称=" + team.getTeamName() +
                        ", 领导=" + team.getTeamLeaderName());

                teams.add(team);
            }

            System.out.println("基础JDBC查询完成，共" + teams.size() + "个团队");
        } catch (SQLException e) {
            System.err.println("数据库查询错误: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return teams;
    }
    // 新增方法：创建团队（团队领导默认为创建者）
    public boolean createTeam(String teamName, Integer creatorId) throws Exception {
        String sql = "INSERT INTO teams (team_name, team_leader_id) VALUES (?, ?)";

        try {
            int result = runner.update(sql, teamName, creatorId);
            System.out.println("创建团队: " + teamName + ", 创建者ID: " + creatorId + ", 结果: " + result);
            return result > 0;
        } catch (SQLException e) {
            System.err.println("创建团队失败: " + e.getMessage());
            // 如果是重复团队名等错误，可以返回更具体的错误信息
            if (e.getMessage().contains("Duplicate")) {
                throw new Exception("团队名称已存在");
            }
            throw e;
        }
    }

    // 新增方法：加入团队
    public boolean joinTeam(Integer userId, Integer teamId) throws Exception {
        // 检查团队是否存在
        String checkTeamSql = "SELECT COUNT(*) FROM teams WHERE team_id = ?";
        Integer teamCount = runner.query(checkTeamSql, rs -> {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }, teamId);

        if (teamCount == 0) {
            throw new Exception("团队不存在");
        }

        // 检查是否已加入团队
        String checkSql = "SELECT COUNT(*) FROM teammenbers WHERE user_id = ? AND team_id = ?";
        Integer count = runner.query(checkSql, rs -> {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }, userId, teamId);

        if (count > 0) {
            throw new Exception("您已经加入该团队");
        }

        // 加入团队
        String sql = "INSERT INTO teammenbers (user_id, team_id) VALUES (?, ?)";

        try {
            int result = runner.update(sql, userId, teamId);
            System.out.println("用户" + userId + "加入团队" + teamId + ", 结果: " + result);
            return result > 0;
        } catch (SQLException e) {
            System.err.println("加入团队失败: " + e.getMessage());
            throw e;
        }
    }

    public List<Team> findTeamsByUserId(Integer userId) throws Exception {
        String sql = "SELECT t.*, u.username as teamLeaderName " +
                "FROM teams t " +
                "LEFT JOIN users u ON t.team_leader_id = u.id " +
                "WHERE t.team_id IN (SELECT team_id FROM teammenbers WHERE user_id = ?) " +
                "ORDER BY t.team_id";

        System.out.println("执行SQL: " + sql + ", 用户ID=" + userId);

        return runner.query(sql, rs -> {
            List<Team> teams = new ArrayList<>();
            while (rs.next()) {
                Team team = new Team();
                team.setTeamId(rs.getInt("team_id"));
                team.setTeamName(rs.getString("team_name"));
                team.setTeamLeaderId(rs.getInt("team_leader_id"));
                team.setTeamLeaderName(rs.getString("teamLeaderName"));
                teams.add(team);
            }
            System.out.println("用户" + userId + "加入的团队数量: " + teams.size());
            return teams;
        }, userId);
    }

    // 新增方法：退出团队
    public boolean leaveTeam(Integer userId, Integer teamId) throws Exception {
        // 检查团队是否存在
        String checkTeamSql = "SELECT COUNT(*) FROM teams WHERE team_id = ?";
        Integer teamCount = runner.query(checkTeamSql, rs -> {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }, teamId);

        if (teamCount == 0) {
            throw new Exception("团队不存在");
        }

        // 检查是否是团队领导，团队领导不能直接退出团队
        String checkLeaderSql = "SELECT team_leader_id FROM teams WHERE team_id = ?";
        Integer leaderId = runner.query(checkLeaderSql, rs -> {
            if (rs.next()) {
                return rs.getInt("team_leader_id");
            }
            return null;
        }, teamId);

        if (leaderId != null && leaderId.equals(userId)) {
            throw new Exception("您是该团队的领导，不能直接退出团队，请先转让领导职位或解散团队");
        }

        // 退出团队
        String sql = "DELETE FROM teammenbers WHERE user_id = ? AND team_id = ?";

        try {
            int result = runner.update(sql, userId, teamId);
            System.out.println("用户" + userId + "退出团队" + teamId + ", 结果: " + result);
            return result > 0;
        } catch (SQLException e) {
            System.err.println("退出团队失败: " + e.getMessage());
            throw e;
        }
    }

    // 新增方法：解散团队
    public boolean disbandTeam(Integer teamId, Integer leaderId) throws Exception {
        // 检查是否是团队领导
        String checkLeaderSql = "SELECT COUNT(*) FROM teams WHERE team_id = ? AND team_leader_id = ?";
        Integer count = runner.query(checkLeaderSql, rs -> {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }, teamId, leaderId);

        if (count == 0) {
            throw new Exception("您不是该团队的领导，无法解散团队");
        }

        try {
            // 删除团队成员
            String deleteMembersSql = "DELETE FROM teammenbers WHERE team_id = ?";
            runner.update(deleteMembersSql, teamId);
            
            // 删除团队
            String deleteTeamSql = "DELETE FROM teams WHERE team_id = ?";
            int result = runner.update(deleteTeamSql, teamId);
            
            System.out.println("领导" + leaderId + "解散团队" + teamId + ", 结果: " + result);
            return result > 0;
        } catch (SQLException e) {
            System.err.println("解散团队失败: " + e.getMessage());
            throw e;
        }
    }
}