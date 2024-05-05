package controllers;

import entities.Customers;
import static entities.Role.CUSTOMER;
import entities.Session;
import exceptions.DatabaseException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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
import utilities.RedirectUtilities.RedirectType;
import utilities.SecurityLog;
import utilities.SessionUtilities;
import utilities.StringUtilities;

@MultipartConfig
public class BillingDetailsServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final String BILLING_DETAILS_JSP_URL = "/pages/billingDetails.jsp";
    private static final String BILLING_DETAILS_URL = "/pages/billingDetails";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Please login to view this page.", "/");
            return;
        }
        boolean isInit;
        switch (session.getRole()) {
            case CUSTOMER:
                isInit = initBillingDetails(request, session);
                break;
            default:
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid role.", "/");
                return;
        }
        if (!isInit) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error fetching billing details.", "/");
            return;
        }
        request.getRequestDispatcher(BILLING_DETAILS_JSP_URL).forward(request, response);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Please login to view this page.", "/");
            return;
        }
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String country = request.getParameter("country");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zipCode = request.getParameter("zipCode");
        String phoneNumber = request.getParameter("phone_number");
        if (StringUtilities.anyNullOrBlank(firstName, lastName, country, address, city, state, zipCode, phoneNumber)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "All fields are required.", BILLING_DETAILS_URL);
            return;
        }
        boolean isUpdated = false;
        switch (session.getRole()) {
            case CUSTOMER:
                Customers customer = new Customers(session.getUserId(), address, phoneNumber, firstName, lastName, country, city, state, zipCode);
                isUpdated = updateBillingDetails(customer);
                break;
        }
        if (isUpdated) {
            SecurityLog.addSecurityLog(request, "update billing details");
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Billing details updated successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error updating billing details.");
        }
        RedirectUtilities.sendRedirect(request, response, BILLING_DETAILS_URL);
    }

    private boolean updateBillingDetails(Customers customer) {
        try {
            userTransaction.begin();
            Customers existingCustomer = entityManager.find(Customers.class, customer.getUserId());
            if (existingCustomer == null) {
                return false;
            }
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setLastName(customer.getLastName());
            existingCustomer.setCountry(customer.getCountry());
            existingCustomer.setCity(customer.getCity());
            existingCustomer.setState(customer.getState());
            existingCustomer.setZipCode(customer.getZipCode());
            entityManager.merge(existingCustomer);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
