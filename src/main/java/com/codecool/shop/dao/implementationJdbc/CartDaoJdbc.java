package com.codecool.shop.dao.implementationJdbc;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductLineDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.ProductLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class CartDaoJdbc implements CartDao {

    private static Logger logger = LoggerFactory.getLogger(CartDaoJdbc.class);
    private DataSource dataSource;
    private ProductLineDao productLineDao;

    public CartDaoJdbc(DataSource dataSource, ProductLineDao productLineDao) {
        this.productLineDao = productLineDao;
        this.dataSource = dataSource;
    }

    @Override
    public Cart find(int id){
        String query = "SELECT  id, user_id, currency " +
                " FROM carts " +
                "WHERE id = ?";

        Cart cart  = executeFind(query, id, "cart");
        return cart;
    }

    @Override
    public Cart findOpenByUserId(int userId) {

        String query = "SELECT  id, user_id, currency " +
                " FROM carts " +
                "WHERE user_id = ? AND order_id IS NULL ";

        Cart cart  = executeFind(query, userId, "user");
        return cart;
    }

    private Cart executeFind(String query, int userId , String kindOfid){
        Cart cart = new Cart();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()){
                    cart.setId(rs.getInt("id"));
                    cart.setCurrency(rs.getString("currency"));
                    cart.setUserId(rs.getInt("user_id"));
                }
                List<ProductLine> productLineList = productLineDao.getAll(cart.getId());
                for (ProductLine productLine: productLineList){
                    cart.add(productLine);
                }
                logger.debug("Getting cart  for {} id: {} succeed", kindOfid,userId);

                return cart;
            }
        } catch (SQLException e){
            logger.error("Getting cart  for {} id: {} failed", kindOfid, userId);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Cart cart, int userId){

        String query = "INSERT INTO  carts (user_id, currency) VALUES (?, ?)";

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,userId);
            ps.setString(2,cart.getCurrency());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            cart.setId(rs.getInt(1));

            productLineDao.addAll(cart.getProductLinesValues(), cart.getId());
            logger.debug("Adding cart id: {} succeed", cart.getId());


        } catch (SQLException e){
            e.printStackTrace();
            logger.error("Adding cart  failed" );
        }
    }


    public void update(Cart cart){
        delete(cart.getId());
        if (cart.getSize() >0){
            add(cart,cart.getUserId());
        }

    }

    @Override
    public void delete(int id) {
        productLineDao.removeAll(id);
        String query = "DELETE  FROM carts WHERE id = ? ";

        try (Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.debug("Deleting cart id: {} succeed", id);

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Deleting cart id {}  failed", id );

        }
    }

    public void updateOrderId(Cart cart, int orderId){
        String query = "UPDATE carts SET " +
                "order_id = ? WHERE id=?";

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1,orderId);
            ps.setInt(2,cart.getId());

            ps.executeUpdate();
            logger.debug("Adding order id {} to cart id {}  succeed", orderId, cart.getId());

        } catch (SQLException e){
            e.printStackTrace();
            logger.error("Adding order id {} to cart id {}  failed", orderId, cart.getId());
        }

    }


}
