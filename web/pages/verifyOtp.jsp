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
        <title>Verify Otp</title>
        <link rel="stylesheet" href="assets/css/form.css" />
    </head>

    <body>
    <center>
        <jsp:include page="/defaults/header.jsp" />
        <form method="POST" action="pages/verifyOtp" id="verifyOtp">
            <section class="vh-100" style="background-color: #eee;">
                <div class="container py-5 h-100">
                    <div class="row justify-content-center align-items-center h-100">
                        <div class="col-12 col-lg-9 col-xl-7">
                            <div class="card shadow-2-strong card-registration" style="border-radius: 15px">
                                <div class="card-body p-4 p-md-5">
                                    <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Verify Otp</h3>

                                    <div class="row">
                                        <div class="form-outline">
                                            <label class="form-label" for="email">Email: <%= request.getParameter("email") %></label>
                                            <input type="hidden" name="email" class="form-control form-control-lg" value="<%= request.getParameter("email") %>" />
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="form-outline">
                                            <input type="text" name="otp" class="form-control form-control-lg" maxlength="6" value="${otp}" placeholder="Enter OTP" required />
                                        </div>
                                    </div>

                                    <div class="mt-4 pt-2">
                                        <input class="btn btn-primary btn-lg" type="submit" value="Verify OTP" />
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