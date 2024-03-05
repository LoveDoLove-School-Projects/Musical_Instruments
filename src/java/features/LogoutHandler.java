package features;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import utilities.SessionUtilities;

public class LogoutHandler {

    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        SessionUtilities.removeSessionAttribute(request.getSession(), "login_id");
    }
}
