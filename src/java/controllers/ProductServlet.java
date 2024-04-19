package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import services.ProductServices;
import utilities.FileUtilities;

public class ProductServlet extends HttpServlet {

    private static final String IMAGE_DEFAULT_PATH = "assets/database/productImage/";
    private static final ProductServices productServices = new ProductServices();
    private static final Map<Common.PRODUCT_CATEGORIES, String> PRODUCT_DETAILS;

    static {
        PRODUCT_DETAILS = new EnumMap<>(Common.PRODUCT_CATEGORIES.class);
        PRODUCT_DETAILS.put(Common.PRODUCT_CATEGORIES.PIANO, "pianoProductDetails");
        PRODUCT_DETAILS.put(Common.PRODUCT_CATEGORIES.ELECTRICGUITARS, "guitarProductDetails");
    }

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
        addCart(request,response);
    }

    private void viewMainPageProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the image path using ProductServices
        for (Common.PRODUCT_CATEGORIES product : PRODUCT_DETAILS.keySet()) {
            List<Product> pianoProducts = productServices.getAllProducts(product);
            String attribute = filterProductCategory(pianoProducts);
            request.setAttribute(PRODUCT_DETAILS.get(product), attribute);
        }
        request.getRequestDispatcher(Constants.PRODUCT_JSP_URL).forward(request, response);
    }

    private String filterProductCategory(List<Product> products) {
        StringBuilder builder = new StringBuilder();
        if (!products.isEmpty()) {
            for (Product product : products) {
                byte[] pictureBytes = FileUtilities.readDirectoryContent(IMAGE_DEFAULT_PATH + product.getImage_path());
                String pictureBase64 = Base64.getEncoder().encodeToString(pictureBytes);
                String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
                builder.append("<div class=\"col-6 col-xl-3 col-xxl-3\">\n")
                        .append("<a href=\"pages/product/viewProduct?product_id=")
                        .append(product.getId())
                        .append("\" class=\"MusicInstruments row1-MusicInstruments1\">\n")
                        .append("<div class=\"pt-4 d-flex justify-content-center\">\n")
                        .append("<img src=\"")
                        .append(imageSrc)
                        .append("\" class=\"img-fluid w-50 d-flex text-center justify-self-center align-self-center\">\n")
                        .append("</div>\n")
                        .append("<p class=\"text-center\"><strong>")
                        .append("</br>").append(product.getName())
                        .append("<br>RM").append(product.getQuantity())
                        .append("</strong></p> </a> </div>");
            }
        }
        return builder.toString();
    }

    private void viewProductWithID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("product_id");
        System.err.println(productId);
        StringBuilder builder = new StringBuilder();
        Product product = productServices.getProductWithID(productId);
        if (product != null) {
            byte[] pictureBytes = FileUtilities.readDirectoryContent(IMAGE_DEFAULT_PATH + product.getImage_path());
            String pictureBase64 = Base64.getEncoder().encodeToString(pictureBytes);
            String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
            builder.append("<div class=\"row m-5 d-flex align-items-center justify-content-center\">\n")
                    .append("    <div class=\"col-12 row productPanel\">\n")
                    .append("        <div class=\"col-5 p-5 d-flex justify-content-center align-content-centers productImage\">\n")
                    .append("            <img src=\"").append(imageSrc).append("\" class=\"img-fluid w-100 m-5\">\n")
                    .append("        </div>\n")
                    .append("        <div class=\"col-7 px-5\">\n")
                    .append("            <h1 class=\"m-4\"><strong>").append(product.getName()).append("</p><hr>\n")
                    .append("            <h3 class=\"my-4\"><strong>RM").append(product.getPrice()).append("</strong> </h1>\n")
                    .append("            <h3 class=\"my-4\"><strong>Stock:</strong> ").append(product.getQuantity()).append("</h3>\n")
                    .append("           <h3 class=\"my-4\"><strong>Color:</strong> ").append(product.getColor()).append("</h3><hr>\n")
                    .append("  <h5 class=\"my-2\"><strong>Quantity</strong></h5>")
                    .append(" <form action=\"ProductServlet\" method=\"post\" class=\"qty-container\">")
                    .append("                                              <button class=\"qty-btn-minus\" type=\"button\"><i\n")
                    .append("                                                       class=\"fa fa-minus\"></i></button>\n")
                    .append("                                               <input type=\"text\" name=\"qty\" value=\"0\" class=\"input-qty w-50 text-center p-2\" min=\"1\"/>\n")
                    .append("                                               <button class=\"qty-btn-plus\" type=\"button\"><i\n")
                    .append("                                                       class=\"fa fa-plus\"></i></button><br>\n")
                    .append("          <button type=\"submit\" class=\"my-4 mx-auto p-2 addtocartbtn\"><strong>Add to cart</strong></button>\n")
                     .append("</form>")
                    .append("</div>")
                    .append("</div>");

            request.setAttribute("productDetails", builder.toString());
        }
        request.getRequestDispatcher(Constants.VIEW_PRODUCT_JSP_URL).forward(request, response);
    }

    private void addCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantity = request.getParameter("qty");
        
    }

    private void productNotFound() {

    }

}
