package com.codecool.shop.dao;

import com.codecool.shop.model.Customer;

public interface ShippingInfoDao {
    void add(Customer customer);
    Customer get(int id);
}
