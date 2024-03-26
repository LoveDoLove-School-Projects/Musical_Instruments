package features;

import domain.common.Common;
import domain.models.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import utilities.SessionUtilities;

public class SessionHandler {

    public void setCustomerSession(HttpSession session, Integer customerId) {
        setLoginSession(session, customerId, Common.Role.CUSTOMER);
    }

    public void setAdminSession(HttpSession session, Integer adminId) {
        setLoginSession(session, adminId, Common.Role.ADMIN);
    }

    public void setLoginSession(HttpSession session, Integer loginId, Common.Role role) {
        SessionUtilities.setSessionAttribute(session, "login_id", loginId);
        SessionUtilities.setSessionAttribute(session, "role", role);
    }

    public Session getLoginSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object role = SessionUtilities.getSessionAttribute(request.getSession(), "role");
        if (role == null) {
            return showSessionExpired();
        }
        Integer loginId = SessionUtilities.getSessionAttributeInt(request.getSession(), "login_id");
        if (loginId == null) {
            return showSessionExpired();
        }
        return new Session(true, loginId, (Common.Role) role);
    }

    private Session showSessionExpired() throws IOException {
        return new Session(false, 0, Common.Role.UNKNOWN);
    }
}
