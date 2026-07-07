package dao.impl;

import dao.OrderDao;
import java.sql.*;
import java.util.List;
import model.Order;
import model.OrderDetail;
import util.DbUtil;

public class OrderDaoImpl implements OrderDao {
    @Override
    public int executeCheckoutTransaction(Order order, List<OrderDetail> details, int updatedMemberPoints) throws SQLException {
        int generatedOrderId = -1;
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (order.getMemberId() != null) {
                String sqlUpdateMember = "UPDATE members SET total_points = ? WHERE member_id = ?";
                try (PreparedStatement psMem = conn.prepareStatement(sqlUpdateMember)) {
                    psMem.setInt(1, updatedMemberPoints);
                    psMem.setString(2, order.getMemberId());
                    psMem.executeUpdate();
                }
            }

            String sqlOrder = "INSERT INTO orders (member_id, total_amount, earned_points, gift_count, order_time) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                if (order.getMemberId() != null) psOrder.setString(1, order.getMemberId());
                else psOrder.setNull(1, Types.VARCHAR);
                psOrder.setInt(2, order.getTotalAmount());
                psOrder.setInt(3, order.getEarnedPoints());
                psOrder.setInt(4, order.getGiftCount());
                psOrder.setTimestamp(5, order.getOrderTime());
                psOrder.executeUpdate();

                try (ResultSet rsKeys = psOrder.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        generatedOrderId = rsKeys.getInt(1);
                    }
                }
            }

            String sqlDetail = "INSERT INTO order_details (order_id, item_name, unit_price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                for (OrderDetail detail : details) {
                    psDetail.setInt(1, generatedOrderId);
                    psDetail.setString(2, detail.getItemName());
                    psDetail.setInt(3, detail.getUnitPrice());
                    psDetail.setInt(4, detail.getQuantity());
                    psDetail.setInt(5, detail.getSubtotal());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) conn.rollback();
            throw ex;
        } finally {
            if (conn != null) conn.close();
        }
        return generatedOrderId;
    }
}