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
     * Adds a security log entry based on the provided request, response,
     * session, and action.
     *
     * @param request the HttpServletRequest object representing the incoming
     * request
     * @param session the Session object representing the user session
     * @param action the action performed by the user
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void addSecurityLog(HttpServletRequest request, Session session, String action) throws ServletException, IOException {
        int userId = session.getUserId();
        String role = session.getRole().getRole();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        Securitylog securitylog = new Securitylog(userId, role, action, ipAddress, userAgent);
        securityLogServices.addSecurityLog(securitylog);
    }
}
