package utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class RedirectUtilities {

    public static void redirectWithMessage(HttpServletRequest request, HttpServletResponse response, String message, String redirectUrl) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        response.sendRedirect(redirectUrl);
    }
}
