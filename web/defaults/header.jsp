<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.security.Principal" %>
<%@ page import="entities.Session" %>
<%@ page import="entities.Role" %>
<%
Principal principal = request.getUserPrincipal();
String j_username = principal == null ? null : principal.getName();
boolean isAdmin = j_username != null ? true : false;
Session user_session = (Session) session.getAttribute("user_session");
%>
<!DOCTYPE html>
<nav class="fixed-top navbar navbar-expand-lg navbar-light bg-body-tertiary">
    <div class="container">
        <a class="navbar-brand me-2" href="#">
            <img src="assets/image/logo.png" width="50" height="50" alt="Musical Instruments Logo"
                 loading="lazy" style="margin-top: -1px;" />
        </a>

        <button class="navbar-toggler" type="button" aria-controls="navbarButtonsExample"
                aria-expanded="false" aria-label="Toggle navigation" data-bs-toggle="collapse" data-bs-target="#navbarButtonsExample">
            <svg  width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
            <path
                d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0" />
            </svg>
        </button>

        <div class="collapse navbar-collapse" id="navbarButtonsExample">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item mx-4">
                    <a class="nav-link" href="#"><strong>Home</strong></a>
                </li>
                <li class="nav-item mx-4">
                    <a class="nav-link" href="pages/products"><strong>Products</strong></a>
                </li>
                <%
              if ((user_session != null && user_session.getRole() != Role.STAFF) && !isAdmin) { // If the user is not a staff member or an admin, they can see the Carts link
                %>

                <li class="nav-item mx-4">
                    <a class="nav-link" href="pages/cart"><strong>Carts</strong></a>
                </li>
                <li class="nav-item mx-4">
                    <a class="nav-link" href="pages/transactionHistory"><strong>Transaction History</strong></a>
                </li>
                <% 
                } 
                %>
                <li class="search-item mx-4">
                    <a class="nav-link" href="pages/productsearch"><svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
                        <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0"/>
                        </svg><i class="bi bi-search"></i></a>
                </li>


                <%
                    if (isAdmin || user_session != null && user_session.getRole() == Role.STAFF) {
                %>
                <li class="nav-item mx-4">
                    <a class="nav-link" href="pages/staffs">Admin Panel</a>
                </li>
                <% 
                } 
                %>
            </ul>


            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle circle-btn" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="fas fa-bars"></i>
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <%
                    if (user_session != null || isAdmin) {
                        if (!isAdmin) { // If the user is not an admin, they can see the Profile link
                    %>
                    <li><a class="dropdown-item" href="pages/profile">Profile</a></li>
                        <%
                            }
                           
                        %>

                    <%
                            
                        if (!isAdmin) {
                    %>
                    <li><a class="dropdown-item" href="pages/securityLog">View Log</a></li>
                        <%
                            }
                        %>
                    <li><a class="dropdown-item" id="logoutBtn">Logout</a></li>
                        <%
                        } else {
                        %>
                    <li><a class="dropdown-item" href="pages/login">Login</a></li>
                    <li><a class="dropdown-item" href="pages/register">Register</a></li>
                    <li><a class="dropdown-item" href="pages/staffLogin">Staff Login</a></li>
                    <li><a class="dropdown-item" href="pages/adminLogin">Admin Login</a></li>
                        <%
                        }
                        %>
                </ul>
            </div>
        </div>
    </div>
</nav>

<jsp:include page="message.jsp" />
<script type="module" src="assets/js/header.js"></script>
