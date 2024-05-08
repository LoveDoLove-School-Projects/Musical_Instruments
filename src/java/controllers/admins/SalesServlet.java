package controllers.admins;

import entities.OrderDetails;
import entities.Orders;
import entities.Sales;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import services.TransactionServices;
import utilities.RedirectUtilities;

public class SalesServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private static final String SALES_REPORT_JSP_URL = "/pages/superAdmin/salesReport.jsp";
    private static final String VIEW_SALES_JSP_URL = "/pages/superAdmin/viewSales.jsp";
    private static final String VIEW_SALES_URL = "/pages/superAdmin/viewSales";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final TransactionServices transactionServices = new TransactionServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Orders> orderList = entityManager.createNamedQuery("Orders.findAll", Orders.class).getResultList();
        List<Sales> salesList = new ArrayList<>();
        for (Orders order : orderList) {
            salesList.add(new Sales(order.getProductName(), order.getOrderDate(), order.getProductQuantity(), order.getProductTotalprice()));
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
        request.setAttribute("top10Products", combinedSalesList);
        request.getRequestDispatcher(VIEW_SALES_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String date = request.getParameter("date");
        String reportType = request.getParameter("reportType");
        try {
            List<Orders> orderList = entityManager.createNamedQuery("Orders.findAll", Orders.class).getResultList();
            List<Orders> filterOrderList = new ArrayList<>();
            Date requestDate = dateFormat.parse(date);
            for (Orders order : orderList) {
                Date orderDate = order.getOrderDate();
                if (orderDate != null && orderDate instanceof Date) {
                    Date formattedOrderDate = new Date(orderDate.getTime());
                    if (!dateFormat.format(formattedOrderDate).equals(dateFormat.format(requestDate))) {
                        continue;
                    }
                    filterOrderList.add(order);
                }
            }

            List<Sales> salesList = new ArrayList<>();
            Map<String, String> isCalculated = new HashMap<>();
            for (Orders order : filterOrderList) {
                String orderNumber = order.getOrderNumber();
//                if (isCalculated.containsKey(orderNumber)) {
//                    continue;
//                }
                List<Orders> getSpecificOrderList = entityManager.createNamedQuery("Orders.findByOrderNumber", Orders.class)
                        .setParameter("orderNumber", orderNumber)
                        .getResultList();
                OrderDetails orderDetails = transactionServices.getOrderHistoryDetails(getSpecificOrderList);
                salesList.add(new Sales(order.getProductName(), order.getOrderDate(), order.getProductQuantity(), BigDecimal.valueOf(Double.parseDouble(orderDetails.getTotal()))));
                isCalculated.put(orderNumber, orderNumber);
            }

            // Group sales by product name and calculate total amount
            Map<String, BigDecimal> productSales = new HashMap<>();
            for (Sales sale : salesList) {
                String productName = sale.getProductName();
                BigDecimal totalPrice = sale.getTotalAmount();
                if (productSales.containsKey(productName)) {
                    // If product already exists, update total amount
                    totalPrice = totalPrice.add(productSales.get(productName));
                }
                productSales.put(productName, totalPrice);
            }

            // Create a new list of combined sales
            List<Sales> combinedSalesList = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> entry : productSales.entrySet()) {
                String productName = entry.getKey();
                BigDecimal totalAmount = entry.getValue();
                combinedSalesList.add(new Sales(productName, requestDate, 0, totalAmount));
            }

            // Handle other report types here
            request.setAttribute("salesList", combinedSalesList);
            request.setAttribute("reportType", reportType);
            request.getRequestDispatcher(SALES_REPORT_JSP_URL).forward(request, response);
        } catch (ParseException ex) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Error in converting date!", VIEW_SALES_URL);
        }
    }

}
