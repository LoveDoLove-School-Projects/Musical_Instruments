package features;

import dao.SecurityLogDao;
import entities.Internalsecuritylog;
import entities.Securitylog;
import entities.Session;
import jakarta.servlet.http.HttpServletRequest;

public class SecurityLog {

    public static void addSecurityLog(HttpServletRequest request, String action) {
        Session session = SessionChecker.getLoginSession(request);
        if (session == null) {
            return;
        }
        int userId = session.getUserId();
        String username = getUsername(request, session);
        String email = session.getEmail();
        String role = session.getRole().getRole();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        new SecurityLogDao().addSecurityLog(new Securitylog(userId, username, email, role, action, ipAddress, userAgent));
    }

    public static void addInternalSecurityLog(HttpServletRequest request, String action) {
        Session session = SessionChecker.getLoginSession(request);
        String username = getUsername(request, session);
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        if (SessionChecker.getIsAdminOrNot(request)) {
            new SecurityLogDao().addInternalSecurityLog(new Internalsecuritylog(0, username, null, action, ipAddress, userAgent));
            return;
        }
        if (session != null) {
            int userId = session.getUserId();
            String email = session.getEmail();
            new SecurityLogDao().addInternalSecurityLog(new Internalsecuritylog(userId, username, email, action, ipAddress, userAgent));
        }
    }

    private static String getUsername(HttpServletRequest request, Session session) {
        if (session == null) {
            return SessionChecker.getPrincipalName(request);
        }
        return session.getUsername();
    }
}
