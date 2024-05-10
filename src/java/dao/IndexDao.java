package dao;

import controllers.ConnectionController;
import entities.Orders;
import entities.Sales;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class IndexDao {

    private static final Logger LOG = Logger.getLogger(IndexDao.class.getName());
    private static final String ORDER_LIST_SQL = "SELECT * FROM Orders";

    public static List<Sales> getTopProducts() {
        List<Orders> orderList = getOrderList();
        List<Sales> salesList = new ArrayList<>();
        for (Orders order : orderList) {
            salesList.add(new Sales(order.getProductId(), order.getProductName(), order.getProductQuantity(), order.getProductImage()));
        }
        Map<String, Integer> productSales = new HashMap<>();
        for (Sales sale : salesList) {
            String productName = sale.getProductName();
            Integer totalQuantity = sale.getTotalQuantity();
            if (productSales.containsKey(productName)) {
                totalQuantity = totalQuantity + (productSales.get(productName));
            }
            productSales.put(productName, totalQuantity);
        }
        // Create a new list of combined sales
        List<Sales> combinedSalesList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
            String productName = entry.getKey();
            Integer totalQuantity = entry.getValue();
            for (Sales sale : salesList) {
                if (sale.getProductName().equals(productName)) {
                    combinedSalesList.add(new Sales(sale.getProductId(), productName, totalQuantity, sale.getProductImage()));
                    break;
                }
            }
        }
        // Sort the combined sales list in descending order based on total quantity
        Collections.sort(combinedSalesList, new Comparator<Sales>() {
            @Override
            public int compare(Sales sale1, Sales sale2) {
                // Compare total quantities in descending order
                return Integer.compare(sale2.getTotalQuantity(), sale1.getTotalQuantity());
            }
        });
        // Handle other report types here
        return combinedSalesList;
    }

    private static List<Orders> getOrderList() {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ORDER_LIST_SQL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Orders> orderList = new ArrayList<>();
                while (resultSet.next()) {
                    Orders order = new Orders();
                    order.setProductId(resultSet.getInt("product_id"));
                    order.setProductName(resultSet.getString("product_name"));
                    order.setProductQuantity(resultSet.getInt("product_quantity"));
                    order.setProductImage(resultSet.getBytes("product_image"));
                    orderList.add(order);
                }
                return orderList;
            }
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }
}
