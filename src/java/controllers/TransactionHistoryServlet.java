package controllers;

import common.Constants;
import entities.Session;
import entities.Transactions;
import utilities.SessionUtilities;
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

public class TransactionHistoryServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final String TRANSACTION_HISTORY_JSP_URL = "/pages/transactionHistory.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        if (SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Admin or staff are not allowed to view this page because it only for customer.", "/");
            return;
        }
        List<Transactions> transactionList = entityManager.createNamedQuery("Transactions.findByUserId", Transactions.class)
                .setParameter("userId", session.getUserId())
                .getResultList();
        Collections.reverse(transactionList);
        request.setAttribute("transactionList", transactionList);
        request.getRequestDispatcher(TRANSACTION_HISTORY_JSP_URL).forward(request, response);
    }
}
