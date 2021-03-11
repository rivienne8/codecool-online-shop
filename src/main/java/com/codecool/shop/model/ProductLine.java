package com.codecool.shop.model;

import java.util.Objects;

public class ProductLine {
    private static int counter = 0;
    private int id;
    private Product product;
    private int quantity;


    public ProductLine(Product product){
        this.product = product;
        this.quantity = 1;
    }

    public ProductLine(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }



    public float getTotalPrice(){
        return quantity * product.getDefaultPrice();
    }

    public String getTotalPriceString(){
        return getTotalPrice() + " " + product.getDefaultCurrency();
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void changeQuantity(int i) {
        setQuantity(quantity + i);
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductLine that = (ProductLine) o;
        return id == that.id && quantity == that.quantity && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, quantity);
    }
}
