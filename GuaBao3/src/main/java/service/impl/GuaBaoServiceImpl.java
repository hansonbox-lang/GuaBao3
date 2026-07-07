package service.impl;

import dao.EmployeeDao;
import dao.MemberDao;
import dao.OrderDao;
import dao.impl.EmployeeDaoImpl;
import dao.impl.MemberDaoImpl;
import dao.impl.OrderDaoImpl;
import exception.GuaBaoException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import model.Employee;
import model.Member;
import model.Order;
import model.OrderDetail;
import service.GuaBaoService;

public class GuaBaoServiceImpl implements GuaBaoService {
    // 多型宣告：使用介面指向實作物件
    private EmployeeDao employeeDao = new EmployeeDaoImpl();
    private MemberDao memberDao = new MemberDaoImpl();
    private OrderDao orderDao = new OrderDaoImpl();

    @Override
    public Employee employeeLogin(String id, String password) throws SQLException, GuaBaoException {
        if (id.isEmpty() || password.isEmpty()) throw new GuaBaoException("請輸入帳號與密碼！");
        Employee emp = employeeDao.login(id, password);
        if (emp == null) throw new GuaBaoException("帳號或密碼錯誤！");
        return emp;
    }

    @Override public List<Employee> getAllEmployees() throws SQLException { return employeeDao.getAll(); }
    @Override public List<Employee> queryEmployees(String id) throws SQLException { return employeeDao.queryById(id); }
    @Override public Employee getEmployeeById(String id) throws SQLException { return employeeDao.getById(id); }
    
    @Override
    public void addEmployee(Employee emp) throws SQLException, GuaBaoException {
        if(emp.getEmployeeId().isEmpty() || emp.getPassword().isEmpty() || emp.getName().isEmpty()) {
            throw new GuaBaoException("所有欄位皆為必填！");
        }
        employeeDao.add(emp);
    }
    
    @Override
    public void updateEmployee(Employee emp) throws SQLException, GuaBaoException {
        if(emp.getEmployeeId().isEmpty()) throw new GuaBaoException("請指定員工帳號！");
        int rows = employeeDao.update(emp);
        if(rows == 0) throw new GuaBaoException("找不到該員工帳號！");
    }
    
    @Override
    public void deleteEmployee(String id) throws SQLException, GuaBaoException {
        if(id.isEmpty()) throw new GuaBaoException("請指定員工帳號！");
        int rows = employeeDao.delete(id);
        if(rows == 0) throw new GuaBaoException("找不到該員工帳號！");
    }

    @Override
    public Member memberLogin(String memberId) throws SQLException, GuaBaoException {
        if (memberId.isEmpty()) throw new GuaBaoException("請輸入會員卡號！");
        Member mem = memberDao.getById(memberId);
        if (mem == null) throw new GuaBaoException("查無此會員！");
        return mem;
    }
    
    @Override
    public void addMember(String memberId) throws SQLException, GuaBaoException {
        if (memberId.isEmpty()) throw new GuaBaoException("會員卡號資料輸入欄位要有文字，才執行新增會員功能！");
        memberDao.add(memberId);
    }
    
    @Override public List<Member> getAllMembers() throws SQLException { return memberDao.getAll(); }
    
    @Override
    public void updateMemberPoints(String memberId, String ptsStr) throws Exception {
        if (memberId.isEmpty() || ptsStr.isEmpty()) throw new GuaBaoException("請填入欲修改之會員卡號與點數！");
        int points = Integer.parseInt(ptsStr);
        int rows = memberDao.updatePoints(memberId, points);
        if(rows == 0) throw new GuaBaoException("找不到該會員！");
    }
    
    @Override
    public void deleteMember(String memberId) throws SQLException, GuaBaoException {
        if (memberId.isEmpty()) throw new GuaBaoException("請填入欲刪除之會員卡號！");
        int rows = memberDao.delete(memberId);
        if(rows == 0) throw new GuaBaoException("找不到該會員！");
    }

    @Override
    public int checkout(String memberId, int totalAmount, List<OrderDetail> details, int currentPoints) throws SQLException, GuaBaoException {
        Order order = new Order();
        order.setMemberId(memberId);
        order.setTotalAmount(totalAmount);
        order.setOrderTime(new Timestamp(System.currentTimeMillis()));

        int earnedPoints = totalAmount / 100;
        order.setEarnedPoints(earnedPoints);

        int updatedPoints = currentPoints;
        int giftCount = 0;

        if (memberId != null) {
            updatedPoints += earnedPoints;
            if (updatedPoints >= 10) {
                giftCount = updatedPoints / 10;
                updatedPoints -= (giftCount * 10);
            }
        }
        order.setGiftCount(giftCount);

        return orderDao.executeCheckoutTransaction(order, details, updatedPoints);
    }
}