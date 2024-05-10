package controllers;

import entities.Carts;
import entities.Constants;
import entities.Orders;
import entities.Products;
import entities.Session;
import entities.TransactionStatus;
import entities.Transactions;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class UpdateOrderServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(UpdateOrderServlet.class.getName());
    private static final String RECEIPT_URL = "/payments/receipt";

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
        int userId = session.getUserId();
        String tnxStatus = request.getParameter("txnStatus");
        String transaction_number = request.getParameter("transaction_number");
        String orderNumber = request.getParameter("order_number");
        String url = RECEIPT_URL + "?transaction_number=" + transaction_number;
        if (tnxStatus == null || transaction_number == null || orderNumber == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid transaction details", "/");
            return;
        }
        if (!tnxStatus.equals(TransactionStatus.APPROVED)) {
            RedirectUtilities.sendRedirect(request, response, url);
            return;
        }
        Transactions transactions = entityManager.createNamedQuery("Transactions.findByTransactionNumberAndUserId", Transactions.class).setParameter("transactionNumber", transaction_number).setParameter("userId", userId).getSingleResult();
        if (transactions == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "No transaction record", "/");
            return;
        }
        List<Carts> carts = entityManager.createNamedQuery("Carts.findByCustomerId").setParameter("customerId", userId).getResultList();
        try {
            userTransaction.begin();
            for (Carts userCart : carts) {
                Products products = entityManager.find(Products.class, userCart.getProductId());
                int productQuantity = products.getQuantity() - userCart.getProductQuantity();
                products.setQuantity(productQuantity);
                entityManager.merge(products);
                Carts managedUserCart = entityManager.getReference(Carts.class, userCart.getCartId());
                entityManager.remove(managedUserCart);
                Orders orders = new Orders();
                orders.setUserId(userId);
                orders.setOrderNumber(orderNumber);
                orders.setProductColor(products.getColor());
                orders.setProductId(products.getProductId());
                orders.setProductImage(products.getImage());
                orders.setProductName(products.getName());
                orders.setProductPrice(products.getPrice());
                orders.setProductQuantity(userCart.getProductQuantity());
                BigDecimal totalPrice = BigDecimal.valueOf(userCart.getProductQuantity() * products.getPrice());
                orders.setProductTotalprice(totalPrice);
                orders.setOrderDate(transactions.getDateUpdatedGmt());
                entityManager.persist(orders);
            }
            userTransaction.commit();
            RedirectUtilities.sendRedirect(request, response, url);
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | NumberFormatException | SecurityException ex) {
            LOG.severe(ex.getMessage());
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to update order! Please contact support to resolve the issue.", "/");
        }
    }
}
