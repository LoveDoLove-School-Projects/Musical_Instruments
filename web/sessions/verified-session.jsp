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
        <title>2FA Session</title>
        <link rel="stylesheet" href="assets/css/form.css" />
    </head>

    <body>
        <jsp:include page="/defaults/message.jsp" />
        <section class="vh-100" style=" background-color: #eee;">
            <form method="POST" action="sessions/verified-session" id="verifiedSession">
                <center>
                    <div class="container py-5 h-100">
                        <div class="row justify-content-center align-items-center h-100">
                            <div class="col-12 col-lg-9 col-xl-7">
                                <div class="card shadow-2-strong card-registration" style="border-radius: 15px">
                                    <div class="card-body p-4 p-md-5">
                                        <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Two Step Verification</h3>
                                        <h6 class="mb-4 pb-2 pb-md-0 mb-md-5">Enter the verification code we sent to</h6>
                                        <h6 class="mb-4 pb-2 pb-md-0 mb-md-5">${email}</h6>
                                        <h6 class="mb-4 pb-2 pb-md-0 mb-md-5">Enter your 6 digit security code</h6>

                                        <div class="row d-flex justify-content-center">
                                            <div class="col-md-6 mb-4 d-flex align-items-center">
                                                <div class="form-outline datepicker w-100">
                                                    <input type="text" name="otp" id="otp" class="form-control form-control-lg" min="0" maxlength="6" placeholder="XXXXXX" required />
                                                </div>
                                            </div>
                                        </div>

                                        <div class="mt-4 pt-2">
                                            <button type="button" class="btn btn-primary btn-lg" id="submitButton">Submit</button>
                                        </div>

                                        <div class="mt-4 pt-2">
                                            Didn’t get the code ? <a href="#" class="text-primary fw-bold text-decoration-none">Resend</a>
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