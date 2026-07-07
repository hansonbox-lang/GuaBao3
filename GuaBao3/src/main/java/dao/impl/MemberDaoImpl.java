package dao.impl;

import dao.MemberDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Member;
import util.DbUtil;

public class MemberDaoImpl implements MemberDao {
    @Override
    public Member getById(String memberId) throws SQLException {
        String sql = "SELECT * FROM members WHERE member_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Member mem = new Member();
                    mem.setMemberId(rs.getString("member_id"));
                    mem.setTotalPoints(rs.getInt("total_points"));
                    return mem;
                }
            }
        }
        return null;
    }

    @Override
    public List<Member> getAll() throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT member_id, total_points FROM members";
        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Member mem = new Member();
                mem.setMemberId(rs.getString("member_id"));
                mem.setTotalPoints(rs.getInt("total_points"));
                list.add(mem);
            }
        }
        return list;
    }

    @Override
    public void add(String memberId) throws SQLException {
        String sql = "INSERT INTO members (member_id, total_points) VALUES (?, 0)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ps.executeUpdate();
        }
    }

    @Override
    public int updatePoints(String memberId, int points) throws SQLException {
        String sql = "UPDATE members SET total_points=? WHERE member_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, points);
            ps.setString(2, memberId);
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(String memberId) throws SQLException {
        String sql = "DELETE FROM members WHERE member_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            return ps.executeUpdate();
        }
    }
}