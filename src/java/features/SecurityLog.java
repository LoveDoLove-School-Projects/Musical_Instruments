package features;

import dao.SecurityLogDao;
import entities.Internalsecuritylog;
import entities.Securitylog;
import entities.Session;
import jakarta.servlet.http.HttpServletRequest;

public class SecurityLog {

    public static void addSecurityLog(HttpServletRequest request, String action) {
        Session session = SessionChecker.getLoginSession(request);
        int userId = session.getUserId();
        String username = getUsername(request, session);
        String email = session.getEmail();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        new SecurityLogDao().addSecurityLog(new Securitylog(userId, username, email, action, ipAddress, userAgent));
    }

    public static void addInternalSecurityLog(HttpServletRequest request, String action) {
        Session session = SessionChecker.getLoginSession(request);
        int userId = session.getUserId();
        String username = getUsername(request, session);
        String email = session.getEmail();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        new SecurityLogDao().addInternalSecurityLog(new Internalsecuritylog(userId, username, email, action, ipAddress, userAgent));
    }

    private static String getUsername(HttpServletRequest request, Session session) {
        if (session == null) {
            return SessionChecker.getPrincipalName(request);
        }
        return session.getUsername();
    }
}
