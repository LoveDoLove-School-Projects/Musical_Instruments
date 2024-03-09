<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath"
       value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<c:if test="${not empty sessionScope.login_id}">
    <c:redirect url="${basePath}pages/profile" />
</c:if>
<!DOCTYPE html>
<html>

    <head>
        <base href="${basePath}">
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Register Page</title>
        <link rel="stylesheet" href="assets/css/form.css" />
    </head>

    <body>
    <center>
        <jsp:include page="/defaults/header.jsp" />
        <form method="POST" action="pages/register">
            <section class="vh-100" style="background-color: #eee;">
                <div class="container py-5 h-100">
                    <div class="row justify-content-center align-items-center h-100">
                        <div class="col-12 col-lg-9 col-xl-7">
                            <div class="card shadow-2-strong card-registration" style="border-radius: 15px">
                                <div class="card-body p-4 p-md-5">
                                    <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Registration Form</h3>

                                    <div class="row">
                                        <div class="col-md-6 mb-4">
                                            <div class="form-outline">
                                                <input type="text" name="username"
                                                       class="form-control form-control-lg" value="${username}" required />
                                                <label class="form-label" for="username">Username</label>
                                            </div>
                                        </div>

                                        <div class="col-md-6 mb-4">
                                            <div class="form-outline">
                                                <input type="password" name="password"
                                                       class="form-control form-control-lg" min="8" value="${password}" required />
                                                <label class="form-label" for="password">Password</label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6 mb-4 d-flex align-items-center">
                                            <div class="form-outline datepicker w-100">
                                                <input type="text" name="address"
                                                       class="form-control form-control-lg" value="${address}" required />
                                                <label for="address" class="form-label">Address</label>
                                            </div>
                                        </div>

                                        <div class="col-md-6 mb-4">
                                            <h6 class="mb-2 pb-1">Gender:</h6>

                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender"
                                                       value="Male" checked/>
                                                <label class="form-check-label"
                                                       for="maleGender">Male</label>
                                            </div>

                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender"
                                                       value="Female" />
                                                <label class="form-check-label"
                                                       for="femaleGender">Female</label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6 mb-4 pb-2">
                                            <div class="form-outline">
                                                <input type="email" name="email"
                                                       class="form-control form-control-lg" value="${email}" required />
                                                <label class="form-label" for="email">Email</label>
                                            </div>
                                        </div>
                                        <div class="col-md-6 mb-4 pb-2">
                                            <div class="form-outline">
                                                <input type="tel" name="phone_number"
                                                       class="form-control form-control-lg" value="${phone_number}" required />
                                                <label class="form-label" for="phone_number">Phone
                                                    Number</label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mt-4 pt-2">
                                        <input class="btn btn-primary btn-lg" type="submit"
                                               value="Submit" />
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
            </section>
        </form>
    </center>
    <jsp:include page="/defaults/footer.jsp" />
</body>

</html>