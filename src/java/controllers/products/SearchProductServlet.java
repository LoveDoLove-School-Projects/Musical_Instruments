package controllers.products;

import common.Constants;
import dao.ProductDao;
import entities.Products;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SearchProductServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SearchProductServlet.class.getName());
    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.SEARCH_PRODUCT_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");
        List<Products> searchResults = (List<Products>) productDao.searchProduct(searchQuery);
        request.setAttribute("searchResults", searchResults);
        request.getRequestDispatcher(Constants.SEARCH_PRODUCT_JSP_URL).forward(request, response);
    }
}
