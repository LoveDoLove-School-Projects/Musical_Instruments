package dao;

import controllers.ConnectionController;
import entities.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SessionDao {

    private static final Logger LOG = Logger.getLogger(SessionDao.class.getName());
    private static final String CHECK_SESSION_SQL = "SELECT * FROM table_name WHERE user_id = ?";

    public boolean checkSessionIsExistOrNot(Session session) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(replaceQuery(session.getRole().getRole()))) {
            preparedStatement.setInt(1, session.getUserId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    private String replaceQuery(String role) {
        return CHECK_SESSION_SQL.replace("table_name", role);
    }
}
