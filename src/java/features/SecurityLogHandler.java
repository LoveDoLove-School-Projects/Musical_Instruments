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
     * Adds a security log entry for the specified user action.
     *
     * @param request The HttpServletRequest object representing the incoming request.
     * @param session The Session object representing the user session.
     * @param action  The action performed by the user.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    public void addSecurityLog(HttpServletRequest request, Session session, String action) throws ServletException, IOException {
        int userId = session.getUserId();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        Securitylog securitylog = new Securitylog(userId, action, ipAddress, userAgent);
        securityLogServices.addSecurityLog(securitylog);
    }
}
