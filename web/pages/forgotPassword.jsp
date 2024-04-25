<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Forgot Password</title>
        <link rel="stylesheet" href="assets/css/form.css" />
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />
        <section style="background-color: #eee;">
            <form method="POST" action="pages/forgotPassword" id="forgotPasswordForm">
                <center>
                    <div class="container py-5 h-100">
                        <div class="row justify-content-center align-items-center h-100">
                            <div class="col-12 col-lg-9 col-xl-7">
                                <div class="card shadow-2-strong card-registration" style="border-radius: 15px">
                                    <div class="card-body p-4 p-md-5">
                                        <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Forgot Password</h3>

                                        <div class="row">
                                            <div class="form-outline">
                                                <input type="email" name="email" id="email" class="form-control form-control-lg" value="${email}" placeholder="Enter email" required />
                                                <label class="form-label" for="email">Email</label>
                                            </div>
                                        </div>

                                        <div class="mt-4 pt-2">
                                            <button type="submit" class="btn btn-primary btn-lg" id="forgotPasswordButton">Reset Password</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </center>
            </form>
        </section>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module" src="assets/js/forgotPassword.js"></script>
    </body>

</html>