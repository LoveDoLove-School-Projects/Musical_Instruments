package contollers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionController {

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/Musical_Instruments_DB", "root", "admin@1234");
        } catch (ClassNotFoundException | SQLException ex) {
            connection.close();
            System.out.println(ex);
        }
        return connection; // returns the connection.
    }
}
