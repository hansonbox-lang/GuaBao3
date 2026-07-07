package dao;

import java.sql.SQLException;
import java.util.List;
import model.Employee;

public interface EmployeeDao {
    Employee login(String id, String password) throws SQLException;
    List<Employee> getAll() throws SQLException;
    List<Employee> queryById(String id) throws SQLException;
    Employee getById(String id) throws SQLException;
    void add(Employee emp) throws SQLException;
    int update(Employee emp) throws SQLException;
    int delete(String id) throws SQLException;
}