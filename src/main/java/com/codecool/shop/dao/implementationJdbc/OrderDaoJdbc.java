package com.codecool.shop.dao.implementationJdbc;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJdbc implements OrderDao {

    private static Logger logger = LoggerFactory.getLogger(OrderDaoJdbc.class);
    private DataSource dataSource;
    private CartDao cartDaoJdbc;

    public OrderDaoJdbc(DataSource dataSource, CartDao cartDaoJdbc) {

        this.cartDaoJdbc = cartDaoJdbc;
        this.dataSource = dataSource;
    }

    @Override
    public void add(Order order) {
        String query = "INSERT INTO  orders (user_id, currency) " +
                "VALUES (?,?)";

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (order.getUserId() == 0){
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setObject(1,order.getUserId());
            }
            //            ps.setInt(2,order.getCart().getId());
            ps.setString(2,order.getCart().getCurrency());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            order.setId(rs.getInt(1));

            cartDaoJdbc.updateOrderId(order.getCart(), order.getId());
            logger.debug("Adding order id: {} for cart id {} succeed", order.getId(), order.getCart().getId());

        } catch (SQLException e){
            e.printStackTrace();
            logger.error("Adding order for cart id {} failed", order.getCart().getId() );
        }
    }

    @Override
    public void save(Order order, String date) {
        String query = "UPDATE orders SET date_day = ? " +
                "WHERE id=?";

        executeDateUpdate(query, order, date);
    }


    @Override
    public void update(Order order){

        String query = "UPDATE orders SET " +
                "user_id = ?, " +
                "payment_method = ?," +
                "payment_status = ? " +
                "WHERE id=?";

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1,order.getUserId());
//            ps.setInt(2,order.getCart().getId());
            if (order.getPayment() == null){
                ps.setNull(2, Types.INTEGER);
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setString(2,order.getPayment().getName());
                ps.setInt(3,order.getPayment().getStatus());
            }

            ps.setInt(4,order.getId());

            ps.executeUpdate();
            logger.debug("Updating order id {}  succeed", order.getId());

        } catch (SQLException e){
            e.printStackTrace();
            logger.error("Updating order id {} failed", order.getId());
        }
    }


    private void executeDateUpdate(String query, Order order, String parameter ){
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1,parameter);
            ps.setInt(2,order.getId());

            ps.executeUpdate();
            logger.debug("Adding date for order id {}  succeed", order.getId());

        } catch (SQLException e){
            e.printStackTrace();
            logger.error("Adding date for order id {} failed", order.getId());
        }
    }

    @Override
    public List<Order> findAllByUserId(int userId){

        List<Order> orderList = new ArrayList<>();
        String query = "SELECT c.id, payment_method, " +
                "payment_status FROM orders o " +
                "LEFT JOIN  carts c ON o.id=c.order_id WHERE user_id =?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()){
                    int cartId = rs.getInt("id");
                    Order order = new Order(cartDaoJdbc.find(cartId));
                    order.setPayment(new Payment(rs.getString("payment_method")));
                    order.getPayment().setStatus(rs.getInt("payment_status"));
                    orderList.add(order);
                }
                logger.debug("Getting orders for user id: {} succeed", userId);

                return orderList;
            }
        } catch (SQLException e){
            logger.error("Getting orders  for user id {} failed", userId);
            throw new RuntimeException(e);
        }
    }
}

