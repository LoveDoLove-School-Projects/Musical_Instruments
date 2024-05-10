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
import java.util.logging.Logger;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;

@MultipartConfig
public class ModifyProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(ModifyProductServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        request.getRequestDispatcher("/pages/staffs/modifyProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        int productId = Integer.parseInt(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        String color = request.getParameter("color");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String category = request.getParameter("category");
        InputStream pictureStream = request.getPart("image").getInputStream();
        if (pictureStream == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Error uploading picture.", "/pages/staffs/modifyProduct");
            return;
        }
        byte[] pictureBytes = pictureStream.readAllBytes();
        Products product = new Products(productId, productName, price, color, quantity, category, pictureBytes);
        boolean isUpdated = updateProductDetails(request, product);
        if (!isUpdated) {
            showError(request, response);
            return;
        }
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Update Successful!", "/pages/staffs/manageProduct");
    }

    private boolean updateProductDetails(HttpServletRequest request, Products product) {
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
            productFromDB.setImage(product.getImage());
            entityManager.merge(productFromDB);
            userTransaction.commit();
            SecurityLog.addInternalSecurityLog(request, "Product: " + product.getName() + " updated successfully.");
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            SecurityLog.addInternalSecurityLog(request, "Failed to update product: " + product.getProductId() + ".");
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    private void showError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid details, please re-enter it.", "/pages/staffs/modifyProduct");
    }
}
