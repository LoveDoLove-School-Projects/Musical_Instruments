package ms.paymentgateway.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import ms.paymentgateway.domain.common.Constants;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/payments/transaction", "/payments/processTransaction"})
public class TransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER,
        // "Invalid request", Constants.MAIN_URL);
        request.getRequestDispatcher(Constants.TRANSACTION_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case Constants.TRANSACTION_URL:
                processTransaction(request, response);
                break;
            case Constants.PAY_TRANSACTION_URL:
                break;
        }
    }

    private void processTransaction(HttpServletRequest request, HttpServletResponse response) {
        String orderId = request.getParameter("orderId");
        String amount = request.getParameter("amount");
    }
}
