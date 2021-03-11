package com.codecool.shop.util;

import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class JDBCConnection {
    public DataSource connect(String serverName, int portNumber, String dataBaseName, String user, String password) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName(serverName);
        dataSource.setPortNumber(portNumber);
        dataSource.setDatabaseName(dataBaseName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        return dataSource;
    }
}
