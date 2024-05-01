package controllers;

import common.Constants;
import entities.Session;
import features.SessionChecker;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import utilities.RedirectUtilities;

public class PaymentCancelServlet extends HttpServlet {

    private static final String CANCEL_JSP_URL = "/payments/cancel.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionChecker.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        request.getRequestDispatcher(CANCEL_JSP_URL).forward(request, response);
    }
}
