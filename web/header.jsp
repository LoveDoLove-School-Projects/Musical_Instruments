<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

    <head>
    </head>

    <body>
        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-body-tertiary">
            <!-- Container wrapper -->
            <div class="container">
                <!-- Navbar brand -->
                <!--                <a class="navbar-brand me-2" href="#">
                                    <img
                                        src="#"
                                        height="16"
                                        alt="Musical Instruments Logo"
                                        loading="lazy"
                                        style="margin-top: -1px;"
                                        />
                                </a>-->

                <!-- Toggle button -->
                <button data-mdb-collapse-init class="navbar-toggler" type="button"
                        data-mdb-target="#navbarButtonsExample" aria-controls="navbarButtonsExample" aria-expanded="false"
                        aria-label="Toggle navigation">
                    <i class="fas fa-bars"></i>
                </button>

                <!-- Collapsible wrapper -->
                <div class="collapse navbar-collapse" id="navbarButtonsExample">
                    <!-- Left links -->
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="index.jsp">Home</a>
                        </li>
                    </ul>
                    <!-- Left links -->

                    <div class="d-flex align-items-center">
                        <%
                        Integer login_id = (Integer) session.getAttribute("login_id");
                        if (login_id != null) { %>
                        <!-- Print it out -->
                        <a data-mdb-ripple-init class="btn btn-primary px-3 me-2" href="profile.jsp">Profile</a>
                        <form action="LogoutServlet" method="POST">
                            <input type="submit" value="Logout" name="Logout" data-mdb-ripple-init class="btn btn-danger px-3 me-2"/>
                        </form>
                        <% } else { %>
                        <a data-mdb-ripple-init class="btn btn-link px-3 me-2" href="login.jsp">Login</a>
                        <a data-mdb-ripple-init class="btn btn-primary me-3" href="register.jsp">Register for free</a>
                        <% } %>
                    </div>
                </div>
                <!-- Collapsible wrapper -->
            </div>
            <!-- Container wrapper -->
        </nav>
        <!-- Navbar -->
        <jsp:include page="message.jsp" />
    </body>

</html>