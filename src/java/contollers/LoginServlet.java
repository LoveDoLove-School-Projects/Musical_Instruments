package contollers;

import features.LoginHandler;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Message;
import utilities.MessageUtilities;
import utilities.SessionUtilities;

public class LoginServlet extends HttpServlet {

    private HttpSession session;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int customer_id = new LoginHandler().handle(request, response);

        if (customer_id == 0) {
            Message message = new Message();
            message.setRequest(request);
            message.setResponse(response);
            message.setMessage("Incorrect Email or Password!");
            message.setJspPage("login.jsp");
            MessageUtilities.sendMessageToJsp(message);
            return;
        }

        session = request.getSession();
        SessionUtilities.setSessionAttribute(session, "userId", customer_id);

        response.sendRedirect("index.jsp");
    }
}
