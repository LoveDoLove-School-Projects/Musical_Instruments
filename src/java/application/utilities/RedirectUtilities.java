package application.utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class RedirectUtilities {

    public static void redirectWithMessage(HttpServletRequest request, HttpServletResponse response, String message, String redirectUrl) throws IOException {
        setInfoMessage(request, message);
        sendRedirect(request, response, redirectUrl);
    }

    public static void redirectWithSuccess(HttpServletRequest request, HttpServletResponse response, String successMessage, String redirectUrl) throws IOException {
        setSuccessMessage(request, successMessage);
        sendRedirect(request, response, redirectUrl);
    }

    public static void redirectWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage, String rediectUrl) throws IOException {
        setErrorMessage(request, errorMessage);
        sendRedirect(request, response, rediectUrl);
    }

    public static void setInfoMessage(HttpServletRequest request, String message) {
        setMessage(request, "info", message);
    }

    public static void setWarningMessage(HttpServletRequest request, String message) {
        setMessage(request, "warning", message);
    }

    public static void setPrimaryMessage(HttpServletRequest request, String message) {
        setMessage(request, "primary", message);
    }

    public static void setSuccessMessage(HttpServletRequest request, String message) {
        setMessage(request, "success", message);
    }

    public static void setErrorMessage(HttpServletRequest request, String message) {
        setMessage(request, "danger", message);
    }

    private static void setMessage(HttpServletRequest request, String status, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("messageStatus", status);
        session.setAttribute("message", message);
    }

    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws IOException {
        try {
            response.sendRedirect(request.getContextPath() + redirectUrl);
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }
}
