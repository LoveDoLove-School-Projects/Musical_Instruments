package features;

import entities.Role;
import entities.Session;
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
        if (getIsAdminOrNot(session)) {
            userSession.setRole(Role.ADMIN);
        }
        return userSession;
    }

    /**
     * Checks if the user in the given session is an admin.
     *
     * @param session the HttpSession object containing the user session
     * @return true if the user is an admin, false otherwise
     */
    private boolean getIsAdminOrNot(HttpSession session) {
        UserPrincipal userPrincipal = (UserPrincipal) session.getAttribute("javax.security.auth.subject");
        return userPrincipal != null && userPrincipal.getName().equals("ADMIN");
    }
}
