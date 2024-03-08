package contollers;

import features.LoginHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class LoginServlet extends HttpServlet {

    private final LoginHandler loginHandler = new LoginHandler();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int customerId = loginHandler.handle(request, response);

        if (customerId == 0) {
            RedirectUtilities.redirectWithMessage(request, response, "Incorrect Email or Password!", "/pages/login.jsp");
            return;
        }

        SessionUtilities.setSessionAttribute(request.getSession(), "login_id", customerId);
        response.sendRedirect("/index.jsp");
    }
}
