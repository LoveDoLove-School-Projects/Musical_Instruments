<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script>
            import { Collapse, Ripple, initMDB } from "mdb-ui-kit";
            initMDB({Collapse, Ripple});
        </script>
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
                <button
                    data-mdb-collapse-init
                    class="navbar-toggler"
                    type="button"
                    data-mdb-target="#navbarButtonsExample"
                    aria-controls="navbarButtonsExample"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                    >
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
                        <button data-mdb-ripple-init type="button" class="btn btn-link px-3 me-2" id="login_button">
                            Login
                        </button>
                        <button data-mdb-ripple-init type="button" class="btn btn-primary me-3" id="register_button">
                            Register for free
                        </button>
                    </div>
                </div>
                <!-- Collapsible wrapper -->
            </div>
            <!-- Container wrapper -->
        </nav>
        <!-- Navbar -->
        <script>
            document.getElementById("login_button").addEventListener("click", () => {
                window.location.href = "login.jsp";
            });
            document.getElementById("register_button").addEventListener("click", () => {
                window.location.href = "register.jsp";
            });
        </script>
    </body>
</html>
