package controllers;

import domain.common.Constants;
import features.SessionChecker;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import utilities.RedirectUtilities;

public class LogoutServlet extends HttpServlet {

    private static final SessionChecker SESSION_HANDLER = new SessionChecker();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SESSION_HANDLER.clearSession(request);
        RedirectUtilities.sendRedirect(request, response, Constants.MAIN_URL);
    }
}
