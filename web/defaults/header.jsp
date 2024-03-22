<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>
    <head>
        <base href="${basePath}">
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="stylesheet" href="assets/css/header.css" />
    </head>

    <body>
        <nav class="navbar navbar-expand-lg navbar-light bg-body-tertiary">
            <div class="container">
                <a class="navbar-brand me-2" href="pages/main">
                    <img src="assets/image/logo.png" width="38" height="38" alt="Musical Instruments Logo"
                         loading="lazy" style="margin-top: -1px;" />
                </a>

                <button class="navbar-toggler" type="button" aria-controls="navbarButtonsExample"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                         class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                    <path
                        d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0" />
                    </svg>
                </button>

                <div class="collapse navbar-collapse" id="navbarButtonsExample">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item mx-4 my-2 p-2">
                            <a class="nav-link" href="pages/main">Home</a>
                        </li>
                        <li class="nav-item mx-4 my-2 p-2">
                            <a class="nav-link" href="pages/product">Product</a>
                        </li>
                    </ul>

                    <div class="d-flex align-items-center">
                        <c:if test="${sessionScope.login_id != null}">
                            <a class="btn btn-primary px-3 me-2" href="pages/profile">Profile</a>
                            <button class="btn btn-danger px-3 me-2" id="logoutBtn">Logout</button>
                        </c:if>
                        <c:if test="${sessionScope.login_id == null}">
                            <a class="btn btn-link px-3 me-2" href="pages/login">Login</a>
                            <a class="btn btn-primary me-3" href="pages/register">Register for
                                free</a>
                            <a class="btn btn-primary px-3 me-2" href="pages/adminLogin">Admin Login</a>
                        </c:if>
                    </div>
                </div>
            </div>
        </nav>
        <jsp:include page="message.jsp" />
        <script type="module" src="assets/js/header.js"></script>
    </body>

</html>