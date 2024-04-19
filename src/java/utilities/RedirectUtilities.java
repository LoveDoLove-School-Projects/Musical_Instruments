package utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class RedirectUtilities {

    /**
     * Enum representing different types of redirects.
     */
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

        /**
         * Constructs a RedirectType with the specified redirect type.
         *
         * @param redirectType the redirect type
         */
        private RedirectType(String redirectType) {
            this.redirectType = redirectType;
        }

        /**
         * Returns the redirect type.
         *
         * @return the redirect type
         */
        public String getRedirectType() {
            return redirectType;
        }
    }

    /**
     * Redirects the user to a specified URL with a message and message type.
     *
     * @param request the HttpServletRequest object representing the client's
     * request
     * @param response the HttpServletResponse object representing the server's
     * response
     * @param messageType the type of message to be displayed (e.g., success,
     * error, warning)
     * @param message the message to be displayed to the user
     * @param redirectUrl the URL to redirect the user to
     * @throws IOException if an I/O error occurs while redirecting the user
     */
    public static void redirectWithMessage(HttpServletRequest request, HttpServletResponse response, RedirectType messageType, String message, String redirectUrl) throws IOException {
        setMessage(request, messageType, message);
        sendRedirect(request, response, redirectUrl);
    }

    /**
     * Sets the message and message status in the session.
     *
     * @param request the HttpServletRequest object
     * @param messageType the RedirectType enum representing the message status
     * @param message the message to be set in the session
     */
    public static void setMessage(HttpServletRequest request, RedirectType messageType, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("messageStatus", messageType.getRedirectType());
        session.setAttribute("message", message);
    }

    /**
     * Sends a redirect response to the client using the specified redirect URL.
     *
     * @param request the HttpServletRequest object that contains the client's
     * request
     * @param response the HttpServletResponse object that contains the
     * servlet's response
     * @param redirectUrl the URL to which the client should be redirected
     * @throws IOException if an I/O error occurs while sending the redirect
     * response
     */
    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws IOException {
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
