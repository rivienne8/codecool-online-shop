package com.codecool.shop.model.log;

import java.time.LocalDateTime;

public class LogItem {

    protected int id;
    protected int orderId;
    protected LogName name;
    protected LocalDateTime date;
    protected String description;

    public LogItem(LogName name, int orderId, String description) {
        this.name = name;
        this.orderId = orderId;
        this.description = description;
        this.date = LocalDateTime.now();

    }

    public LogItem(LogName name, int orderId){
        this.name = name;
        this.orderId = orderId;
        this.date = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LogName getName() {
        return name;
    }

    public void setName(LogName name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


