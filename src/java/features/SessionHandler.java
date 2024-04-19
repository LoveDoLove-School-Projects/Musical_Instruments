package features;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SessionHandler {

    /**
     * Sets the customer session with the specified customer ID.
     *
     * @param session The HttpSession object representing the current session.
     * @param customerId The ID of the customer.
     */
    public void setCustomerSession(HttpSession session, Integer customerId) {
        setLoginSession(session, customerId, Common.Role.CUSTOMER);
    }

    /**
     * Sets the admin session for the specified HttpSession object.
     *
     * @param session The HttpSession object to set the admin session for.
     * @param adminId The ID of the admin.
     */
    public void setAdminSession(HttpSession session, Integer adminId) {
        setLoginSession(session, adminId, Common.Role.ADMIN);
    }

    /**
     * Sets the login session attributes in the provided HttpSession object.
     *
     * @param session The HttpSession object to set the attributes in.
     * @param loginId The login ID to be stored in the session.
     * @param role The role to be stored in the session.
     */
    public void setLoginSession(HttpSession session, Integer loginId, Common.Role role) {
        session.setAttribute("login_id", loginId);
        session.setAttribute(Constants.ROLE_ATTRIBUTE, role);
    }

    /**
     * Represents a user session.
     */
    public Session getLoginSession(HttpSession session) throws IOException {
        Object role = session.getAttribute(Constants.ROLE_ATTRIBUTE);
        if (role == null) {
            return showSessionExpired();
        }
        Integer loginId = (Integer) session.getAttribute("login_id");
        if (loginId == null) {
            return showSessionExpired();
        }
        return new Session(true, loginId, (Common.Role) role);
    }

    /**
     * Represents a user session.
     */
    private Session showSessionExpired() throws IOException {
        return new Session(false, 0, Common.Role.UNKNOWN);
    }
}
