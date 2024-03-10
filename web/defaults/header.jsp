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
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light bg-body-tertiary">
            <div class="container">
                <a class="navbar-brand me-2" href="pages/main">
                    <img src="assets/image/logo.png" width="38" height="38" alt="Musical Instruments Logo" loading="lazy"
                         style="margin-top: -1px;" />
                </a>

                <button data-mdb-collapse-init class="navbar-toggler" type="button"
                        data-mdb-target="#navbarButtonsExample" aria-controls="navbarButtonsExample" aria-expanded="false"
                        aria-label="Toggle navigation">
                    <i class="fas fa-bars"></i>
                </button>

                <div class="collapse navbar-collapse" id="navbarButtonsExample">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="pages/main">Home</a>
                        </li>
                    </ul>

                    <div class="d-flex align-items-center">
                        <% Integer login_id=(Integer) session.getAttribute("login_id"); if (login_id !=null) { %>
                        <a data-mdb-ripple-init class="btn btn-primary px-3 me-2" href="pages/profile">Profile</a>
                        <a data-mdb-ripple-init class="btn btn-danger px-3 me-2" href="pages/logout">Logout</a>
                        <% } else { %>
                        <a data-mdb-ripple-init class="btn btn-link px-3 me-2" href="pages/login">Login</a>
                        <a data-mdb-ripple-init class="btn btn-primary me-3" href="pages/register">Register for
                            free</a>
                            <% } %>
                    </div>
                </div>
            </div>
        </nav>
        <jsp:include page="message.jsp" />
    </body>

</html>