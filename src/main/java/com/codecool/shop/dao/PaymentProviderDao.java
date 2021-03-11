package com.codecool.shop.dao;

import com.codecool.shop.model.Payment;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.List;

public interface PaymentProviderDao {

    void add(Payment payment);
    Payment find(int id);
    void remove(int id);

    List<Payment> getAll();
    }
