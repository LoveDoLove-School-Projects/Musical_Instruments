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
                    <% Customers customerDetails = (Customers) session.getAttribute("customersList"); %>
                    <div class="card">
                        <div class="card-body">
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
                            <div class="mt-3">
                                <a href="pages/staffs/manageCustomer.jsp" class="btn btn-primary mr-2">Go back</a>
                                <a href="pages/staffs/modifyCustomer.jsp" class="btn btn-success mr-2">Modify</a>
                                <%
                                boolean isAdmin = request.isUserInRole("Admin");
                                if (isAdmin) {
                                %>
                                <a href="pages/staffs/manageCustomer.jsp" class="btn btn-danger">Delete</a>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
