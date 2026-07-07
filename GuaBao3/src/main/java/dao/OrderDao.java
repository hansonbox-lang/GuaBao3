package dao;

import java.sql.SQLException;
import java.util.List;
import model.Order;
import model.OrderDetail;

public interface OrderDao {
    int executeCheckoutTransaction(Order order, List<OrderDetail> details, int updatedMemberPoints) throws SQLException;
}