package dao;

import java.sql.SQLException;
import java.util.List;
import model.Member;

public interface MemberDao {
    Member getById(String memberId) throws SQLException;
    List<Member> getAll() throws SQLException;
    void add(String memberId) throws SQLException;
    int updatePoints(String memberId, int points) throws SQLException;
    int delete(String memberId) throws SQLException;
}