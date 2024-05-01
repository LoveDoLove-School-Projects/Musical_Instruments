package dao;

import controllers.ConnectionController;
import entities.Transactions;
import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionDao {

    private static final String INSERT_NEW_TRANSACTION_SQL = "INSERT INTO Transactions VALUES (?, ?, ?, ?, ?, ?, ?)";

    public void insertNewTransaction(Transactions transaction) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_TRANSACTION_SQL)) {
            preparedStatement.setInt(1, transaction.getUserId());
            preparedStatement.setString(2, transaction.getTransactionNumber());
            preparedStatement.setString(3, transaction.getOrderNumber());
            preparedStatement.setString(4, transaction.getTransactionStatus());
            preparedStatement.setString(5, transaction.getPaymentMethod());
            preparedStatement.setString(6, transaction.getCurrency());
            preparedStatement.setBigDecimal(7, transaction.getTotalAmount());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
