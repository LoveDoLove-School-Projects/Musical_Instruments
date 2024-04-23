package features;

import domain.models.Session;
import entities.Securitylog;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import services.SecurityLogServices;

public class SecurityLogHandler {

    private static final SecurityLogServices securityLogServices = new SecurityLogServices();

    /**
     * Adds a security log entry for the specified request, session, and action.
     *
     * @param request The HttpServletRequest object representing the current
     * request.
     * @param session The Session object representing the current user session.
     * @param action The action performed that triggered the security log entry.
     * @throws ServletException If an error occurs while processing the request.
     * @throws IOException If an I/O error occurs.
     */
    public void addSecurityLog(HttpServletRequest request, Session session, String action) throws ServletException, IOException {
        addSecurityLog(request, session.getUserId(), action);
    }

    /**
     * Adds a security log entry for the specified user and action.
     *
     * @param request the HttpServletRequest object representing the current
     * request
     * @param userId the ID of the user performing the action
     * @param action the action performed by the user
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void addSecurityLog(HttpServletRequest request, int userId, String action) throws ServletException, IOException {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        Securitylog securitylog = new Securitylog(userId, action, ipAddress, userAgent);
        securityLogServices.addSecurityLog(securitylog);
    }
}
