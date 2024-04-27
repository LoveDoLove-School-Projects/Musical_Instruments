package features;

import entities.Session;
import jakarta.servlet.http.HttpSession;

/**
 * This class provides methods to manage login sessions.
 */
public class SessionChecker {

    private static final String LOGIN_ID = "login_id";

    /**
     * Sets the login session attribute in the provided HttpSession object.
     *
     * @param session The HttpSession object to set the attribute in.
     * @param loginId The login ID to be stored in the session attribute.
     */
    public void setLoginSession(HttpSession session, Integer loginId) {
        if (session != null) {
            session.setAttribute(LOGIN_ID, loginId);
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
        Integer loginId = (Integer) session.getAttribute(LOGIN_ID);
        if (loginId == null) {
            return new Session(false, 0);
        }
        return new Session(true, loginId);
    }
}
