package com.example.inventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final static String url = "jdbc:mysql://localhost:3306/INV1";
    private final static String userName = "root";
    private final static String password = "Mysql@1234";
    private static Connection connection;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL driver", e);
        }
        try {
            connection =  DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
        	System.out.println("Database Connection Failure");
            throw new RuntimeException(e);
        }
    }
    
    public static Connection getConnection() {
        return connection;
    }
}