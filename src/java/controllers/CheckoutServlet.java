package controllers;

import common.Constants;
import entities.Carts;
import entities.OrderDetails;
import entities.Session;
import features.SessionChecker;
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
import utilities.RedirectUtilities.RedirectType;

public class CheckoutServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private final TransactionServices transactionServices = new TransactionServices();
    private static final String CHECKOUT_JSP_URL = "/payments/checkout.jsp";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = SessionChecker.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        List<Carts> cartList = entityManager.createNamedQuery("Carts.findByCustomerId", Carts.class).setParameter("customerId", session.getUserId()).getResultList();
        if (cartList == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Cart is empty.", Constants.CART_URL);
            return;
        }
        OrderDetails orderDetails = transactionServices.getOrderDetails(cartList);
        request.setAttribute("cartList", cartList);
        request.setAttribute("subtotal", orderDetails.getSubtotal());
        request.setAttribute("shipping", orderDetails.getShipping());
        request.setAttribute("tax", orderDetails.getTax());
        request.setAttribute("total", orderDetails.getTotal());
        request.getRequestDispatcher(CHECKOUT_JSP_URL).forward(request, response);
    }
}
