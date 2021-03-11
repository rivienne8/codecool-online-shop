package com.codecool.shop.dao;

import com.codecool.shop.model.log.Log;


public interface LogDao {
    void add(Log log);
    void save(Log log, String data);
}
