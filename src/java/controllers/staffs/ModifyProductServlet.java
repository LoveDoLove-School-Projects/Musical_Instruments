package controllers.staffs;

import entities.Products;
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
import utilities.RedirectUtilities;

public class ModifyProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        String color = request.getParameter("color");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String category = request.getParameter("category");
        String imagePath = request.getParameter("imagePath");
        Products product = new Products(productId, productName, price, color, quantity, category, imagePath);
        boolean isUpdated = updateProductDetails(product);
        if (!isUpdated) {
            showError(request, response);
            return;
        }
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Update Successful!", "/pages/staffs/manageProduct");
    }

    private boolean updateProductDetails(Products product) {
        try {
            userTransaction.begin();
            Products productFromDB = entityManager.find(Products.class, product.getProductId());
            if (productFromDB == null) {
                return false;
            }
            productFromDB.setName(product.getName());
            productFromDB.setPrice(product.getPrice());
            productFromDB.setColor(product.getColor());
            productFromDB.setQuantity(product.getQuantity());
            productFromDB.setCategory(product.getCategory());
            productFromDB.setImagePath(product.getImagePath());
            entityManager.merge(productFromDB);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException e) {
            return false;
        }
    }

    private void showError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid details, please re-enter it.", "/pages/staffs/modifyProduct.jsp");
    }
}
