package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;

public interface CartDao {
    Cart findOpenByUserId(int userId);
    Cart find(int id);
    void add(Cart cart, int userId);
    void update(Cart cart);
    void delete(int id);
    void updateOrderId(Cart cart, int orderId);

}
