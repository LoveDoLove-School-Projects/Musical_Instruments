package utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class RedirectUtilities {

    public enum RedirectType {
        PRIMARY("primary"),
        SECONDARY("secondary"),
        SUCCESS("success"),
        DANGER("danger"),
        WARNING("warning"),
        INFO("info"),
        LIGHT("light"),
        DARK("dark");
        private final String redirectType;

        private RedirectType(String redirectType) {
            this.redirectType = redirectType;
        }

        public String getRedirectType() {
            return redirectType;
        }
    }

    public static void redirectWithMessage(HttpServletRequest request, HttpServletResponse response, RedirectType messageType, String message, String redirectUrl) throws IOException {
        setMessage(request, messageType, message);
        sendRedirect(request, response, redirectUrl);
    }

    public static void setMessage(HttpServletRequest request, RedirectType messageType, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("messageStatus", messageType.getRedirectType());
        session.setAttribute("message", message);
    }

    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws IOException {
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
