package contollers;

import features.LogoutHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    private final LogoutHandler logoutHandler = new LogoutHandler();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logoutHandler.handle(request, response);
        response.sendRedirect("/index.jsp");
    }
}
