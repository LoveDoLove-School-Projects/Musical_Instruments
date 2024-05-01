package controllers.admins;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class ManageStaffServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ManageStaffServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/pages/admins/manageStaff.jsp").forward(request, response);
    }
}
