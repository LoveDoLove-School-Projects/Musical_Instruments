<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64"%>
<%@ page import="utilities.FileUtilities"%>
<%@ page import="entities.Products" %>
<jsp:useBean id="productDetails" class="entities.Products" scope="session"></jsp:useBean>
    <!DOCTYPE html>
    <html>
        <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Product Details</title>
        <style>
            body {
                padding-top: 20px;
            }
            .card {
                margin-bottom: 20px;
            }

            .productImage{
                background-color: #d9d9e1;
                border: solid black 0px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <div class="container">

            <div class="row">
                <div class="col-md-10 offset-md-1">
                    <h1 class="text-center mb-4">Product Details</h1>
                    <div class="card">
                        <%
                String pictureBase64 = Base64.getEncoder().encodeToString(productDetails.getImage());
                String imageSrc = "data:image/png;base64," + pictureBase64;
                        %>
                        <div class="card-body">

                            <div class="col-3 p-1 d-flex justify-content-center align-content-centers productImage">
                                <img src="<%if(imageSrc!=null) { %><%=imageSrc%> <%}%>" class="img-fluid w-100 m-5" alt="<%=productDetails.getName() %>">
                            </div>
                            <ul class="list-group list-group-flush">

                                <li class="list-group-item"><strong>Product ID:</strong> <%=productDetails.getProductId() %></li>
                                <li class="list-group-item"><strong>Name:</strong> <%=productDetails.getName() %></li>
                                <li class="list-group-item"><strong>Price(RM):</strong> <%=productDetails.getPrice() %></li>
                                <li class="list-group-item"><strong>Color:</strong> <%=productDetails.getColor() %></li>
                                <li class="list-group-item"><strong>Quantity:</strong> <%=productDetails.getQuantity() %></li>
                                <li class="list-group-item"><strong>Category:</strong> <%=productDetails.getCategory() %></li>

                            </ul>
                            <br>
                            <div class="row">
                                <div class="col-md-2">
                                    <a href="pages/staffs/searchProduct.jsp" class="btn btn-primary mr-2">Go back</a>
                                </div>
                                <div class="col-md-2">
                                    <a href="pages/staffs/modifyProduct.jsp" class="btn btn-success mr-2">Modify</a>
                                </div>
                                <div class="col-md-1">
                                    <%
                                     boolean isAdmin = request.isUserInRole("Admin");
                                     if (isAdmin) { %>
                                    <form action="pages/admins/DeleteProduct" method="post" id="deleteProductForm">
                                        <input type="hidden" name="productId" value="<%=productDetails.getProductId() %>" />
                                        <button class="btn btn-danger">Delete</button>
                                    </form>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
        <%
               if (isAdmin) {
        %>
        <script type="module">
            import { showErrorDialog } from "${basePath}assets/js/dialog.js";
            let deleteProductFormElement = document.getElementById("deleteProductForm");
            function setDeleteProductForm() {
                if (deleteProductFormElement === null) {
                    showErrorDialog("Delete button not found");
                    return;
                }
                deleteProductFormElement.onsubmit = function (event) {
                    event.preventDefault();
                    Swal.fire({
                        title: "Are you sure?",
                        text: "You won't be able to revert this!",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#3085d6",
                        cancelButtonColor: "#d33",
                        confirmButtonText: "Yes, delete it!",
                    }).then((result) => {
                        if (!result.isConfirmed) {
                            event.preventDefault();
                            return
                        }
                        deleteProductFormElement.submit();
                    });
                }
            }
            setDeleteProductForm();
        </script>
        <%
            }
        %>
    </body>
</html>
