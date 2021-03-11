package com.codecool.shop.dao;

import com.codecool.shop.model.Order;

import java.util.List;

public interface OrderDao {

    void add(Order order);
    void save(Order order, String data);
    void update(Order order);
    List<Order> findAllByUserId(int userId);

}
