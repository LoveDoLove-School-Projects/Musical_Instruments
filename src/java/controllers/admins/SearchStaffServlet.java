package controllers.admins;

import entities.Constants;
import entities.Staffs;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import utilities.RedirectUtilities;

public class SearchStaffServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Staffs> staffList = entityManager.createNamedQuery("Staffs.findAll", Staffs.class).getResultList();
        request.setAttribute("staffCount", staffList.size());
        request.setAttribute("staffList", staffList);
        request.getRequestDispatcher("/pages/admins/searchStaff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String searchQuery = request.getParameter("searchQuery");
        if (searchQuery.isBlank() || searchQuery.trim().isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please fill in the staff email!", Constants.ADMIN_SEARCH_STAFF_URL);
            return;
        }
        List<Staffs> staffList = entityManager.createNamedQuery("Staffs.findByEmail", Staffs.class).setParameter("email", searchQuery).getResultList();
        if (staffList == null || staffList.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Email address does not exist!", Constants.ADMIN_SEARCH_STAFF_URL);
            return;
        }
        Staffs staff = staffList.get(0);
        HttpSession session = request.getSession();
        session.setAttribute("staffDetails", staff);
        RedirectUtilities.sendRedirect(request, response, "/pages/admins/manageStaff");
    }
}
