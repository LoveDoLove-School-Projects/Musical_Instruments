package utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtilities {

    public static void setSessionAttribute(HttpSession session, String key, Object value) {
        if (session != null) {
            session.setAttribute(key, value);
        }
    }

    public static Object getSessionAttribute(HttpSession session, String key) {
        if (session == null) {
            return null;
        }
        return session.getAttribute(key);
    }

    public static Integer getSessionAttributeInt(HttpSession session, String key) {
        if (session == null) {
            return null;
        }
        Object attribute = session.getAttribute(key);
        return (attribute instanceof Integer) ? (Integer) attribute : null;
    }

    public static String getSessionAttributeString(HttpSession session, String key) {
        if (session == null) {
            return null;
        }
        Object attribute = session.getAttribute(key);
        return (attribute instanceof String) ? (String) attribute : null;
    }

    public static void removeSessionAttribute(HttpSession session, String key) {
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    public static void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
