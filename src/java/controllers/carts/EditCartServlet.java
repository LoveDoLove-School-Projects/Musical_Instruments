package controllers.carts;

import common.Constants;
import entities.Carts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import utilities.RedirectUtilities;

public class EditCartServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int cardId = Integer.parseInt(request.getParameter("cartId"));
        List<Carts> carts = entityManager.createNamedQuery("Carts.findByCartId").setParameter("cartId", cardId).getResultList();
        if (carts == null || carts.isEmpty()) {
              RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Cart Not Found!", Constants.CART_URL);
            return;
        }
        request.setAttribute("editCartDetails", carts);
        request.getRequestDispatcher(Constants.EDITCART_JSP_URL).forward(request, response);
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
