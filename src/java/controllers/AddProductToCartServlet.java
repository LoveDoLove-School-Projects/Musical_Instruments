package controllers;

import entities.Carts;
import entities.Constants;
import entities.Products;
import entities.Session;
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
import java.util.logging.Logger;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;

public class AddProductToCartServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(AddProductToCartServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        String productId = request.getParameter("productId");
        int productQuantity = Integer.parseInt(request.getParameter("productQuantity"));
        Products product = entityManager.find(Products.class, Integer.valueOf(productId));
        if (product == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.WARNING, "Product not found!", Constants.PRODUCT_URL);
            return;
        }
        try {
            userTransaction.begin();
            Carts carts = new Carts();
            carts.setCustomerId(session.getUserId());
            carts.setProductId(product.getProductId());
            carts.setProductQuantity(productQuantity);
            carts.setProductName(product.getName());
            carts.setProductColor(product.getColor());
            carts.setProductPrice(product.getPrice());
            carts.setProductImage(product.getImage());
            carts.setProductTotalprice(productQuantity * product.getPrice());
            entityManager.persist(carts);
            userTransaction.commit();
            SecurityLog.addSecurityLog(request, "Product " + product.getName() + " has been added to cart successfully.");
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Add to cart successfull !", Constants.CART_URL);
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addSecurityLog(request, "Product " + productId + " has been added to cart failed.");
            LOG.severe(ex.getMessage());
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Add to cart failed!", Constants.PRODUCT_URL);
        }
    }
}
