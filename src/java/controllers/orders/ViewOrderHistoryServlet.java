package controllers.orders;

import entities.Constants;
import entities.OrderDetails;
import entities.Orders;
import entities.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import services.TransactionServices;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class ViewOrderHistoryServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private final TransactionServices transactionServices = new TransactionServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        if (SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Admin or staff are not allowed to view this page because it only for customer.", "/");
            return;
        }
        String order_number = request.getParameter("order_number");
        List<Orders> orders = entityManager.createNamedQuery("Orders.findByOrderNumber").setParameter("orderNumber", order_number).getResultList();
        request.setAttribute("orderLists", orders);
        request.setAttribute("entityManager", entityManager);
        OrderDetails orderDetails = transactionServices.getOrderHistoryDetails(orders);
        request.setAttribute("subtotal", orderDetails.getSubtotal());
        request.setAttribute("shipping", orderDetails.getShipping());
        request.setAttribute("tax", orderDetails.getTax());
        request.setAttribute("total", orderDetails.getTotal());
        request.getRequestDispatcher(Constants.VIEWORDERHISTORY_JSP_URL).forward(request, response);
    }
}
