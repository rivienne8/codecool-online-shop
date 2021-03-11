package com.codecool.shop.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
    private final Properties appProps = new Properties();
    private String url;
    private int portNumber;
    private String database;
    private String user;
    private String password;
    private final DaoNames daoName;

    public PropertiesReader() {
        try {
            String appConfig = "src/main/resources/connection.properties";
            appProps.load(new FileInputStream(appConfig));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (appProps.getProperty("dao").equals(DaoNames.JDBC.getDaoName())) {
            this.daoName = DaoNames.JDBC;
            this.url = appProps.getProperty("url").split(":")[0];
            this.portNumber = Integer.parseInt(appProps.getProperty("url").split(":")[1]);
            this.database = appProps.getProperty("database");
            this.user = appProps.getProperty("user");
            this.password = appProps.getProperty("password");

        } else {
            this.daoName = DaoNames.MEMORY;
        }

    }

    public DaoNames getDaoName() {
        return daoName;
    }

    public String getUrl() {
        return url;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
