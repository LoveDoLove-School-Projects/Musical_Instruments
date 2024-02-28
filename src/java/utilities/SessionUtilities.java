package utilities;

import jakarta.servlet.http.HttpSession;

public class SessionUtilities {

    public static void setSessionAttribute(HttpSession session, String key, Object value) {
        session.setAttribute(key, value);
    }

    public static void getSessionAttribute(HttpSession session, String key) {
        session.getAttribute(key);
    }

    public static void removeSessionAttribute(HttpSession session, String key) {
        session.removeAttribute(key);
    }
}
