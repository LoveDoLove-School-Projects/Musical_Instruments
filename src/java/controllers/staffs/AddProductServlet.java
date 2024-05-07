package controllers.staffs;

import entities.Products;
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
import java.io.InputStream;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;

@MultipartConfig
public class AddProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final String ADD_PRODUCT_JSP_URL = "/pages/staffs/addProduct.jsp";
    private static final String ADD_PRODUCT_URL = "/pages/staffs/addProduct";
    private static final String STAFF_SEARCH_PRODUCT_URL = "/pages/staffs/staffSearchProduct";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        request.getRequestDispatcher(ADD_PRODUCT_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        response.setContentType("text/html;charset=UTF-8");
        String productName = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        String color = request.getParameter("color");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String category = request.getParameter("category");
        InputStream pictureStream = request.getPart("image").getInputStream();
        if (pictureStream == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Error uploading picture.", ADD_PRODUCT_URL);
            return;
        }
        byte[] pictureBytes = pictureStream.readAllBytes();
        try {
            userTransaction.begin();
            Products product = new Products();
            product.setName(productName);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setCategory(category);
            product.setColor(color);
            product.setImage(pictureBytes);
            entityManager.persist(product);
            userTransaction.commit();
            SecurityLog.addInternalSecurityLog(request, " added new product " + product.toString());
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Product Added successful!", STAFF_SEARCH_PRODUCT_URL);
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
