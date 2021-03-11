package com.codecool.shop.model.jdbcModel;

import com.codecool.shop.model.ProductLine;

public class ProductLineModel {

    private int id;
    private int productId;
    private int quantity;

    public ProductLineModel(ProductLine productLine){
        this.productId = productLine.getProduct().getId();
        this.quantity = productLine.getQuantity();
    }

    public ProductLineModel(int id, int productId, int quantity){
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
