package com.codecool.shop.dao.implementationJdbc;

import com.codecool.shop.dao.ShippingInfoDao;
import com.codecool.shop.model.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShippingInfoDaoJdbc implements ShippingInfoDao {
    private final DataSource dataSource;

    public ShippingInfoDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Customer customer) {
        String query = "INSERT INTO shipping_info (user_id, first_name, last_name, email, phone_number," +
                " country, city, post_code, street) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, customer.getId());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setString(4, customer.getEmail());
            statement.setString(5, customer.getPhoneNumber());
            statement.setString(6, customer.getCountry());
            statement.setString(7, customer.getCity());
            statement.setString(8, customer.getPostCode());
            statement.setString(9, customer.getStreet());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Customer get(int id) {
        Customer customerShippingInfo = null;
        String query = "SELECT first_name, last_name, email, phone_number, country," +
                " city, post_code, street FROM shipping_info WHERE user_id = ?";

        try (Connection con = dataSource.getConnection();
            PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    customerShippingInfo = new Customer(
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("city"),
                            rs.getString("country"),
                            rs.getString("post_code"),
                            rs.getString("street"),
                            rs.getString("phone_number"));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerShippingInfo;
    }
}
