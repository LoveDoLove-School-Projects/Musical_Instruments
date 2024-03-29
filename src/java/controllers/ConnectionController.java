package controllers;

import exceptions.DatabaseAccessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionController {

    private static final String HOST = "jdbc:derby://localhost:1527/Musical_Instruments_DB";
    private static final String USER = "nbuser";
    private static final String PASSWORD = "nbuser";

    public static Connection getConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            return connection;
        } catch (ClassNotFoundException | SQLException ex) {
            throw new DatabaseAccessException("Error establishing database connection", ex);
        }
    }
}
