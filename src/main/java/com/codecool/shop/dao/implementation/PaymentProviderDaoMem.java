package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.PaymentProviderDao;
import com.codecool.shop.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentProviderDaoMem implements PaymentProviderDao {

    private List<Payment> data = new ArrayList<>();
    private static PaymentProviderDaoMem instance = null;


    public static PaymentProviderDaoMem getInstance() {
        if (instance == null) {
            instance = new PaymentProviderDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Payment payment) {
        payment.setId(data.size() + 1);
        data.add(payment);
    }

    @Override
    public Payment find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<Payment> getAll() {
        return data;
    }
}
