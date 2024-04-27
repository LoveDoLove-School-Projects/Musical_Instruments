<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Add Product</title>
        <style>
            body {
                padding-top: 20px;
            }
            .form-group {
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <div class="container">
            <div class="row">
                <div class="col-md-6 offset-md-3">
                    <h1 class="text-center mb-4">Add Product</h1>
                    <form id="addForm" action="pages/staffs/AddProductServlet" method="POST" onsubmit="return confirmAdd()">

                        <div class="form-group">
                            <label for="productName">Product Name:  </label>
                            <input type="text" class="form-control" name="productName" placeholder="Enter Product Name: " required>
                        </div>
                        <div class="form-group">
                            <label for="price">Price: </label>
                            <input type="text" class="form-control" name="price" placeholder="RM: " pattern="\d+(\.\d{2})?" required>
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
                            <label for="quantity">Quantity:  </label>
                            <input type="text" class="form-control" name="quantity"  pattern="[0-9]+" placeholder="0" required>
                        </div>
                        <div class="form-group">
                            <label for="category">Please select a category: </label>
                            <select class="form-control" id="category" name="category" required>
                                <option value="Piano" >Piano</option>
                                <option value="Drum" checked>Drum</option>
                                <option value="Guitar">Guitar</option>
                                <option value="Violin">Violin</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="imagePath">Image : </label>
                            <input type="file" name="imagePath" required>
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-success">Add</button>
                            <button type="reset" class="btn btn-danger">Reset</button>
                        </div>

                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="/defaults/footer.jsp" />

        <script>
            function confirmAdd() {
                return confirm("Are you sure you want to submit the form?");
            }
        </script>

    </body>
</html>
