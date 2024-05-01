<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title> Admin Panel </title>
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

            main {
                padding-top: 58px;
            }

            .container {
                margin-top: 20px;
            }

            .col-lg-4 {
                margin-bottom: 20px;
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
            }

            .btn:hover {
                background-color: lightslategray;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <main>
            <div class="container pt-4">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/mainDashboard" class="btn btn-primary btn-lg btn-block">Main dashboard</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/staffs/viewTransaction" class="btn btn-primary btn-lg btn-block">View Transaction</a>
                        </div>
                        <%
                            boolean isAdmin = request.isUserInRole("Admin");
                            if (isAdmin) { %>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/sales" class="btn btn-primary btn-lg btn-block">View Sales</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/searchStaff" class="btn btn-primary btn-lg btn-block">Manage Staff</a>
                        </div>

                        <% } %>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/staffs/searchCustomer" class="btn btn-primary btn-lg btn-block">Manage Customer</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/staffs/manageProduct" class="btn btn-primary btn-lg btn-block">Manage Product</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/staffs/viewLog" class="btn btn-primary btn-lg btn-block">View Log</a>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
