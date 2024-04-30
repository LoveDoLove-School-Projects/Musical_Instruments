<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64"%>
<%@ page import="entities.Customers" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Customer Details</title>
        <style>
            body {
                padding-top: 20px;
            }
            .card {
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
                <div class="col-md-10 offset-md-1">
                    <h1 class="text-center mb-4">Customer Details</h1>
                    <% Customers customerDetails = (Customers) session.getAttribute("customerDetails"); %>
                    <div class="card">
                        <div class="card-body">
                            <%
                                String pictureBase64 = Base64.getEncoder().encodeToString(customerDetails.getPicture());
                                String imageSrc = "data:image/png;base64," + pictureBase64;
                            %>
                            <div class="col-3 p-1 d-flex justify-content-center align-content-centers productImage">
                                <img src="<%if(imageSrc!=null) { %><%=imageSrc%> <%}%>" class="img-fluid w-100 m-5" alt="<%=customerDetails.getUsername() %>">
                            </div>
                            <h5 class="card-title"><strong><%=customerDetails.getUsername() %></strong></h5>
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item"><strong>ID:</strong> <%=customerDetails.getUserId() %></li>
                                <li class="list-group-item"><strong>Gender:</strong> <%=customerDetails.getGender() %></li>
                                <li class="list-group-item"><strong>Email:</strong> <%=customerDetails.getEmail() %></li>
                                <li class="list-group-item"><strong>Address:</strong> <%=customerDetails.getAddress() %></li>
                                <li class="list-group-item"><strong>Phone Number:</strong> <%=customerDetails.getPhoneNumber() %></li>
                                <li class="list-group-item"><strong>Two Factor Authentication:</strong> <%=customerDetails.getTwoFactorAuth() ? "Activated" : "Deactivated" %></li>
                                <li class="list-group-item"><strong>Account Creation Date:</strong> <%=customerDetails.getAccountCreationDate() %></li>
                            </ul>
                            <div class="row">
                                <div class="col-md-2">
                                    <a href="pages/staffs/searchCustomer.jsp" class="btn btn-primary mr-2">Go back</a>
                                </div>
                                <div class="col-md-2">
                                    <a href="pages/staffs/modifyCustomer.jsp" class="btn btn-success mr-2">Modify</a>
                                </div>
                                <%
                                     boolean isAdmin = request.isUserInRole("Admin");
                                         if (isAdmin) { %>
                                <div class="col-md-1">
                                    <form action="pages/admins/DeleteCustomerServlet" method="post" id="deleteCustomerForm">
                                        <input type="hidden" name="userId" value="<%=customerDetails.getUserId() %>"/>
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
            let deleteCustomerFormElement = document.getElementById("deleteCustomerForm");
            function setDeleteCustomerForm() {
                if (deleteCustomerFormElement === null) {
                    showErrorDialog("Delete button not found");
                    return;
                }
                deleteCustomerFormElement.onsubmit = function (event) {
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
                        deleteCustomerFormElement.submit();
                    });
                }
            }
            setDeleteCustomerForm();
        </script>
        <%
            }
        %>
    </body>
</html>
