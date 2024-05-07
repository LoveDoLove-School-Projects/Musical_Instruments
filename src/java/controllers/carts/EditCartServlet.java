package controllers.carts;

import entities.Constants;
import entities.Carts;
import entities.Products;
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
import java.util.List;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;

public class EditCartServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final String EDITCART_JSP_URL = "/pages/carts/editCart.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        String cardId = request.getParameter("cart_id");
        List<Carts> cartsList = entityManager.createNamedQuery("Carts.findByCartId").setParameter("cartId", Integer.valueOf(cardId)).getResultList();
        if (cartsList == null || cartsList.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Cart Not Found!", Constants.CART_URL);
            return;
        }
        Carts carts = cartsList.get(0);
        //retrive the product stock
        List<Products> productsList = entityManager.createNamedQuery("Products.findByProductId").setParameter("productId", carts.getProductId()).getResultList();
        Products products = productsList.get(0);
        request.setAttribute("editCartDetails", carts);
        request.setAttribute("productDetails", products);
        request.getRequestDispatcher(EDITCART_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        int cardId = Integer.parseInt(request.getParameter("cartId"));
        int productQuantity = Integer.parseInt(request.getParameter("productQuantity"));
        try {
            userTransaction.begin();
            Carts carts = entityManager.find(Carts.class, cardId);
            carts.setProductQuantity(productQuantity);
            carts.setProductTotalprice(productQuantity * carts.getProductPrice());
            entityManager.merge(carts);
            userTransaction.commit();
            SecurityLog.addSecurityLog(request, "Product " + carts.getProductName() + " has been updated in cart successfully.");
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Your cart has been update !", Constants.CART_URL);
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addSecurityLog(request, "Product " + cardId + " has been update in cart failed.");
            throw new DatabaseException(ex.getMessage());
        }
    }
}
