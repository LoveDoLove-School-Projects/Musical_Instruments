package controllers;

import common.Constants;
import entities.Session;
import entities.TransactionStatus;
import entities.Transactions;
import exceptions.DatabaseException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.Date;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;

public class TransactionCancelServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final String CANCEL_JSP_URL = "/payments/cancel.jsp";

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
        HttpSession httpSession = request.getSession();
        String transaction_number = (String) httpSession.getAttribute("transaction_number");
        if (transaction_number == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Transaction number is required.", Constants.CART_URL);
            return;
        }
        if (!updateTransactionToDB(session, transaction_number)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to update transaction.", Constants.CART_URL);
            return;
        }
        SecurityLog.addSecurityLog(request, "Transaction cancelled: " + transaction_number);
        request.getRequestDispatcher(CANCEL_JSP_URL).forward(request, response);
    }

    private boolean updateTransactionToDB(Session session, String transaction_number) throws DatabaseException {
        try {
            userTransaction.begin();
            Transactions dbTransaction = entityManager.createNamedQuery("Transactions.findByTransactionNumberAndUserId", Transactions.class).setParameter("transactionNumber", transaction_number).setParameter("userId", session.getUserId()).getSingleResult();
            dbTransaction.setTransactionStatus(TransactionStatus.CANCELLED);
            dbTransaction.setDateUpdatedGmt(new Date());
            entityManager.merge(dbTransaction);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
