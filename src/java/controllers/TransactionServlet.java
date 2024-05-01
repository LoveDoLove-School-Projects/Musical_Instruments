package controllers;

import common.Constants;
import entities.Session;
import features.SessionChecker;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import utilities.RedirectUtilities;

@WebServlet(name = "TransactionServlet", urlPatterns = {"/payments/processTransaction", "/payments/transaction"})
public class TransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER,
        // "Invalid request", Constants.MAIN_URL);
        request.getRequestDispatcher(Constants.TRANSACTION_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionChecker.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        processTransaction(request, response);
    }

    private void processTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
