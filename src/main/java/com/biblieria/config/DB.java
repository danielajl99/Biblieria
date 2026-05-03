package com.biblieria.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DB {
    private static final String DEFAULT_URL = "jdbc:mariadb://127.0.0.1:3306/biblieria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USER = "biblieria_app";
    private static final String DEFAULT_PASS = "biblieria123";
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String MARIADB_DRIVER = "org.mariadb.jdbc.Driver";

    private DB() {}

    public static Connection getConnection() throws SQLException {
        String url = valueOrDefault(System.getenv("DB_URL"), DEFAULT_URL);
        loadDriver(url);
        String user = valueOrDefault(System.getenv("DB_USER"), DEFAULT_USER);
        String pass = valueOrDefault(System.getenv("DB_PASS"), DEFAULT_PASS);
        return DriverManager.getConnection(url, user, pass);
    }

    private static void loadDriver(String url) throws SQLException {
        String driverClass = url.startsWith("jdbc:mariadb:") ? MARIADB_DRIVER : MYSQL_DRIVER;
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver JDBC para la URL: " + url, e);
        }
    }

    private static String valueOrDefault(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }
}
