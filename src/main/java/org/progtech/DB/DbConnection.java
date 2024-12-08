package org.progtech.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    private Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "connect4db";
        String databaseUser = "root";
        String databasePassword = "";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }

        return databaseLink;
    }
}
