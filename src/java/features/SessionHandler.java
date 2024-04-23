package features;

import domain.models.Session;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SessionHandler {

    /**
     * Sets the login session attributes in the provided HttpSession object.
     *
     * @param session The HttpSession object to set the attributes in.
     * @param loginId The login ID to be stored in the session.
     */
    public void setLoginSession(HttpSession session, Integer loginId) {
        session.setAttribute("login_id", loginId);
    }

    /**
     * Represents a user session.
     */
    public Session getLoginSession(HttpSession session) throws IOException {
        Integer loginId = (Integer) session.getAttribute("login_id");
        if (loginId == null) {
            return showSessionExpired();
        }
        return new Session(true, loginId);
    }

    /**
     * Represents a user session.
     */
    private Session showSessionExpired() throws IOException {
        return new Session(false, 0);
    }
}
