//package controllers;
//
//import com.paypal.api.payments.Payment;
//import common.Constants;
//import entities.Session;
//import features.SessionChecker;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import services.PaypalServices;
//import utilities.RedirectUtilities;
//
//@WebServlet(name = "PaypalServlet", urlPatterns = {"/payments/paypal/execute"})
//public class PaypalExecuteServlet {
//
//    @PersistenceContext
//    EntityManager entityManager;
//    private final SessionChecker sessionChecker = new SessionChecker();
//    private final PaypalServices paypalServices = new PaypalServices();
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Session session = sessionChecker.getLoginSession(request.getSession(false));
//        if (!session.isResult()) {
//            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
//            return;
//        }
//        Payment payment = paypalServices.executePayment(paymentId, payerId);
//        System.out.println(payment.toJSON());
//        if (payment.getState().equals("approved")) {
//            return "success";
//        }
//        return "redirect:/";
//    }
//}
