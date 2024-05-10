package dao;

import controllers.ConnectionController;
import entities.Products;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductDao {

    private static final Logger LOG = Logger.getLogger(ProductDao.class.getName());
    private static final String SEARCH_PRODUCT_SQL = "SELECT * FROM PRODUCTS WHERE UPPER(name) LIKE ? OR UPPER(category) LIKE ?";

    public List<Products> searchProduct(String searchQuery) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_PRODUCT_SQL)) {
            String searchTerm = "%" + searchQuery.toUpperCase() + "%";
            preparedStatement.setString(1, searchTerm);
            preparedStatement.setString(2, searchTerm);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Products> productList = new ArrayList<>();
                while (resultSet.next()) {
                    Products product = new Products();
                    product.setProductId(resultSet.getInt("product_id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setColor(resultSet.getString("color"));
                    product.setQuantity(resultSet.getInt("quantity"));
                    product.setCategory(resultSet.getString("category"));
                    product.setImage(resultSet.getBytes("image"));
                    productList.add(product);
                }
                return productList;
            }
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }
}
