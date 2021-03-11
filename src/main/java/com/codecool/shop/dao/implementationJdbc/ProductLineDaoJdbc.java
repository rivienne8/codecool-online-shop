package com.codecool.shop.dao.implementationJdbc;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.ProductLineDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.ProductLine;
import com.codecool.shop.model.jdbcModel.ProductLineModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductLineDaoJdbc implements ProductLineDao {

    private static Logger logger = LoggerFactory.getLogger(ProductLineDaoJdbc.class);
    private DataSource dataSource;
    private ProductDao productDao;

    public ProductLineDaoJdbc(DataSource dataSource, ProductDao productDao){
        this.dataSource = dataSource;
        this.productDao = productDao;
    }

    @Override
    public ProductLine find(int id) {

        String query = "SELECT  id, cart_id, product_id, quantity " +
                " FROM product_lines " +
                "WHERE id = ?";

        ProductLine productLine = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                rs.first();
                int prodId = rs.getInt("product_id");
                int quantity =rs.getInt("quantity");
                int prodLineId = rs.getInt("id");


                Product product = productDao.find(prodId);
                productLine = new ProductLine(product,quantity);
                productLine.setId(prodLineId);
                logger.debug("Getting products and quantity  for productLine id: {} succeed", id);

                return productLine;

            }
        } catch (SQLException e){
            logger.error("Getting products and quantity  for product line id: {} failed", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(ProductLine productLine, int cartId){
        ProductLineModel productLineModel = new ProductLineModel(productLine);

        String query = "INSERT INTO  product_lines (cart_id, product_id, quantity) VALUES (?,?,?)";

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,cartId);
            ps.setInt(2,productLineModel.getProductId());
            ps.setInt(3,productLineModel.getQuantity());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            productLine.setId(rs.getInt(1));
            logger.debug("Adding products and quantity  for productLine id: {} succeed", productLine.getId());


        } catch (SQLException e){
            e.printStackTrace();
            logger.error("Adding products and quantity  for product line  failed" );
        }
    }

    @Override
    public void addAll(List<ProductLine> productLines, int cartId){

        for (ProductLine productLine : productLines){
            add(productLine, cartId);
        }
    }


    @Override
    public void update(ProductLine productLine, int cartId){
//        ProductLineModel productLineModel = new ProductLineModel(productLine);

        String query = "UPDATE product_lines SET quantity = ? " +
                "WHERE product_id=? AND cart_id=?";

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1,productLine.getQuantity());
            ps.setInt(2,productLine.getProduct().getId());
            ps.setInt(3,cartId);

            ps.executeUpdate();

            logger.debug("Updating products and quantity  for productLine id: {} succeed", productLine.getId());


        } catch (SQLException e){
            e.printStackTrace();
            logger.error("Updating products and quantity  for product line  failed" );
        }
    }

    @Override
    public  void updateAll(List<ProductLine> productLines, int cartId){
        for (ProductLine productLine : productLines){
            update(productLine, cartId);
        }

    }

    @Override
    public void remove(int id) {
        String query = "DELETE  FROM product_lines WHERE id = ? ";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.debug("Deleting products and quantity  for productLine id: {} succeed", id);

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Deleting products and quantity  for product line  failed" );

        }
    }

    @Override
    public void removeAll(int cartId){
        String query = "DELETE  FROM product_lines WHERE cart_id = ? ";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
            logger.debug("Deleting products and quantity  for cart id: {} succeed", cartId);

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Deleting products and quantity  for cart id {}  failed", cartId );

        }

    }


    @Override
    public List<ProductLine> getAll(int cartId) {

        String query = "SELECT  id, product_id, quantity " +
                " FROM product_lines " +
                "WHERE cart_id = ?";

        List<ProductLine> productLines = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, cartId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()){
                    int prodId = rs.getInt("product_id");
                    int quantity =rs.getInt("quantity");
                    Product product = productDao.find(prodId);
                    productLines.add(new ProductLine(product,quantity));
                }
                logger.debug("Getting productLines for car id: {} succeed", cartId);

                return productLines;
            }
        } catch (SQLException e){
            logger.error("Getting productLine for car id: {} failed", cartId);
            throw new RuntimeException(e);
        }
    }
}
