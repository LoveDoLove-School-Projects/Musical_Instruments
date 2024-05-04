package controllers.orders;

import common.Constants;
import entities.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class OrderHistoryServlet extends HttpServlet {

//       @PersistenceContext
//    EntityManager entityManager;
       
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        if (SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Admin or staff are not allowed to view this page because it only for customer.", "/");
            return;
        }
//      List<Orders> orders = entityManager.createNamedQuery("Orders.findByOrderNumber").setParameter("orderNumber", session.getUserId()).getResultList();
//        if (orders == null || orders.isEmpty()) {
//            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Your order history is empty !", Constants.PRODUCT_URL);
//        } else {
//            request.setAttribute("orderHistoryDetails", orders);
//            request.getRequestDispatcher(Constants.ORDERHISTORY_JSP_URL).forward(request, response);
//        }
           request.getRequestDispatcher(Constants.ORDERHISTORY_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
