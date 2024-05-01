<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Change Password</title>
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />
        <section style="background-color: #eee;">
            <form method="POST" action="pages/changePassword" id="changePasswordForm">
                <center>
                    <div class="container py-5 h-100">
                        <div class="row justify-content-center align-items-center h-100">
                            <div class="col-12 col-lg-9 col-xl-7">
                                <div class="card shadow-2-strong card-registration" style="border-radius: 15px">
                                    <div class="card-body p-4 p-md-5">
                                        <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Change Password</h3>

                                        <div class="row">
                                            <div class="form-outline mb-4">
                                                <input type="password" name="currentPassword" id="currentPassword" class="form-control form-control-lg" value="${currentPassword}" placeholder="Enter current password" required />
                                                <label class="form-label" for="currentPassword">Current Password</label>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="form-outline mb-4">
                                                <input type="password" name="newPassword" id="newPassword" class="form-control form-control-lg" value="${newPassword}" placeholder="Enter new password" required />
                                                <label class="form-label" for="email">New Password</label>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="form-outline mb-2">
                                                <input type="password" name="confirmNewPassword" id="confirmNewPassword" class="form-control form-control-lg" value="${confirmNewPassword}" placeholder="Enter confirm new password" required />
                                                <label class="form-label" for="email">Confirm New Password</label>
                                            </div>
                                        </div>

                                        <div class="mt-4 pt-2">
                                            <button type="submit" class="btn btn-primary btn-lg" id="changePasswordButton">Change Password</button>
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
        <script type="module" src="assets/js/changePassword.js"></script>
    </body>

</html>