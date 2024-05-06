<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-... (integrity hash)" crossorigin="anonymous" />
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



            .col-lg-4 {
                margin-bottom: 20px;
            }

            .panelBtn {
                width: 100%;
                height: 150px;
                padding: 20px;
                border: groove 20px black;
                background-color: grey;
                color: #fff;
                border-radius: 5px;
                transition: all 0.3s ease;
                font-size: 24px;
                font-weight: bold;
                text-decoration: none;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .panelBtn:hover {
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
                            <a href="pages/admins/internalSecurityLog" class="panelBtn btn-primary btn-lg btn-block">
                                <i class="fas fa-book">  View Internal Log</i>
                            </a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/staffs/viewTransaction" class="panelBtn btn-primary btn-lg btn-block">
                                <i class="fas fa-file-invoice"> View Transaction</i>
                            </a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/superAdmin/viewSales" class="panelBtn btn-primary btn-lg btn-block">
                                <i class="fas fa-chart-line">  View Sales</i>
                            </a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/searchStaff" class="panelBtn btn-primary btn-lg btn-block">
                                <i class="fas fa-user">  Manage Staff</i>
                            </a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/staffs/searchCustomer" class="panelBtn btn-primary btn-lg btn-block">
                                <i class="fas fa-users">  Manage Customer</i>
                            </a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/staffs/staffSearchProduct" class="panelBtn btn-primary btn-lg btn-block">
                                <i class="fas fa-box">  Manage Product</i>
                            </a>
                        </div>

                    </div>
                </div>
            </div>
        </main>

        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
