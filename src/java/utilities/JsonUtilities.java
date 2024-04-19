package utilities;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtilities {

    /**
     * Sends an error response to the client.
     *
     * @param response the HttpServletResponse object representing the response
     * to be sent
     * @param message the error message to be included in the response
     * @throws IOException if an I/O error occurs while sending the response
     */
    public static void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        sendResponse(response, "error", message);
    }

    /**
     * Sends a success response to the HttpServletResponse object with the
     * specified message.
     *
     * @param response The HttpServletResponse object to send the response to.
     * @param message The message to include in the response.
     * @throws IOException if an I/O error occurs while sending the response.
     */
    public static void sendSuccessResponse(HttpServletResponse response, String message) throws IOException {
        sendResponse(response, "success", message);
    }

    /**
     * Sends a JSON response to the HttpServletResponse object.
     *
     * @param response The HttpServletResponse object to send the response to.
     * @param status The status of the response.
     * @param message The message of the response.
     * @throws IOException If an I/O error occurs while sending the response.
     */
    public static void sendResponse(HttpServletResponse response, String status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"status\":\"" + status + "\",\"message\":\"" + message + "\"}");
        }
    }
}
