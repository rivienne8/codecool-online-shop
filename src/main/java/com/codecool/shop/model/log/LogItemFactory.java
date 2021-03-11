package com.codecool.shop.model.log;

import com.codecool.shop.model.Order;

public class LogItemFactory {

    private static StringBuilder builder = new StringBuilder();

    public static LogItem create(LogName name, Order order){
        LogItem item;

        switch (name){
            case CREATE:
                item = new LogItem(name, order.getId());
                builder.append("orderId: ");
                builder.append(order.getId());
                builder.append(", cart size: ");
                builder.append(order.getCart().getSize());
                item.setDescription(builder.toString());
                break;
            case ERROR:
                item = new LogItem(name, order.getId());
                item.setDescription(builder.toString());
                break;
            case ADD_ADDRESS:
                item = new LogItem(name, order.getId());
                builder.append("user:  ");
                builder.append(order.getCustomer().getId());
                item.setDescription(builder.toString());
                break;
            case VALIDATE_PAYMENT:
                item = new LogItem(name, order.getId());
                builder.append("payment Method:  ");
                builder.append(order.getPayment().getName());
                builder.append("payment status:  ");
                builder.append(order.getPayment().getStatus());
                item.setDescription(builder.toString());
                break;
            case FINISHED:
                item = new LogItem(name, order.getId());
                builder.append("Order finished successfully");
                item.setDescription(builder.toString());
                break;
            default:
                item = null;

        }
        builder.setLength(0);
        return item;
    }
}
