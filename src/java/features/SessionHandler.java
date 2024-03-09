package features;

import common.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import models.Session;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class SessionHandler {

    public Session checkLoginStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer loginIdOpt = SessionUtilities.getSessionAttributeInt(request.getSession(), "login_id");
        if (loginIdOpt != null) {
            return new Session(true, loginIdOpt);
        }
        RedirectUtilities.setErrorMessage(request, "Session expired. Please login again.");
        RedirectUtilities.sendRedirect(request, response, Constants.LOGIN_URL);
        return new Session(false, 0);
    }
}
