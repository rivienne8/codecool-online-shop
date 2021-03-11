package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.util.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoMem implements OrderDao {
    private static Logger logger = LoggerFactory.getLogger(OrderDaoMem.class);
   private List<Order> data = new ArrayList<>();
   private static OrderDaoMem instance = null;


   private OrderDaoMem(){}

    public static OrderDaoMem getInstance(){
        if (instance == null){
            instance = new OrderDaoMem();
        }
        return instance;
    }


    @Override
    public void add(Order order) {
        order.setId(data.size() + 1);
        data.add(order);
    }

    @Override
    public void save(Order order, String data) {
       try {
           JsonWriter.saveToFile(order, "orders/order", data);
           logger.info("Order saved to file successfully, id {}", order.getId());
       } catch (IOException e){
           logger.error("Saving order id {} to file failed", order.getId());
       }

    }

    @Override
    public void update(Order order) {

    }

    @Override
    public List<Order> findAllByUserId(int userId) {
        return null;
    }


}
