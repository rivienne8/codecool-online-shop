package com.codecool.shop.dao.implementationJdbc;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJdbc implements ProductDao {
    private final DataSource dataSource;

    public ProductDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Product product) {

    }

    @Override
    public Product find(int id) {

        String query = "SELECT p.id , p.default_price, p.product_name, p.currency, " +
                "p.description, pc.category_name, pc.category_department, pc.category_description, " +
                "s.supplier_name, s.supplier_description FROM products AS p " +
                "INNER JOIN product_categories AS pc on pc.id = p.category_id " +
                "INNER JOIN suppliers s on p.supplier_id = s.id " +
                "WHERE p.id = ?";


        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)){
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                Product product = new Product(rs.getString("product_name"),
                        Float.parseFloat(rs.getString("default_price")), rs.getString("currency"),
                        rs.getString("description"), new ProductCategory(rs.getString("category_name"),
                        rs.getString("category_department"), rs.getString("category_description")),
                        new Supplier(rs.getString("supplier_name"), rs.getString("supplier_description")));
                product.setId(rs.getInt("id"));
                return product;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Product> getAll() {
        return null;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return null;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        String query = "SELECT * FROM products AS p " +
                "INNER JOIN product_categories AS pc on pc.id = p.category_id " +
                "INNER JOIN suppliers s on p.supplier_id = s.id " +
                "WHERE pc.category_name = ?";

        List<Product> productList = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, productCategory.getName());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(rs.getString("product_name"),
                            Float.parseFloat(rs.getString("default_price")), rs.getString("currency"),
                            rs.getString("description"), new ProductCategory(rs.getString("category_name"),
                            rs.getString("category_department"), rs.getString("category_description")),
                            new Supplier(rs.getString("supplier_name"), rs.getString("supplier_description")));
                    product.setId(rs.getInt("id"));
                    productList.add(product);

                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productList;
    }
}
