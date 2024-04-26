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
            body {
                background-color: #fbfbfb;
            }
            @media (min-width: 991.98px) {
                main {
                    padding-left: 240px;
                }
            }

            /* Sidebar */
            .sidebar {
                position: fixed;
                top: 0;
                bottom: 0;
                left: 0;
                padding: 58px 0 0; /* Height of navbar */
                box-shadow: 0 2px 5px 0 rgb(0 0 0 / 5%), 0 2px 10px 0 rgb(0 0 0 / 5%);
                width: 240px;
                z-index: 1;
            }

            @media (max-width: 991.98px) {
                .sidebar {
                    width: 100%;
                }
            }
            .sidebar .active {
                border-radius: 5px;
                box-shadow: 0 2px 5px 0 rgb(0 0 0 / 16%), 0 2px 10px 0 rgb(0 0 0 / 12%);
            }

            .sidebar-sticky {
                position: relative;
                top: 0;
                height: calc(100vh - 48px);
                padding-top: 0.5rem;
                overflow-x: hidden;
                overflow-y: auto; /* Scrollable contents if viewport is shorter than content. */
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <!--Main layout-->
        <main style="margin-top: 58px;">
            <div class="container pt-4">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/mainDashboard.jsp" class="btn btn-primary btn-lg btn-block">Main dashboard</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/manageStaff.jsp" class="btn btn-primary btn-lg btn-block">Manage Staff</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/staffs/searchCustomer.jsp" class="btn btn-primary btn-lg btn-block">Manage Customer</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/manageStock.jsp" class="btn btn-primary btn-lg btn-block">Manage Stock</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/viewTransaction.jsp" class="btn btn-primary btn-lg btn-block">View Transaction</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/controlPanel.jsp" class="btn btn-primary btn-lg btn-block">Control Panel</a>
                        </div>
                        <div class="col-lg-4 col-md-6 mb-4">
                            <a href="pages/admins/sales.jsp" class="btn btn-primary btn-lg btn-block">Sales</a>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <!--Main layout-->
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
