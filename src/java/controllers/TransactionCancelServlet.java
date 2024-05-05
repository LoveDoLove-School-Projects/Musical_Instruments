package controllers;

import common.Constants;
import entities.Session;
import utilities.SessionUtilities;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import utilities.RedirectUtilities;

public class TransactionCancelServlet extends HttpServlet {

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
        request.getRequestDispatcher(CANCEL_JSP_URL).forward(request, response);
    }
}
