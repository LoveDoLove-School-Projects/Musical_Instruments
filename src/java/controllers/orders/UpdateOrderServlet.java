package controllers.orders;

import common.Constants;
import entities.Carts;
import entities.Products;
import entities.Session;
import entities.Transactions;
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
import utilities.SessionUtilities;

public class UpdateOrderServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final String RECEIPT_URL = "/payments/receipt";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        Transactions transactions = entityManager.createNamedQuery("Transactions.findByTransactionNumber", Transactions.class).setParameter("transactionNumber", transaction_number).getSingleResult();
        if (transactions.getUserId() == userId && tnxStatus.equals(transactions.getTransactionStatus())) {

            //update product quantity
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
                }

                userTransaction.commit();
                String url = RECEIPT_URL + "?transaction_number=" + transaction_number;
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "welcome to receipt page", url);
            } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | NumberFormatException | SecurityException ex) {
                throw new DatabaseException(ex.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
