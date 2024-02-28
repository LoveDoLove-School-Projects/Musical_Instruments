package utilities;

import jakarta.servlet.ServletException;
import java.io.IOException;
import models.Message;

public class MessageUtilities {

    public static void sendMessageToJsp(Message message) {
        try {
            message.getRequest().setAttribute("message", message.getMessage());
            message.getRequest().getRequestDispatcher(message.getJspPage()).forward(message.getRequest(), message.getResponse());
        } catch (ServletException | IOException ex) {
            LoggerUtilities.logSevere(ex);
        }
    }
}
