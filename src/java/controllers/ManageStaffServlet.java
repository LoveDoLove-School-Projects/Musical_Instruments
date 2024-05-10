package controllers;

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

public class ManageStaffServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("text/html;charset=UTF-8");
        Staffs staff = (Staffs) session.getAttribute("staffDetails");
        List<Staffs> staffList = entityManager.createNamedQuery("Staffs.findByEmail", Staffs.class).setParameter("email", staff.getEmail()).getResultList();
        if (staffList == null || staffList.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Email address does not exist!", Constants.ADMIN_SEARCH_STAFF_URL);
            return;
        }
        staff = staffList.get(0);
        session.removeAttribute("staffDetails");
        request.getSession().setAttribute("staffDetails", staff);
        request.getRequestDispatcher("/pages/admins/manageStaff.jsp").forward(request, response);
    }
}
