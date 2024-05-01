package controllers.staffs;

import features.SessionChecker;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import utilities.RedirectUtilities;

public class StaffServlet extends HttpServlet {

    private static final String PANEL_JSP = "/pages/staffs/panel.jsp";
    private final SessionChecker sessionChecker = new SessionChecker();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionChecker.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        request.getRequestDispatcher(PANEL_JSP).forward(request, response);
    }
}
