package contollers;

import features.RegisterHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import models.Message;
import utilities.MessageUtilities;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int status = new RegisterHandler().handle(request, response);

        if (status != 1) {
            Message message = new Message();
            message.setRequest(request);
            message.setResponse(response);
            message.setMessage("Please Enter Valid Details to Register!");
            message.setJspPage("register.jsp");
            MessageUtilities.sendMessageToJsp(message);
            return;
        }

        response.sendRedirect("login.jsp");
    }
}
