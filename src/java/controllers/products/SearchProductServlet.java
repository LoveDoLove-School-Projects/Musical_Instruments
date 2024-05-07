package controllers.products;

import com.google.gson.Gson;
import entities.Constants;
import dao.ProductDao;
import entities.Products;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class SearchProductServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();
    private static final String API_SEARCH_PRODUCT_URL = "/api/products/productsearch";
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.SEARCH_PRODUCT_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");
        List<Products> searchResults = (List<Products>) productDao.searchProduct(searchQuery);
        switch (request.getServletPath()) {
            case API_SEARCH_PRODUCT_URL:
                returnJsonResponse(response, searchResults);
                return;
            default:
                request.setAttribute("searchResults", searchResults);
                request.getRequestDispatcher(Constants.SEARCH_PRODUCT_JSP_URL).forward(request, response);
        }
    }

    private void returnJsonResponse(HttpServletResponse response, List<Products> searchResults) throws IOException {
        response.setContentType("application/json");
        String json = gson.toJson(searchResults);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
