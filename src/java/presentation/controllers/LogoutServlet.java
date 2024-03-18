package presentation.controllers;

import domain.common.Constants;
import application.features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import application.utilities.RedirectUtilities;

public class LogoutServlet extends HttpServlet {

    private static final SessionHandler SESSION_HANDLER = new SessionHandler();

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
