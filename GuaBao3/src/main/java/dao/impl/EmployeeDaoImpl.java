package dao.impl;

import dao.EmployeeDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import util.DbUtil;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public Employee login(String id, String password) throws SQLException {
        String sql = "SELECT * FROM employees WHERE employee_id=? AND password=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setEmployeeId(rs.getString("employee_id"));
                    emp.setName(rs.getString("name"));
                    emp.setRole(rs.getString("role"));
                    return emp;
                }
            }
        }
        return null;
    }

    @Override
    public List<Employee> getAll() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT employee_id, name, role FROM employees";
        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getString("employee_id"));
                emp.setName(rs.getString("name"));
                emp.setRole(rs.getString("role"));
                list.add(emp);
            }
        }
        return list;
    }

    @Override
    public List<Employee> queryById(String id) throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT employee_id, name, role FROM employees WHERE employee_id LIKE ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + id + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.setEmployeeId(rs.getString("employee_id"));
                    emp.setName(rs.getString("name"));
                    emp.setRole(rs.getString("role"));
                    list.add(emp);
                }
            }
        }
        return list;
    }

    @Override
    public Employee getById(String id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE employee_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setEmployeeId(rs.getString("employee_id"));
                    emp.setPassword(rs.getString("password"));
                    emp.setName(rs.getString("name"));
                    emp.setRole(rs.getString("role"));
                    return emp;
                }
            }
        }
        return null;
    }

    @Override
    public void add(Employee emp) throws SQLException {
        String sql = "INSERT INTO employees (employee_id, password, name, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getEmployeeId());
            ps.setString(2, emp.getPassword());
            ps.setString(3, emp.getName());
            ps.setString(4, emp.getRole());
            ps.executeUpdate();
        }
    }

    @Override
    public int update(Employee emp) throws SQLException {
        String sql = "UPDATE employees SET password=?, name=?, role=? WHERE employee_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getPassword());
            ps.setString(2, emp.getName());
            ps.setString(3, emp.getRole());
            ps.setString(4, emp.getEmployeeId());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(String id) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        }
    }
}