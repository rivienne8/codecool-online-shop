package com.codecool.shop.model.log;

import com.codecool.shop.model.Gsonable;

import java.util.ArrayList;
import java.util.List;

public class Log implements Gsonable {

    private int id;
    private List<LogItem> logList;

    public Log() {
        logList = new ArrayList<>();
    }

    public void add(LogItem item){
        item.setId(logList.size()+1);
        if (logList.size() == 0){
            setId(item.getOrderId());
        }
        logList.add(item);
    }


    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void clear() {
        logList.clear();
    }
}
