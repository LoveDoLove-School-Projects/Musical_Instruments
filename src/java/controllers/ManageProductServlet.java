package controllers;

import entities.Products;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class ManageProductServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        HttpSession session = request.getSession();
        response.setContentType("text/html;charset=UTF-8");
        Products product = (Products) session.getAttribute("productDetails");
        product = entityManager.find(Products.class, product.getProductId());
        session.setAttribute("productDetails", product);
        request.getRequestDispatcher("/pages/staffs/manageProduct.jsp").forward(request, response);
    }
}
