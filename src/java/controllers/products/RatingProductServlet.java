package controllers.products;

import entities.Constants;
import entities.Ratings;
import entities.Session;
import exceptions.DatabaseException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.Date;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;

public class RatingProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    public static final String VIEW_PRODUCT_URL = "/pages/products/viewProduct";
    public static final String VIEW_PRODUCT_JSP_URL = "/pages/products/viewProduct.jsp";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        int productId = Integer.parseInt(request.getParameter("product_id"));
        int productRate = Integer.parseInt(request.getParameter("rating"));
        String productComment = request.getParameter("comment");
        try {
            userTransaction.begin();
            Ratings ratings = new Ratings();
            ratings.setProductId(productId);
            ratings.setRatingScore(productRate);
            ratings.setTimestamp(new Date());
            ratings.setComment(productComment);
            ratings.setUserId(session.getUserId());
            entityManager.persist(ratings);
            userTransaction.commit();
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "submit succesfull!", VIEW_PRODUCT_URL + "?product_id=" + productId);
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addSecurityLog(request, "Product " + productId + " has been added to cart failed.");
            throw new DatabaseException(ex.getMessage());
        }
    }
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int productId = Integer.parseInt(request.getParameter("product_id"));
//        Session session = SessionUtilities.getLoginSession(request.getSession());
//        if (session == null) {
//            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
//            return;
//        }
//        if (SessionUtilities.checkIsStaffOrAdmin(request)) {
//            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Admin or staff are not allowed to view this page because it only for customer.", "/");
//            return;
//        }
//
//        List<Ratings> ratings = entityManager.createNamedQuery("Ratings.findByProductId").setParameter("productId", productId).getResultList();
//
//        request.setAttribute("ratingList", ratings);
//        request.getRequestDispatcher(VIEW_PRODUCT_JSP_URL).forward(request, response);
//
//    }
}
