package com.codecool.shop.dao.implementationJdbc;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductCategoryDaoJdbc implements ProductCategoryDao {
    private final DataSource dataSource;

    public ProductCategoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(ProductCategory category) {

    }

    @Override
    public ProductCategory find(int id) {
        String query = "SELECT * FROM product_categories WHERE id = ?";
        ProductCategory productCategory = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)){
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    productCategory = new ProductCategory(rs.getString("category_name"),
                        rs.getString("category_department"), rs.getString("category_description"));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productCategory;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<ProductCategory> getAll() {
        return null;
    }
}
