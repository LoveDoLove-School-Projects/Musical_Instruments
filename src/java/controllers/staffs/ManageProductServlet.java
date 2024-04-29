package controllers.staffs;

import entities.Products;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class ManageProductServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ManageProductServlet.class.getName());

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Products product = (Products) session.getAttribute("productDetails");

        product = entityManager.find(Products.class, product.getProductId());

        session.setAttribute("productDetails", product);
        request.getRequestDispatcher("/pages/staffs/manageProduct.jsp").forward(request, response);
    }

}
