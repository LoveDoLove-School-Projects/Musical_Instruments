package controllers.staffs;

import entities.Products;
import entities.Role;
import entities.Session;
import exceptions.DatabaseException;
import features.SessionChecker;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.io.InputStream;
import utilities.RedirectUtilities;

@MultipartConfig
public class AddProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private final SessionChecker sessionChecker = new SessionChecker();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        boolean isAdmin = sessionChecker.getIsAdminOrNot(request);
        Session session = sessionChecker.getLoginSession(httpSession);
        boolean isLoggedIn = session.isResult();
        if (!isAdmin && (!isLoggedIn || session.getRole() != Role.STAFF)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        request.getRequestDispatcher("/pages/staffs/addProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String productName = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        String color = request.getParameter("color");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String category = request.getParameter("category");
        InputStream pictureStream = request.getPart("image").getInputStream();
        if (pictureStream == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Error uploading picture.", "/pages/staffs/AddProduct.jsp");
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
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            throw new DatabaseException(ex.getMessage());
        }
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Product Added successful!", "/pages/staffs/searchProduct.jsp");
    }
}
