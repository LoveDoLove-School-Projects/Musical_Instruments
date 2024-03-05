package contollers;

import features.LoginHandler;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilities.SessionUtilities;

public class LoginServlet extends HttpServlet {

    private final LoginHandler loginHandler = new LoginHandler();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int customerId = loginHandler.handle(request, response);

        if (customerId == 0) {
            HttpSession session = request.getSession();
            session.setAttribute("message", "Incorrect Email or Password!");
            response.sendRedirect("login.jsp");
            return;
        }

        HttpSession session = request.getSession();
        SessionUtilities.setSessionAttribute(session, "login_id", customerId);

        response.sendRedirect("index.jsp");
    }
}
