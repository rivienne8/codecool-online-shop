package com.codecool.shop.dao;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductLine;
import com.codecool.shop.model.jdbcModel.ProductLineModel;

import java.util.List;

public interface ProductLineDao {

    ProductLine find(int id);
    void add(ProductLine productLine, int cartId);
    List<ProductLine> getAll(int cartId);
    void remove(int id);
    void removeAll(int cartId);
    void update(ProductLine productLine, int cartId);
    void addAll(List<ProductLine> lists, int cartId);
    void updateAll(List<ProductLine> lists, int cartId);

}
