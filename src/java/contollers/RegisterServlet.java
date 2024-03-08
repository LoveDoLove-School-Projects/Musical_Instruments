package contollers;

import features.RegisterHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import utilities.RedirectUtilities;

public class RegisterServlet extends HttpServlet {

    private final RegisterHandler registerHandler = new RegisterHandler();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int status = registerHandler.handle(request, response);

        if (status == 0) {
            RedirectUtilities.redirectWithMessage(request, response, "Please Enter Valid Details to Register!", "/pages/register.jsp");
            return;
        }

        response.sendRedirect("/pages/login.jsp");
    }
}
