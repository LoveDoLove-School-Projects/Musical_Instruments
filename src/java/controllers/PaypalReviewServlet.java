
import common.Constants;
import entities.PaypalPayment;
import entities.Session;
import features.SessionChecker;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;
import services.PaypalServices;
import utilities.RedirectUtilities;

@WebServlet(name = "PaypalReviewServlet", urlPatterns = {"/payments/paypal/review"})
public class PaypalReviewServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(PaypalReviewServlet.class.getName());
    private static final String REVIEW_JSP_URL = "/payments/review.jsp";
    private final PaypalServices paypalServices = new PaypalServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionChecker.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        String paymentId = request.getParameter("paymentId");
        if (paymentId == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment ID is required.", Constants.CART_URL);
            return;
        }
        PaypalPayment paypalPayment = paypalServices.getPaymentDetails(paymentId);
        PaypalPayment.PayerInfo payerInfo = paypalPayment.getPayer().getPayer_info();
        PaypalPayment.Transaction transaction = paypalPayment.getTransactions().get(0);
        PaypalPayment.ShippingAddress shippingAddress = transaction.getItem_list().getShipping_address();
        request.setAttribute("payer", payerInfo);
        request.setAttribute("transaction", transaction);
        request.setAttribute("shippingAddress", shippingAddress);
        request.getRequestDispatcher(REVIEW_JSP_URL).forward(request, response);
    }
}
