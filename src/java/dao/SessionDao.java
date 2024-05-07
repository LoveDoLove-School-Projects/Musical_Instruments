package dao;

import controllers.ConnectionController;
import entities.Session;
import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SessionDao {

    private static final String CHECK_SESSION_SQL = "SELECT * FROM table_name WHERE user_id = ?";
    private final Map<String, Boolean> cache = new HashMap<>();

    public boolean checkSessionIsExistOrNot(Session session) throws DatabaseException {
        String key = session.getRole().getRole() + "-" + session.getUserId();
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(replaceQuery(session.getRole().getRole()))) {
            preparedStatement.setInt(1, session.getUserId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean result = resultSet.next();
                cache.put(key, result);
                return result;
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private String replaceQuery(String role) {
        return CHECK_SESSION_SQL.replace("table_name", role);
    }
}
