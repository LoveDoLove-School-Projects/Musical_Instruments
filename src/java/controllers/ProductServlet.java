package controllers;

import domain.common.Constants;
import domain.models.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import services.ProductServices;
import utilities.FileUtilities;

public class ProductServlet extends HttpServlet {

    private static final String IMAGE_DEFAULT_PATH = "assets/database/productImage/";
    private static final ProductServices productServices = new ProductServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case Constants.PRODUCT_URL:
                viewMainPageProduct(request, response);
                return;
            case Constants.VIEW_PRODUCT_URL:
                viewProductWithID(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private void viewMainPageProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the image path using ProductServices
        StringBuilder builder = new StringBuilder();
        List<Product> products = productServices.getAllProducts();
        if (!products.isEmpty()) {
            for (Product product : products) {
                byte[] pictureBytes = FileUtilities.readDirectoryContent(IMAGE_DEFAULT_PATH + product.getImage_path());
                String pictureBase64 = Base64.getEncoder().encodeToString(pictureBytes);
                String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
                builder.append("<div class=\"col-6 col-xl-3 col-xxl-3\">\n")
                        .append("<a href=\"#\" class=\"MusicInstruments row1-MusicInstruments1\">\n")
                        .append("<div class=\"p-2\">\n")
                        .append("<img src=\"").append(imageSrc).append("\" class=\"img-fluid w-100\">\n")
                        .append("</div>\n")
                        .append("<p class=\"text-center\"><strong>")
                        .append("</br>").append(product.getName())
                        .append("</br>").append(product.getColor())
                        .append("</br>").append(product.getQuantity())
                        .append("</strong></p> </a> </div>");
            }
            request.setAttribute("productDetails", builder.toString());
        }
        request.getRequestDispatcher(Constants.PRODUCT_JSP_URL).forward(request, response);
    }

    private void viewProductWithID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("product_id");
        StringBuilder builder = new StringBuilder();
        Product product = productServices.getProductWithID(productId);
        if (product != null) {
            byte[] pictureBytes = FileUtilities.readDirectoryContent(IMAGE_DEFAULT_PATH + product.getImage_path());
            String pictureBase64 = Base64.getEncoder().encodeToString(pictureBytes);
            String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
            builder.append("<div class=\"col-6 col-xl-3 col-xxl-3\">\n")
                    .append("<a href=\"#\" class=\"MusicInstruments row1-MusicInstruments1\">\n")
                    .append("<div class=\"p-2\">\n")
                    .append("<img src=\"").append(imageSrc).append("\" class=\"img-fluid w-100\">\n")
                    .append("</div>\n")
                    .append("<p class=\"text-center\"><strong>")
                    .append("</br>").append(product.getName())
                    .append("</br>").append(product.getColor())
                    .append("</br>").append(product.getQuantity())
                    .append("</strong></p> </a> </div>");
            request.setAttribute("productDetails", builder.toString());
        }
        request.getRequestDispatcher(Constants.VIEW_PRODUCT_JSP_URL).forward(request, response);
    }

    private void productNotFound() {

    }

}
