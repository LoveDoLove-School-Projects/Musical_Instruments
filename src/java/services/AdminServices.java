package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.common.Constants;
import domain.request.AdminRequest;
import domain.response.AdminResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminServices {

    private static final String GET_ADMIN_NAME_SQL = "SELECT * FROM admins WHERE user_id = ?";

    public AdminResponse getAdminProfile(AdminRequest adminRequest) {
        AdminResponse adminResponse = new AdminResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_ADMIN_NAME_SQL)) {
            preparedStatement.setInt(1, adminRequest.getLogin_id());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString(Constants.USERNAME_ATTRIBUTE);
                    adminResponse.setStatus(Common.Status.OK);
                    adminResponse.setUsername(username);
                } else {
                    adminResponse.setStatus(Common.Status.NOT_FOUND);
                }
            }
        } catch (SQLException ex) {
            adminResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println("Error getting admin profile: " + ex.getMessage());
        }
        return adminResponse;
    }
}
