package controllers.admins;

import entities.Orders;
import entities.Sales;
import entities.Transactions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private static final String SALES_REPORT_JSP_URL = "/pages/superAdmin/salesReport.jsp";
    private static final String VIEW_SALES_JSP_URL = "/pages/superAdmin/viewSales.jsp";
    private static final String VIEW_SALES_URL = "/pages/superAdmin/viewSales";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final TransactionServices transactionServices = new TransactionServices();

    class Reports {

        private List<Sales> salesList;
        private BigDecimal totalRevenue;
        private Date startingDate;
        private Date endingDate;

        public Reports() {
        }

        public Reports(List<Sales> salesList, BigDecimal totalRevenue, Date startingDate, Date endingDate) {
            this.salesList = salesList;
            this.totalRevenue = totalRevenue;
            this.startingDate = startingDate;
            this.endingDate = endingDate;
        }

        public List<Sales> getSalesList() {
            return salesList;
        }

        public void setSalesList(List<Sales> salesList) {
            this.salesList = salesList;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public Date getStartingDate() {
            return startingDate;
        }

        public void setStartingDate(Date startingDate) {
            this.startingDate = startingDate;
        }

        public Date getEndingDate() {
            return endingDate;
        }

        public void setEndingDate(Date endingDate) {
            this.endingDate = endingDate;
        }

    }

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String date = request.getParameter("date");
        String reportType = request.getParameter("reportType");
        try {
            List<Transactions> transactionList = entityManager.createNamedQuery("Transactions.findAllByTransactionStatusApproved", Transactions.class).getResultList();
            Reports reports = new Reports();
            switch (action) {
                case "specificReport":
                    if (date != null && !date.isEmpty()) {
                        reports = getDayReport(transactionList, dateFormat.parse(date));
                    }
                    break;
                case "generateReport":
                    switch (reportType) {
                        case DAY:
                            reports = getDayReport(transactionList, new Date());
                            break;
                        case MONTH: {
                            reports = getMonthReport(transactionList);
                            break;
                        }
                        case YEAR: {
                            reports = getYearReport(transactionList);
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            // Set attributes for JSP and forward to sales report page
            request.setAttribute("salesList", reports.getSalesList());
            request.setAttribute("reportType", reportType);
            request.setAttribute("startingDate", dateFormat.format(reports.getStartingDate()));
            request.setAttribute("endingDate", dateFormat.format(reports.getEndingDate()));
            request.setAttribute("totalRevenue", reports.getTotalRevenue());
            request.getRequestDispatcher(SALES_REPORT_JSP_URL).forward(request, response);
        } catch (Exception ex) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please choose a valid date to fetch the sales!", VIEW_SALES_URL);
        }
    }

    private Reports getDayReport(List<Transactions> transactionList, Date requestDate) {
        List<Sales> salesList = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Transactions transaction : transactionList) {
            Date transactionDate = transaction.getDateCreatedGmt();
            if (transactionDate != null && transactionDate instanceof Date) {
                Date formattedTransactionDate = new Date(transactionDate.getTime());
                if (requestDate != null) {
                    if (!dateFormat.format(formattedTransactionDate).equals(dateFormat.format(requestDate))) {
                        continue;
                    }
                }
                salesList.add(new Sales(transaction.getTransactionNumber(), transaction.getDateCreatedGmt(), 1, transaction.getTotalAmount()));
                totalRevenue = totalRevenue.add(transaction.getTotalAmount());
            }
        }
        return new Reports(salesList, totalRevenue, requestDate, requestDate);
    }

    private Reports getMonthReport(List<Transactions> transactionList) {
        List<Sales> salesList = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDayOfMonth = calendar.getTime();
        for (Transactions transaction : transactionList) {
            Date transactionDate = transaction.getDateCreatedGmt();
            if (transactionDate != null && transactionDate.after(firstDayOfMonth) && transactionDate.before(lastDayOfMonth)) {
                salesList.add(new Sales(transaction.getTransactionNumber(), transaction.getDateCreatedGmt(), 1, transaction.getTotalAmount()));
                totalRevenue = totalRevenue.add(transaction.getTotalAmount());
            }
        }
        return new Reports(salesList, totalRevenue, firstDayOfMonth, lastDayOfMonth);
    }

    private Reports getYearReport(List<Transactions> transactionList) {
        List<Sales> salesList = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfYear = calendar.getTime();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date lastDayOfYear = calendar.getTime();
        for (Transactions transaction : transactionList) {
            Date transactionDate = transaction.getDateCreatedGmt();
            if (transactionDate != null && transactionDate.after(firstDayOfYear) && transactionDate.before(lastDayOfYear)) {
                salesList.add(new Sales(transaction.getTransactionNumber(), transaction.getDateCreatedGmt(), 1, transaction.getTotalAmount()));
                totalRevenue = totalRevenue.add(transaction.getTotalAmount());
            }
        }
        return new Reports(salesList, totalRevenue, firstDayOfYear, lastDayOfYear);
    }
}
