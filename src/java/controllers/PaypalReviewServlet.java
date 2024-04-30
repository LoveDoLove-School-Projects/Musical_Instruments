
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.Constants;
import entities.Session;
import features.SessionChecker;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import utilities.RedirectUtilities;

@WebServlet(name = "PaypalReviewServlet", urlPatterns = {"/payments/paypal/review"})
public class PaypalReviewServlet extends HttpServlet {

    private static final String REVIEW_JSP_URL = "/payments/review.jsp";
    private final SessionChecker sessionChecker = new SessionChecker();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionChecker.getLoginSession(request.getSession());
        if (!session.isResult()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        HttpSession httpSession = request.getSession();
        String paypalResponse = (String) httpSession.getAttribute("paypalResponse");
        JsonObject jsonObject = new Gson().fromJson(paypalResponse, JsonObject.class);
        JsonObject transaction = getTransaction(jsonObject);
        JsonObject payer = getPayer(jsonObject);
        JsonObject shippingAddress = getShippingAddress(transaction);
        request.setAttribute("description", transaction.get("description").getAsString());
        request.setAttribute("amount", transaction.getAsJsonObject("amount"));
        request.setAttribute("firstName", payer.get("first_name").getAsString());
        request.setAttribute("lastName", payer.get("last_name").getAsString());
        request.setAttribute("email", payer.get("email").getAsString());
        if (shippingAddress != null) {
            request.setAttribute("recipientName", shippingAddress.get("recipient_name").getAsString());
            request.setAttribute("line1", shippingAddress.get("line1").getAsString());
            request.setAttribute("city", shippingAddress.get("city").getAsString());
            request.setAttribute("state", shippingAddress.get("state").getAsString());
            request.setAttribute("countryCode", shippingAddress.get("country_code").getAsString());
            request.setAttribute("postalCode", shippingAddress.get("postal_code").getAsString());
        }
        request.getRequestDispatcher(REVIEW_JSP_URL).forward(request, response);
    }

    private JsonObject getTransaction(JsonObject jsonObject) {
        JsonArray transactions = jsonObject.getAsJsonArray("transactions");
        if (transactions != null && transactions.size() > 0) {
            return transactions.get(0).getAsJsonObject();
        }
        return null;
    }

    private JsonObject getPayer(JsonObject jsonObject) {
        JsonObject payer = jsonObject.getAsJsonObject("payer");
        if (payer != null) {
            return payer.getAsJsonObject("payer_info");
        }
        return null;
    }

    private JsonObject getShippingAddress(JsonObject transaction) {
        if (transaction.has("item_list")) {
            JsonObject itemList = transaction.getAsJsonObject("item_list");
            if (itemList.has("items") && itemList.getAsJsonArray("items").size() > 0) {
                JsonObject firstItem = itemList.getAsJsonArray("items").get(0).getAsJsonObject();
                if (firstItem.has("shipping_address")) {
                    return firstItem.getAsJsonObject("shipping_address");
                }
            }
        }
        return null;
    }
}
