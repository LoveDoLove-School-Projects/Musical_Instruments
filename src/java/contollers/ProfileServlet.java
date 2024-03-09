package contollers;

import common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleProfile(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleProfile(request, response);
    }

    private void handleProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if ("POST".equalsIgnoreCase(request.getMethod())) {
            }
            request.getRequestDispatcher(Constants.PROFILE_JSP_URL).forward(request, response);
        } catch (ServletException | IOException ex) {
            throw new ServletException(ex);
        }
    }
}
