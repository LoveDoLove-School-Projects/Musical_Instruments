package presentation.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionController {

    private static final String HOST = "jdbc:derby://localhost:1527/Musical_Instruments_DB";
    private static final String USER = "root";
    private static final String PASSWORD = "admin@1234";

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
//            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection(HOST, USER, PASSWORD);
        } catch (SQLException ex) {
//            connection.close();
            System.out.println(ex);
        }
        return connection; // returns the connection.
    }
}
