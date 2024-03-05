package contollers;

import features.RegisterHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    private final RegisterHandler registerHandler = new RegisterHandler();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int status = registerHandler.handle(request, response);

        if (status == 0) {
            HttpSession session = request.getSession();
            session.setAttribute("message", "Please Enter Valid Details to Register!");
            response.sendRedirect("register.jsp");
            return;
        }

        response.sendRedirect("login.jsp");
    }
}
