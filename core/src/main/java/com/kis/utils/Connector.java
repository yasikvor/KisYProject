package com.kis.utils;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    private static Connection connection;

    public static void connect(String url, String user, String pass) throws SQLException {

            if(url.equals("") || user.equals(""))
                throw new SQLException();
            DriverManager.registerDriver(new FabricMySQLDriver());
            connection = DriverManager.getConnection(url, user, pass);
    }

    public static void connect(Connection con) {
        connection = con;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() throws SQLException {
        connection.close();
        connection = null;
    }
}
