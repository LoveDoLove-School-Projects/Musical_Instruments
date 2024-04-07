package services;

import controllers.ConnectionController;
import domain.models.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductServices {

//    private final String IMG_PATH_SQL = "SELECT * FROM products WHERE images_path = ?";
    
    private final String PRODUCT_DETAILS_SQL = "SELECT * FROM products";
    private final String VIEW_PRODUCT_WITH_ID_SQL = "SELECT * FROM products WHERE Product_Id = ?";

    public List<Product> getAllProducts() {
        List<Product> productData = new ArrayList<>();

        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(PRODUCT_DETAILS_SQL); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String color = resultSet.getString("color");
                int quantity = resultSet.getInt("quantity");
                String category = resultSet.getString("category");
                String image_path = resultSet.getString("image_path");
                productData.add(new Product(id, name, price, color, quantity, category, image_path));
            }
        } catch (SQLException ex) {
            System.err.println("Error" + ex.getMessage());
        }
        return productData;
    }

    public Product getProductWithID(String product_id) {
        Product product = null;
        try (Connection connection = ConnectionController.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(VIEW_PRODUCT_WITH_ID_SQL); 
                    ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String color = resultSet.getString("color");
                int quantity = resultSet.getInt("quantity");
                String category = resultSet.getString("category");
                String image_path = resultSet.getString("image_path");
                product = new Product(id, name, price, color, quantity, category, image_path);
            }
        } catch (SQLException ex) {
            System.err.println("Error" + ex.getMessage());
        }
        return product;
    }

}
