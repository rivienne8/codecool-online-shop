package com.codecool.shop.util;

public enum DaoNames {
    JDBC("jdbc"),
    MEMORY("memory");

    private final String daoName;

    DaoNames(String daoName) {
        this.daoName = daoName;
    }

    public String getDaoName() {
        return daoName;
    }
}
