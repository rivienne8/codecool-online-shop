package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Payment extends  BaseModel{

    private int status;
    private List<Payment> payments;

    public Payment(String name) {
        super(name);
        this.payments = new ArrayList<>();
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
