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
        <title>Login Page</title>
        <link rel="stylesheet" href="assets/css/form.css" />
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />
        <section class="vh-100" style="background-color: #eee;">
            <form method="POST" action="${loginFormUrl}" id="loginForm">
                <center>
                    <div class="container py-5 h-100">
                        <div class="row justify-content-center align-items-center h-100">
                            <div class="col-12 col-lg-9 col-xl-7">
                                <div class="card shadow-2-strong card-registration" style="border-radius: 15px">
                                    <div class="card-body p-4 p-md-5">
                                        <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Login Form</h3>

                                        <div class="row">
                                            <div class="col-md-6 mb-4 pb-2">
                                                <div class="form-outline">
                                                    <input type="email" name="email" id="email" class="form-control form-control-lg" value="${email}" required />
                                                    <label class="form-label" for="email">Email</label>
                                                </div>
                                            </div>

                                            <div class="col-md-6 mb-4 pb-2">
                                                <div class="form-outline">
                                                    <input type="password" name="password" id="password" class="form-control form-control-lg" minlength="8" value="${password}" required />
                                                    <label class="form-label" for="password">Password</label>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="mt-4 pt-2">
                                            <button type="button" class="btn btn-primary btn-lg" id="loginButton">Login</button>
                                        </div>

                                        <div class="mt-4 pt-2">
                                            <!-- Forgot Password -->
                                            <a href="pages/forgotPassword" class="btn btn-link">Forgot Password?</a>
                                        </div>

                                        <c:if test="${loginFormUrl != 'pages/adminLogin'}">
                                            <div class="mt-4 pt-2">
                                                <p>
                                                    No Account?
                                                    <a href="pages/register">Register Here</a>
                                                </p>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </center>
            </form>
        </section>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module" src="assets/js/login.js"></script>
    </body>

</html>