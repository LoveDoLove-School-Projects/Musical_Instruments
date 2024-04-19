package features;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SessionHandler {

    public void setCustomerSession(HttpSession session, Integer customerId) {
        setLoginSession(session, customerId, Common.Role.CUSTOMER);
    }

    public void setAdminSession(HttpSession session, Integer adminId) {
        setLoginSession(session, adminId, Common.Role.ADMIN);
    }

    public void setLoginSession(HttpSession session, Integer loginId, Common.Role role) {
        session.setAttribute("login_id", loginId);
        session.setAttribute(Constants.ROLE_ATTRIBUTE, role);
    }

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

    private Session showSessionExpired() throws IOException {
        return new Session(false, 0, Common.Role.UNKNOWN);
    }
}
