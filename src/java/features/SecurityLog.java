package features;

import dao.SecurityLogDao;
import entities.Internalsecuritylog;
import entities.Securitylog;
import entities.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SecurityLog {

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
    public static void addSecurityLog(HttpServletRequest request, Session session, String action) throws ServletException, IOException {
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
    public static void addSecurityLog(HttpServletRequest request, int userId, String action) throws ServletException, IOException {
        String username = getUsername(request);
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        Securitylog securitylog = new Securitylog(userId, action, ipAddress, userAgent);
        new SecurityLogDao().addSecurityLog(securitylog);
    }

    /**
     * Adds an internal security log entry based on the provided request,
     * username, and action.
     *
     * @param request the HttpServletRequest object representing the incoming
     * request
     * @param username the username associated with the security log entry
     * @param action the action performed for the security log entry
     * @throws ServletException if there is a servlet exception
     * @throws IOException if there is an I/O exception
     */
    public static void addInternalSecurityLog(HttpServletRequest request, String action) throws ServletException, IOException {
        String username = getUsername(request);
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        Internalsecuritylog internalsecuritylog = new Internalsecuritylog(username, action, ipAddress, userAgent);
        new SecurityLogDao().addInternalSecurityLog(internalsecuritylog);
    }

    private static String getUsername(HttpServletRequest request) {
        Session session = SessionChecker.getLoginSession(request);
        if (session == null) {
            return SessionChecker.getPrincipalName(request);
        }
        return session.getUsername();
    }
}
