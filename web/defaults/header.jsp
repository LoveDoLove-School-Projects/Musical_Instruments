<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<style>
    .navbar{
        z-index:2;
        position: sticky;
    }
</style>
<nav class="fixed-top navbar navbar-expand-lg navbar-light bg-body-tertiary ">
    <div class="container">
        <a class="navbar-brand me-2" href="pages/main">
            <img src="assets/image/logo.png" width="38" height="38" alt="Musical Instruments Logo"
                 loading="lazy" style="margin-top: -1px;" />
        </a>

        <button class="navbar-toggler" type="button" aria-controls="navbarButtonsExample"
                aria-expanded="false" aria-label="Toggle navigation" data-bs-toggle="collapse" data-bs-target="#navbarButtonsExample">
            <svg  width="16" height="16" fill="currentColor"
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
                <li class="nav-item mx-4 my-2 p-2">
                    <a class="nav-link" href="pages/admin">Admin Panel</a>
                </li>
            </ul>

            <ul class="nav d-flex align-items-center flex-column flex-md-row">
                <c:if test="${sessionScope.login_id != null}">
                    <li class="nav-item me-2 my-1 my-md-0">
                        <a class="btn btn-primary px-3" href="pages/profile">Profile</a>
                    </li>
                    <li class="nav-item me-2 my-1 my-md-0">
                        <button class="btn btn-danger px-3" id="logoutBtn">Logout</button>
                    </li>
                </c:if>
                <c:if test="${sessionScope.login_id == null}">
                    <li class="nav-item me-2 my-1 my-md-0">
                        <a class="btn btn-primary px-3" href="pages/login">Login</a>
                    </li>
                    <li class="nav-item me-3 my-1 my-md-0">
                        <a class="btn btn-primary" href="pages/register">Register for free</a>
                    </li>
                    <li class="nav-item me-2 my-1 my-md-0">
                        <a class="btn btn-danger px-3" href="pages/adminLogin">Admin Login</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>

<jsp:include page="message.jsp" />
<script type="module" src="assets/js/header.js"></script>
