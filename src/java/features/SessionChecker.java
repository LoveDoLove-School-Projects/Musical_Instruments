package features;

import entities.Role;
import entities.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;

public class SessionChecker {

    private static final String USER_SESSION = "user_session";

    public static void setLoginSession(HttpSession session, Session userSession) {
        if (session != null) {
            session.setAttribute(USER_SESSION, userSession);
        }
    }

    public static Session getLoginSession(HttpServletRequest request) {
        return getLoginSession(request.getSession());
    }

    public static Session getLoginSession(HttpSession session) {
        if (session != null) {
            Session userSession = (Session) session.getAttribute(USER_SESSION);
            if (userSession == null) {
                return null;
            }
            return userSession;
        }
        return null;
    }

    private static boolean getIsAdminOrNot(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return false;
        }
        return request.isUserInRole("Admin");
    }

    public static String getPrincipalName(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return null;
        }
        return principal.getName();
    }

    public static boolean checkIsStaffOrAdmin(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        boolean isAdmin = getIsAdminOrNot(request);
        Session session = getLoginSession(httpSession);
        return isAdmin || session != null && session.getRole() == Role.STAFF;
    }
}
