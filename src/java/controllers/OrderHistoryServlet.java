package controllers;

import entities.Constants;
import entities.Orders;
import entities.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class OrderHistoryServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session sessionChkUser = SessionUtilities.getLoginSession(request.getSession());
        if (sessionChkUser == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        if (SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Admin or staff are not allowed to view this page because it only for customer.", "/");
            return;
        }
        List<Orders> orders = entityManager.createNamedQuery("Orders.findByUserId").setParameter("userId", sessionChkUser.getUserId()).getResultList();
        Collections.reverse(orders);
        request.setAttribute("orderLists", orders);
        request.setAttribute("entityManager", entityManager);
        request.getRequestDispatcher(Constants.ORDERHISTORY_JSP_URL).forward(request, response);
    }
}
