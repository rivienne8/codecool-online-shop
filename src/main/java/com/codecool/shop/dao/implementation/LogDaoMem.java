package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.LogDao;
import com.codecool.shop.model.log.Log;
import com.codecool.shop.util.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogDaoMem implements LogDao {
    private static Logger logger = LoggerFactory.getLogger(LogDaoMem.class);
    private static List<Log> logList = new ArrayList<>();
    private static LogDaoMem instance = null;

    public static LogDaoMem getInstance(){
        if (instance == null){
            instance = new LogDaoMem();
        }
        return instance;
    }

    private LogDaoMem(){}

    @Override
    public void add(Log log) {
        logList.add(log);
    }

    @Override
    public void save(Log log, String data) {
        try{
            JsonWriter.saveToFile(log, "logs/logHistoryForOrder", data);
        } catch (IOException e){
            logger.warn("Saving log for order id {} to file failed", log.getId());
        }

    }


}
