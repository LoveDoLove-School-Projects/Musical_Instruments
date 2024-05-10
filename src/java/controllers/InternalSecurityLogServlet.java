package controllers;

import entities.Internalsecuritylog;
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

public class InternalSecurityLogServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final String SECURITY_LOG_JSP_URL = "/pages/admins/internalSecurityLog.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        List<Internalsecuritylog> internalSecuritylogs = entityManager.createNamedQuery("Internalsecuritylog.findAll", Internalsecuritylog.class).getResultList();
        Collections.reverse(internalSecuritylogs);
        request.setAttribute("internalSecuritylogs", internalSecuritylogs);
        request.getRequestDispatcher(SECURITY_LOG_JSP_URL).forward(request, response);
    }
}
