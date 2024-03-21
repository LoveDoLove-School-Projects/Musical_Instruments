package features;

import domain.common.Common;
import domain.common.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import domain.models.Session;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class SessionChecker {

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
            return showSessionExpired(request, response);
        }
        Integer loginId = SessionUtilities.getSessionAttributeInt(request.getSession(), "login_id");
        if (loginId == null) {
            return showSessionExpired(request, response);
        }
        return new Session(true, loginId, (Common.Role) role);
    }

    public void clearSession(HttpServletRequest request) {
        clearSession(request.getSession());
    }

    public void clearSession(HttpSession session) {
        session.invalidate();
    }

    private Session showSessionExpired(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RedirectUtilities.setErrorMessage(request, "Session expired. Please login again.");
        RedirectUtilities.sendRedirect(request, response, Constants.MAIN_URL);
        return new Session(false, 0, Common.Role.UNKNOWN);

    }
}
