<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List" %>
<%@ page import="entities.Customers" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Customer Details</title>

        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

        <style>

            body {
                padding-top: 20px;
            }
            .card {
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <div class="container">
            <div class="row">
                <div class="col-md-10 offset-md-1">
                    <h1 class="text-center mb-4">Customer Details</h1>
                    <% List<Customers> customersList = (List<Customers>) session.getAttribute("customersList"); %>
                    <% for (Customers customers : customersList) { %>
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title"><strong><%=customers.getUsername() %></strong></h5>
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item"><strong>ID:</strong> <%=customers.getUserId() %></li>
                                <li class="list-group-item"><strong>Gender:</strong> <%=customers.getGender() %></li>
                                <li class="list-group-item"><strong>Email:</strong> <%=customers.getEmail() %></li>
                                <li class="list-group-item"><strong>Address:</strong> <%=customers.getAddress() %></li>
                                <li class="list-group-item"><strong>Phone Number:</strong> <%=customers.getPhoneNumber() %></li>
                                <li class="list-group-item"><strong>Two Factor Authentication:</strong> <%=customers.getTwoFactorAuth() ? "Activated" : "Deactivated" %></li>
                                <li class="list-group-item"><strong>Account Creation Date:</strong> <%=customers.getAccountCreationDate() %></li>
                            </ul>
                            <div class="mt-3">
                                <a href="pages/staffs/manageCustomer.jsp" class="btn btn-primary mr-2">Go back</a>
                                <a href="#" class="btn btn-success mr-2">Modify</a>
                                <a href="#" class="btn btn-danger">Delete</a>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>

        <jsp:include page="/defaults/footer.jsp" />

        <!-- Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
