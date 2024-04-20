<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.security.Principal" %>
<%
Principal principal = request.getUserPrincipal();
String j_username = principal != null ? principal.getName() : null;
Integer login_id = (Integer) session.getAttribute("login_id");
%>
<!DOCTYPE html>
<style>
    .navbar{
        z-index:2;
        position: sticky;
    }
    .circle-btn {
        border-radius: 50%;
        width: 50px;
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .dropdown-menu > li {
        cursor: pointer;
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
                <%
                if (principal != null) {
                %>
                <li class="nav-item mx-4 my-2 p-2">
                    <a class="nav-link" href="pages/admins">Admin Panel</a>
                </li>
                <% } %>
            </ul>

            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle circle-btn" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="fas fa-bars"></i>
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <%
                    if (login_id != null || j_username != null) {
                    %>
                    <li><a class="dropdown-item" href="pages/profile">Profile</a></li>
                    <li><a class="dropdown-item" id="logoutBtn">Logout</a></li>
                        <% } else { %>
                    <li><a class="dropdown-item" href="pages/login">Login</a></li>
                    <li><a class="dropdown-item" href="pages/register">Register</a></li>
                    <li><a class="dropdown-item" href="pages/admins">Admin Login</a></li>
                        <% } %>
                </ul>
            </div>
        </div>
    </div>
</nav>
<jsp:include page="message.jsp" />
<script type="module" src="assets/js/header.js"></script>
