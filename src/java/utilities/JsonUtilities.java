package utilities;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtilities {

    public static void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        sendResponse(response, "error", message);
    }

    public static void sendSuccessResponse(HttpServletResponse response, String message) throws IOException {
        sendResponse(response, "success", message);
    }

    public static void sendResponse(HttpServletResponse response, String status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"status\":\"" + status + "\",\"message\":\"" + message + "\"}");
        }
    }
}
