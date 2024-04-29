<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="entities.Products" %>
<!DOCTYPE html>

<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Modify Customer Information</title>
        <style>
            body {
                padding-top: 20px;
            }
            .form-group {
                margin-bottom: 20px;
            }

            label {
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <div class="container">
            <div class="row">
                <div class="col-md-6 offset-md-3">
                    <h1 class="text-center mb-4">Modify Product Information</h1>
                    <form id="modifyForm" action="pages/staffs/modifyProduct" method="POST" onsubmit="return confirmUpdate()">
                        <% Products productDetails = (Products) session.getAttribute("productDetails"); %>
                        <div class="form-group">
                            <label for="productId">Product ID: <%=productDetails.getProductId()%></label>
                            <input type="hidden" class="form-control" name="productId" value="<%=productDetails.getProductId()%>">
                        </div>
                        <div class="form-group">
                            <label for="productName">Product Name: </label>
                            <input type="text" class="form-control" name="productName" value="<%=productDetails.getName()%>">
                        </div>
                        <div class="form-group">
                            <label for="price">Price: </label>
                            <input type="text" class="form-control" name="price" value="<%=productDetails.getPrice()%>" pattern="\d+(\.\d{2})?" title="Please enter the price with two decimal places (e.g., 10.00)" required>

                        </div>

                        <div class="form-group">
                            <label for="color">Please select a color: </label>
                            <select class="form-control" id="color" name="color">
                                <option value="Red">Red</option>
                                <option value="Orange">Orange</option>
                                <option value="Yellow">Yellow</option>
                                <option value="Green">Green</option>
                                <option value="Blue">Blue</option>
                                <option value="Indigo">Indigo</option>
                                <option value="Violet">Violet</option>
                                <option value="Black">Black</option>
                                <option value="White">White</option>
                                <option value="Brown">Brown</option>
                                <option value="Grey">Grey</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="quantity">Quantity available:  </label>
                            <input type="text" class="form-control" name="quantity"  pattern="[0-9]+" value="<%=productDetails.getQuantity()%>" title="Please enter the quantity in positive number (e.g., 0,1...)"required>
                        </div>
                        <div class="form-group">
                            <label for="category">Please select a category: </label>
                            <select class="form-control" id="category" name="category" required>
                                <option value="Piano" >Piano</option>
                                <option value="Drum">Drum</option>
                                <option value="Guitar">Guitar</option>
                                <option value="Violin">Violin</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="imagePath">Image : </label>
                            <input type="file" name="imagePath" required>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-success">Modify</button>
                            <button type="reset" class="btn btn-danger">Reset</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="/defaults/footer.jsp" />

        <script>
            function confirmModification() {
                if (confirm("Are you sure you want to cancel the modification?")) {
                    window.location.href = "pages/staffs/manageProduct";
                }
            }

            function confirmUpdate() {
                return confirm("Are you sure you want to submit the form?");
            }
        </script>

    </body>
</html>

