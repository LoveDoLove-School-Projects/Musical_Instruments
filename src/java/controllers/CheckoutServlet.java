package controllers;

import entities.Carts;
import entities.Constants;
import entities.Customers;
import entities.OrderDetails;
import entities.Products;
import entities.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import services.TransactionServices;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.SessionUtilities;

public class CheckoutServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final String CHECKOUT_JSP_URL = "/payments/checkout.jsp";
    private final TransactionServices transactionServices = new TransactionServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        List<Carts> cartList = entityManager.createNamedQuery("Carts.findByCustomerId", Carts.class).setParameter("customerId", session.getUserId()).getResultList();
        if (cartList == null || cartList.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Cart is empty.", Constants.CART_URL);
            return;
        }
        Map<Integer, Integer> productQuantities = new HashMap<>();
        for (Carts cartItem : cartList) {
            int productId = cartItem.getProductId();
            int quantity = cartItem.getProductQuantity();
            productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + quantity);
        }
        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            int productId = entry.getKey();
            int cartQuantity = entry.getValue();

            Products product = entityManager.createNamedQuery("Products.findByProductId", Products.class)
                    .setParameter("productId", productId)
                    .getSingleResult();
            if (cartQuantity > product.getQuantity()) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.WARNING, "Product name: " + product.getName() + " quantity in your cart exceeds the available stock", Constants.CART_URL);
                return;
            }
        }
        OrderDetails orderDetails = transactionServices.getOrderDetails(cartList);
        request.setAttribute("cartList", cartList);
        request.setAttribute("subtotal", orderDetails.getSubtotal());
        request.setAttribute("shipping", orderDetails.getShipping());
        request.setAttribute("tax", orderDetails.getTax());
        request.setAttribute("total", orderDetails.getTotal());
        initBillingDetails(request, session);
        request.getRequestDispatcher(CHECKOUT_JSP_URL).forward(request, response);
    }

    private boolean initBillingDetails(HttpServletRequest request, Session session) {
        Customers existingCustomer = entityManager.find(Customers.class, session.getUserId());
        if (existingCustomer == null) {
            return false;
        }
        request.setAttribute("firstName", existingCustomer.getFirstName());
        request.setAttribute("lastName", existingCustomer.getLastName());
        request.setAttribute("country", existingCustomer.getCountry());
        request.setAttribute("address", existingCustomer.getAddress());
        request.setAttribute("city", existingCustomer.getCity());
        request.setAttribute("state", existingCustomer.getState());
        request.setAttribute("zipCode", existingCustomer.getZipCode());
        request.setAttribute("phone_number", existingCustomer.getPhoneNumber());
        request.setAttribute("email", existingCustomer.getEmail());
        return true;
    }
}
