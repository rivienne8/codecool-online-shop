package com.codecool.shop.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cart {

    private int id;
    private int userId;
    private String currency;
    private Map<Integer, ProductLine> productLines;

    public Cart(){
        this.productLines = new HashMap<Integer, ProductLine>();
        currency = "USD";
    }


    public void add(ProductLine productLine){
        productLines.put(productLine.getProduct().getId(), productLine);
    }


    public float getTotalPrice(){

        float totalPrice = productLines.values().stream()
                .map(v -> v.getTotalPrice())
                .reduce(0.0f, (a,b) -> a + b);

        return totalPrice;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotalPriceString(){
        return ""+ getTotalPrice() + " " + currency ;
    }



    public Map<Integer, ProductLine> getProductLines() {
        return productLines;
    }

    public void setProductLines(Map<Integer, ProductLine> productLines) {
        this.productLines = productLines;
    }


    public void remove(int id){
            productLines.remove(id);
    }

    public void decreaseQuantity(int id){
        ProductLine line = productLines.get(id);
        if (line.getQuantity() > 1){
            productLines.get(id).changeQuantity(-1);
        } else if (line.getQuantity() == 1){
            productLines.remove(id);
        }
    }

    public void increaseQuantity(int id){
        ProductLine line = productLines.get(id);
        line.changeQuantity(1);
    }

    public int getQuantity() {
        int quantity = productLines.values().stream()
                .map(line -> line.getQuantity())
                .reduce(0, (a,b) -> a+b);
        return quantity;
    }

    public int getSize(){
        return productLines.size();
    }

    public void clear() {productLines.clear();}

    public String getItems() {
        String items = "";

        for (ProductLine value :productLines.values()) {
            items += value.getProduct().getName() + " price " + value.getProduct().getPrice() + " x "
                    + value.getQuantity() + " = " + value.getTotalPriceString() + "\n";
        }
        return items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ProductLine> getProductLinesValues(){
        return productLines.values().stream().collect(Collectors.toList());
    }
}
