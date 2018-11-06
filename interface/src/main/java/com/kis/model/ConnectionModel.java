package com.kis.model;

import com.kis.constant.DriverNameConstants;
import com.kis.loader.dbloader.LoaderManager;
import com.kis.utils.Connector;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionModel {
    private static String server;

    private static String url;

    private static String port;

    private static String schema;

    private static String user;

    private static Connection connection;

    public static String getServer() {
        return server;
    }

    public static void setUrl(String url) {
        ConnectionModel.url = url;
        url = url.replace(DriverNameConstants.MY_SQL, "");
        ConnectionModel.server = url.substring(0, url.indexOf(":"));
        ConnectionModel.port = url.substring(url.indexOf(":") + 1, url.indexOf("/"));
        ConnectionModel.schema = url.substring(url.indexOf("/") + 1, url.length());
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        ConnectionModel.user = user;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static String getPort() {
        return port;
    }

    public static String getSchema() {
        return schema;
    }

    public static void setSchema(String schema) {
        ConnectionModel.schema = schema;
    }

    public static void close() throws SQLException {
        connection = null;
        Connector.close();
    }

    public static void setConnection(String password) throws SQLException{
        Connector.connect(url, user, password);
        connection = Connector.getConnection();
    }

    public static void setConnection(String server, String port, String schema, String user, String password) throws SQLException {
        ConnectionModel.server = server;
        ConnectionModel.port = port;
        ConnectionModel.schema = schema;
        ConnectionModel.user = user;

        if(schema.equals(""))
            throw new SQLException();
        StringBuilder url = new StringBuilder();
        url.append(DriverNameConstants.MY_SQL).append(server);
        if(!port.equals("")){
            url.append(":").append(port);
        }
        url.append("/").append(schema);
        ConnectionModel.url = url.toString();
        Connector.connect(url.toString(), user, password);
        connection = Connector.getConnection();
        NodeModel.setRoot(LoaderManager.getRoot());
    }

    public static String getUrl() {
        return url;
    }
}
