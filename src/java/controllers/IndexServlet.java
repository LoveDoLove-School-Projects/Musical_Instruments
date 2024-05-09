/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import entities.Orders;
import entities.Sales;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final String INDEX_JSP_URL = "/index.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Orders> orderList = entityManager.createNamedQuery("Orders.findAll", Orders.class).getResultList();
        List<Sales> salesList = new ArrayList<>();
        for (Orders order : orderList) {
            salesList.add(new Sales(order.getProductId(), order.getProductName(), order.getOrderDate(), order.getProductQuantity(), order.getProductTotalprice(), order.getProductImage()));
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
            combinedSalesList.add(new Sales(productName, null, totalQuantity, null));
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
        request.setAttribute("topsales", combinedSalesList);
        request.getRequestDispatcher(INDEX_JSP_URL).forward(request, response);
    }

}
