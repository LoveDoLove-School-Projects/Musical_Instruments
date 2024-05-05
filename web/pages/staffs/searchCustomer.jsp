<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <meta charset="UTF-8">
        <title>Search Customers</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <style>
            body, html {
                margin: 0;
                padding: 0;
            }
            body {
                min-height: 100vh;
                background-color: #fbfbfb;
                font-family: 'Roboto', sans-serif;
            }
            .container {
                margin-top: 20px;
            }
            .btn {
                width: 100%;
                padding: 15px;
                border: groove 10px black;
                background-color: grey;
                color: #fff;
                border-radius: 5px;
                transition: all 0.3s ease;
                font-size: 22px;
                font-weight: bold;
                text-decoration: none;
                justify-content: center;
                align-content: center;
            }
            .btn:hover {
                background-color: lightslategray;
            }
            .customer-search-box {
                background-color: #009999;
                color: #fff;
                border: 10px double cyan;
                border-radius: 10px;
                padding: 20px;
                text-align: center;
                margin-bottom: 20px;
            }
            .customer-search-box i {
                font-size: 36px;
                margin-bottom: 10px;
            }
            h3 > strong {
                font-size: 37px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <br><h1><i class="fas fa-users">  Customer Management</i></h1><br>
            <div class="customer-search-box">
                <i class="fas fa-users"></i>
                <h3>Number of Customers: <strong>(<c:out value="${customerCount}"/>) </strong></h3>
            </div>
            <form class="form-inline mb-3" action="pages/staffs/searchCustomer" method="POST">
                <input class="col-5 form-control mr-2" type="text" name="searchQuery" placeholder="Enter customer email: "><br>
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
