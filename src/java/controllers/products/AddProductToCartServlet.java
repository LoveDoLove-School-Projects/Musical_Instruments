package controllers.products;

import domain.common.Constants;
import domain.models.Session;
import entities.Carts;
import entities.Products;
import exceptions.DatabaseException;
import features.SessionHandler;
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
import utilities.RedirectUtilities;

public class AddProductToCartServlet extends HttpServlet {

    private static final SessionHandler sessionHandler = new SessionHandler();

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = sessionHandler.getLoginSession(request.getSession());
        if (!session.isResult()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        String productId = request.getParameter("productId");
        String productQuantity = request.getParameter("productQuantity");
        Products product = entityManager.find(Products.class, Integer.valueOf(productId));
        if (product == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.WARNING, "Product not found!", Constants.PRODUCT_URL);
            return;
        }
        try {
            userTransaction.begin();
            Carts cart = new Carts();
            cart.setCustomerId(session.getUserId());
            cart.setProductId(product.getProductId());
            cart.setProductQuantity(Integer.parseInt(productQuantity));
            cart.setProductName(product.getName());
            cart.setProductColor(product.getColor());
            cart.setProductPrice(product.getPrice());
            cart.setProductImagePath(product.getImagePath());
            entityManager.persist(cart);
            userTransaction.commit();
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Your cart has been added!", Constants.PRODUCT_URL);

        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | NumberFormatException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
