package controllers.admins;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class ManageStaffServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ManageStaffServlet.class.getName());
    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
//        HttpSession session = request.getSession();
//        Staffs staff = (Staffs) session.getAttribute("staffDetails");
//        if (staff == null) {
//            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please fill in the staff email!", Constants.ADMIN_SEARCH_STAFF_URL);
//            return;
//        }

        request.getRequestDispatcher("/pages/admins/manageStaff.jsp").forward(request, response);
    }
}
