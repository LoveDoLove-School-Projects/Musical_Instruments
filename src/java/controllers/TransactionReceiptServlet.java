package controllers;

import entities.Constants;
import entities.Session;
import entities.Transactions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class TransactionReceiptServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final Logger LOG = Logger.getLogger(TransactionReceiptServlet.class.getName());
    private static final String RECEIPT_JSP_URL = "/payments/receipt.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        String transactionNumber = request.getParameter("transaction_number");
        if (transactionNumber == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid transaction number!", "/");
            return;
        }
        Transactions transaction = getTransactions(transactionNumber, session.getUserId());
        if (transaction == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Transaction not found!", "/");
            return;
        }
        request.setAttribute("transaction", transaction);
        request.getRequestDispatcher(RECEIPT_JSP_URL).forward(request, response);
    }

    private Transactions getTransactions(String transactionNumber, int userId) {
        try {
            return entityManager.createNamedQuery("Transactions.findByTransactionNumberAndUserId", Transactions.class)
                    .setParameter("transactionNumber", transactionNumber)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }
}
