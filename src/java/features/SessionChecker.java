package features;

import entities.Role;
import entities.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.nio.file.attribute.UserPrincipal;
import java.util.logging.Logger;

public class SessionChecker {

    private static final Logger LOG = Logger.getLogger(SessionChecker.class.getName());
    private static final String USER_SESSION = "user_session";

    /**
     * Sets the login session attribute in the provided HttpSession object.
     *
     * @param session The HttpSession object to set the attribute in.
     * @param loginId The login ID to be stored in the session attribute.
     * @param role The role to be stored in the session attribute.
     */
    public void setLoginSession(HttpSession session, Integer loginId, Role role) {
        if (session != null) {
            session.setAttribute(USER_SESSION, new Session(true, loginId, role));
        }
    }

    /**
     * Retrieves the login session attribute from the provided HttpSession
     * object.
     *
     * @param session The HttpSession object to get the attribute from.
     * @return A Session object representing the login session. If the session
     * or the login ID is null, a Session object representing a non-logged in
     * user is returned.
     */
    public Session getLoginSession(HttpSession session) {
        if (session == null) {
            return new Session(false, 0);
        }
        Session userSession = (Session) session.getAttribute(USER_SESSION);
        if (userSession == null) {
            return new Session(false, 0, Role.GUEST);
        }
        return userSession;
    }

    public boolean getIsAdminOrNot(HttpServletRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) request.getUserPrincipal();
        if (userPrincipal == null) {
            return false;
        }
        return request.isUserInRole("Admin");
    }
}
