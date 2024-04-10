<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <meta charset="UTF-8">
        <title>Manage Customers</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <h1>Manage Customers</h1>
            <!-- Search form -->
            <form class="form-inline mb-3" action="search_customer" method="post">
                <input class="form-control mr-2" type="text" name="query" placeholder="Search customers...">
                <button type="submit" class="btn btn-primary">Search</button>
            </form>

            <!-- Customer listing -->
            <table class="table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>John Doe</td>
                        <td>john@example.com</td>
                        <td>
                            <button class="btn btn-info mr-1">Edit</button>
                            <button class="btn btn-danger">Delete</button>
                        </td>
                    </tr>
                    <!-- Add more rows for each customer -->
                </tbody>
            </table>

            <!-- Add customer form -->
            <h2>Add New Customer</h2>
            <form action="add_customer" method="POST">
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" class="form-control" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <button type="submit" class="btn btn-success">Add Customer</button>
            </form>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
