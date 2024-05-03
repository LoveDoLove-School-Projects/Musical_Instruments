package controllers;

import common.Constants;
import entities.Securitylog;
import entities.Session;
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

public class SecurityLogServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final String SECURITY_LOG_JSP_URL = "/pages/securityLog.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        List<Securitylog> securitylogs = entityManager.createNamedQuery("Securitylog.findByUserIdAndRole", Securitylog.class)
                .setParameter("userId", session.getUserId())
                .setParameter("role", session.getRole().getRole())
                .getResultList();
        Collections.reverse(securitylogs);
        request.setAttribute("securitylogs", securitylogs);
        request.getRequestDispatcher(SECURITY_LOG_JSP_URL).forward(request, response);
    }
}
