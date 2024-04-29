<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Registration</title>
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />
        <section style=" background-color: #eee;">
            <form method="POST" action="pages/register" id="registerForm">
                <center>
                    <div class="container py-5 h-100">
                        <div class="row justify-content-center align-items-center h-100">
                            <div class="col-12 col-lg-9 col-xl-7">
                                <div class="card shadow-2-strong card-registration" style="border-radius: 15px">
                                    <div class="card-body p-4 p-md-5">
                                        <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Registration</h3>

                                        <div class="row">
                                            <div class="col-md-6 mb-4">
                                                <div class="form-outline">
                                                    <input type="text" name="username" id="username" class="form-control form-control-lg" value="${username}" required />
                                                    <label class="form-label" for="username">Username</label>
                                                </div>
                                            </div>

                                            <div class="col-md-6 mb-4 d-flex align-items-center">
                                                <div class="form-outline datepicker w-100">
                                                    <input type="text" name="address" id="address" class="form-control form-control-lg" value="${address}" required />
                                                    <label for="address" class="form-label">Address</label>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-6 mb-4">
                                                <div class="form-outline">
                                                    <input type="password" name="password" id="password" class="form-control form-control-lg" minlength="8" value="" required />
                                                    <label class="form-label" for="password">Password</label>
                                                </div>
                                            </div>

                                            <div class="col-md-6 mb-4">
                                                <div class="form-outline">
                                                    <input type="password" name="confirm_password" id="confirm_password" class="form-control form-control-lg" minlength="8" value="" required />
                                                    <label class="form-label" for="confirm_password">Confirm Password</label>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-6 mb-4 pb-2">
                                                <div class="form-outline">
                                                    <input type="email" name="email" id="email" class="form-control form-control-lg" value="${email}" required />
                                                    <label class="form-label" for="email">Email</label>
                                                </div>
                                            </div>
                                            <div class="col-md-6 mb-4 pb-2">
                                                <div class="form-outline">
                                                    <input type="tel" name="phone_number" id="phone_number" class="form-control form-control-lg" value="${phone_number}" pattern="[0-9]+" required />
                                                    <label class="form-label" for="phone_number">Phone Number</label>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-6 mb-4">
                                            <h6 class="mb-2 pb-1">Gender:</h6>

                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender"
                                                       id="maleGender" value="Male"
                                                       <c:choose>
                                                           <c:when test="${empty gender or gender eq 'Male'}">checked</c:when>
                                                       </c:choose> />
                                                <label class="form-check-label" for="maleGender">Male</label>
                                            </div>

                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender"
                                                       id="femaleGender" value="Female"
                                                       <c:choose>
                                                           <c:when test="${gender eq 'Female'}">checked</c:when>
                                                       </c:choose> />
                                                <label class="form-check-label" for="femaleGender">Female</label>
                                            </div>
                                        </div>

                                        <div class="mt-4 pt-2">
                                            <button type="submit" class="btn btn-primary btn-lg" id="registerButton">Register</button>
                                        </div>

                                        <div class="mt-4 pt-2">
                                            <p>
                                                Already registered?
                                                <a href="pages/login">Login Here</a>
                                            </p>
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
        <script type="module" src="assets/js/register.js"></script>
    </body>

</html>