package service;

import exception.GuaBaoException;
import java.sql.SQLException;
import java.util.List;
import model.Employee;
import model.Member;
import model.OrderDetail;

public interface GuaBaoService {
    Employee employeeLogin(String id, String password) throws SQLException, GuaBaoException;
    List<Employee> getAllEmployees() throws SQLException;
    List<Employee> queryEmployees(String id) throws SQLException;
    Employee getEmployeeById(String id) throws SQLException;
    void addEmployee(Employee emp) throws SQLException, GuaBaoException;
    void updateEmployee(Employee emp) throws SQLException, GuaBaoException;
    void deleteEmployee(String id) throws SQLException, GuaBaoException;

    Member memberLogin(String memberId) throws SQLException, GuaBaoException;
    void addMember(String memberId) throws SQLException, GuaBaoException;
    List<Member> getAllMembers() throws SQLException;
    void updateMemberPoints(String memberId, String ptsStr) throws Exception;
    void deleteMember(String memberId) throws SQLException, GuaBaoException;

    int checkout(String memberId, int totalAmount, List<OrderDetail> details, int currentPoints) throws SQLException, GuaBaoException;
}